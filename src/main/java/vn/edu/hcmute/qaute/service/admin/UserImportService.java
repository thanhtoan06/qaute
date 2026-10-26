package vn.edu.hcmute.qaute.service.admin;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import jakarta.validation.Validator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.util.CsvUtil;
import vn.edu.hcmute.qaute.dto.request.admin.UserCreateForm;
import vn.edu.hcmute.qaute.dto.view.ImportReport;
import vn.edu.hcmute.qaute.dto.view.ImportReport.RowResult;
import vn.edu.hcmute.qaute.entity.identity.Faculty;
import vn.edu.hcmute.qaute.repository.identity.FacultyRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;

// Xử lý nghiệp vụ UserImport trong hệ thống.
@Service
public class UserImportService {
    private static final long MAX_BYTES = 2L * 1024 * 1024;
    private static final int MAX_ROWS = 2_000;
    private static final List<String> HEADER = List.of(
            "mssv", "fullName", "email", "facultyCode", "classCode", "cohort");

    private final AdminUserService adminUsers;
    private final UserRepository users;
    private final FacultyRepository faculties;
    private final Validator validator;
    private final AuditService audit;
    private final TransactionTemplate rowTransaction;

    // Khởi tạo dịch vụ nhập tài khoản sinh viên từ CSV.
    public UserImportService(AdminUserService adminUsers, UserRepository users,
                             FacultyRepository faculties, Validator validator,
                             AuditService audit, PlatformTransactionManager transactionManager) {
        this.adminUsers = adminUsers;
        this.users = users;
        this.faculties = faculties;
        this.validator = validator;
        this.audit = audit;
        this.rowTransaction = new TransactionTemplate(transactionManager);
        this.rowTransaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    // Xử lý các dòng CSV và tổng hợp kết quả nhập.
    public ImportReport process(MultipartFile file, boolean dryRun, Long actorId) {
        List<CsvUtil.Row> data = readRows(file);
        Set<String> seenMssv = new HashSet<>();
        Set<String> seenEmail = new HashSet<>();
        List<RowResult> results = new ArrayList<>(data.size());
        int ok = 0;
        for (CsvUtil.Row row : data) {
            RowResult result = processRow(row, dryRun, actorId, seenMssv, seenEmail);
            results.add(result);
            if (result.ok()) {
                ok++;
            }
        }
        ImportReport report = new ImportReport(results.size(), ok, results.size() - ok,
                List.copyOf(results));
        if (!dryRun) {
            audit.log(AuditActions.USER_IMPORTED, "User", null, null,
                    "total=" + report.total() + ",ok=" + report.ok() + ",failed=" + report.failed());
        }
        return report;
    }

    // Đọc, giải mã và kiểm tra dữ liệu CSV.
    private List<CsvUtil.Row> readRows(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Vui lòng chọn tệp CSV");
        }
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase(Locale.ROOT).endsWith(".csv")) {
            throw new BadRequestException("Tệp phải có đuôi .csv");
        }
        if (file.getSize() > MAX_BYTES) {
            throw new BadRequestException("Tệp CSV không được vượt quá 2 MB");
        }
        final byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new BadRequestException("Không thể đọc tệp CSV");
        }
        if (bytes.length > MAX_BYTES) {
            throw new BadRequestException("Tệp CSV không được vượt quá 2 MB");
        }
        final String content;
        try {
            content = StandardCharsets.UTF_8.newDecoder().decode(ByteBuffer.wrap(bytes)).toString();
        } catch (CharacterCodingException e) {
            throw new BadRequestException("Tệp CSV phải được mã hóa UTF-8");
        }
        final List<CsvUtil.Row> parsed;
        try {
            parsed = CsvUtil.parse(content);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
        if (parsed.isEmpty() || !HEADER.equals(parsed.get(0).cells())) {
            throw new BadRequestException("Header phải là: " + String.join(",", HEADER));
        }
        List<CsvUtil.Row> data = parsed.stream().skip(1)
                .filter(row -> row.cells().stream().anyMatch(cell -> !cell.isBlank()))
                .toList();
        if (data.isEmpty()) {
            throw new BadRequestException("Tệp CSV chưa có dòng dữ liệu");
        }
        if (data.size() > MAX_ROWS) {
            throw new BadRequestException("Tệp CSV chỉ được tối đa 2000 dòng dữ liệu");
        }
        return data;
    }

    // Kiểm tra hoặc nhập một dòng dữ liệu sinh viên.
    private RowResult processRow(CsvUtil.Row row, boolean dryRun, Long actorId,
                                 Set<String> seenMssv, Set<String> seenEmail) {
        List<String> cells = row.cells();
        String mssv = cells.isEmpty() ? "" : cells.get(0).trim();
        String email = cells.size() < 3 ? "" : cells.get(2).trim().toLowerCase(Locale.ROOT);
        if (cells.size() != HEADER.size()) {
            return new RowResult(row.line(), mssv, email, false,
                    "Dòng phải có đúng 6 cột");
        }
        List<String> errors = new ArrayList<>();
        if (!seenMssv.add(mssv)) {
            errors.add("MSSV trùng trong tệp");
        }
        if (!seenEmail.add(email)) {
            errors.add("Email trùng trong tệp");
        }
        UserCreateForm form = new UserCreateForm();
        form.setRole(RoleCode.STUDENT);
        form.setMssv(mssv);
        form.setFullName(cells.get(1).trim());
        form.setEmail(email);
        form.setClassCode(cells.get(4).trim());
        form.setCohort(cells.get(5).trim());
        if (mssv.isEmpty() || !mssv.matches("[0-9]{8}")) {
            errors.add("MSSV phải gồm đúng 8 chữ số");
        }
        validator.validate(form).forEach(violation -> errors.add(violation.getMessage()));
        String facultyCode = cells.get(3).trim();
        if (!facultyCode.isEmpty()) {
            Faculty faculty = faculties.findByCodeIgnoreCase(facultyCode).orElse(null);
            if (faculty == null) {
                errors.add("Mã khoa không tồn tại: " + facultyCode);
            } else {
                form.setFacultyId(faculty.getId());
            }
        }
        if (mssv.matches("[0-9]{8}") && users.existsByMssv(mssv)) {
            errors.add("MSSV đã tồn tại");
        }
        if (!email.isEmpty() && users.existsByEmailIgnoreCase(email)) {
            errors.add("Email đã tồn tại");
        }
        if (!errors.isEmpty()) {
            return new RowResult(row.line(), mssv, email, false,
                    String.join("; ", errors.stream().distinct().toList()));
        }
        if (dryRun) {
            return new RowResult(row.line(), mssv, email, true, "Hợp lệ");
        }
        try {
            rowTransaction.executeWithoutResult(status -> adminUsers.create(form, actorId));
            return new RowResult(row.line(), mssv, email, true, "Đã tạo và gửi lời mời");
        } catch (DuplicateException | BadRequestException e) {
            return new RowResult(row.line(), mssv, email, false, e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return new RowResult(row.line(), mssv, email, false,
                    "MSSV hoặc email đã tồn tại trong hệ thống");
        } catch (RuntimeException e) {
            return new RowResult(row.line(), mssv, email, false,
                    "Không thể tạo tài khoản hoặc gửi lời mời");
        }
    }
}
