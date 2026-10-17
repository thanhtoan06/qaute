package vn.edu.hcmute.qaute.controller.admin;

import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.util.CsvUtil;
import vn.edu.hcmute.qaute.dto.view.ImportReport;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.admin.UserImportService;

@Controller
public class AdminUserImportController {
    private static final List<String> ERROR_HEADER = List.of("line", "mssv", "email", "message");
    private final UserImportService imports;

    public AdminUserImportController(UserImportService imports) {
        this.imports = imports;
    }

    @GetMapping("/admin/users/import")
    public String page(Model model) {
        model.addAttribute("pageTitle", "Nhập sinh viên từ CSV");
        return "admin/user-import";
    }

    @GetMapping("/admin/users/import/template")
    public ResponseEntity<byte[]> template() {
        String csv = CsvUtil.row("mssv", "fullName", "email", "facultyCode", "classCode", "cohort")
                + CsvUtil.row("12345678", "Nguyễn Văn A", "student@example.edu.vn", "", "20DTH1", "2024");
        return csvDownload(csv, "qaute-sinh-vien-mau.csv");
    }

    @PostMapping("/admin/users/import")
    public String importUsers(@RequestParam("file") MultipartFile file,
                              @RequestParam(defaultValue = "true") boolean dryRun, Model model) {
        model.addAttribute("pageTitle", "Nhập sinh viên từ CSV");
        model.addAttribute("dryRun", dryRun);
        try {
            ImportReport report = imports.process(file, dryRun, SecurityUtils.requireUserId());
            model.addAttribute("report", report);
            if (report.failed() > 0) {
                StringBuilder errors = new StringBuilder(CsvUtil.row("line", "mssv", "email", "message"));
                report.rows().stream().filter(row -> !row.ok()).forEach(row -> errors.append(
                        CsvUtil.row(String.valueOf(row.line()), row.mssv(), row.email(), row.message())));
                model.addAttribute("errorCsv", errors.toString());
            }
        } catch (BadRequestException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "admin/user-import";
    }

    @PostMapping("/admin/users/import/errors")
    public ResponseEntity<byte[]> errors(@RequestParam String csv) {
        if (csv == null || csv.length() > 2_000_000) {
            throw new BadRequestException("CSV lỗi không hợp lệ");
        }
        final List<CsvUtil.Row> parsed;
        try {
            parsed = CsvUtil.parse(csv);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("CSV lỗi không hợp lệ");
        }
        if (parsed.isEmpty() || !ERROR_HEADER.equals(parsed.get(0).cells())
                || parsed.size() > 2001) {
            throw new BadRequestException("CSV lỗi không hợp lệ");
        }
        StringBuilder output = new StringBuilder();
        for (CsvUtil.Row row : parsed) {
            if (row.cells().size() != ERROR_HEADER.size()) {
                throw new BadRequestException("CSV lỗi không hợp lệ");
            }
            output.append(CsvUtil.row(row.cells().toArray(String[]::new)));
        }
        return csvDownload(output.toString(), "qaute-dong-loi.csv");
    }

    private static ResponseEntity<byte[]> csvDownload(String csv, String filename) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(filename, StandardCharsets.UTF_8).build().toString())
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(CsvUtil.withBom(csv));
    }
}
