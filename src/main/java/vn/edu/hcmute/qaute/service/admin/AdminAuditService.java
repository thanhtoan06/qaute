package vn.edu.hcmute.qaute.service.admin;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.util.CsvUtil;
import vn.edu.hcmute.qaute.common.util.BoundedPages;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.dto.request.admin.AuditFilter;
import vn.edu.hcmute.qaute.dto.view.AuditLogView;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.governance.AuditLog;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.governance.AuditLogRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;

// Xử lý nghiệp vụ AdminAudit trong hệ thống.
@Service
@Transactional(readOnly = true)
public class AdminAuditService {
    private static final int EXPORT_LIMIT = 5000;
    private static final Map<String, String> ACTION_LABELS = labels();

    private final AuditLogRepository logs;
    private final UserLookupService users;

    // Khởi tạo dịch vụ tra cứu nhật ký quản trị.
    public AdminAuditService(AuditLogRepository logs, UserLookupService users) {
        this.logs = logs;
        this.users = users;
    }

    // Tìm kiếm nhật ký theo bộ lọc và phân trang.
    public Page<AuditLogView> search(AuditFilter filter, Pageable pageable) {
        Page<AuditLog> page = logs.findAll(specification(filter), BoundedPages.of(pageable));
        Map<Long, UserSummary> actors = summaries(page.getContent());
        return page.map(log -> view(log, actors));
    }

    // Lấy một bản ghi nhật ký theo mã.
    public AuditLogView get(Long id) {
        AuditLog log = logs.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy dòng nhật ký"));
        return view(log, summaries(List.of(log)));
    }

    // Liệt kê các mã hành động đã được ghi nhận.
    public List<String> actions() {
        return logs.findDistinctActions();
    }

    // Chuyển mã hành động thành nhãn hiển thị.
    public String actionLabel(String action) {
        return ACTION_LABELS.getOrDefault(action, action == null ? "" : action.replace('_', ' '));
    }

    // Xuất nhật ký phù hợp thành tệp CSV.
    public byte[] exportCsv(AuditFilter filter) {
        List<AuditLog> rows = logs.findAll(specification(filter),
                PageRequest.of(0, EXPORT_LIMIT, Sort.by(Sort.Order.desc("createdAt"),
                        Sort.Order.desc("id")))).getContent();
        Map<Long, UserSummary> actors = summaries(rows);
        StringBuilder csv = new StringBuilder();
        csv.append(CsvUtil.row("ID", "Thời gian", "Người thực hiện", "Vai trò", "Hành động",
                "Mã hành động", "Loại đối tượng", "ID đối tượng", "Trước", "Sau",
                "IP", "User-Agent", "Trace ID"));
        for (AuditLog log : rows) {
            AuditLogView row = view(log, actors);
            csv.append(CsvUtil.row(string(row.id()), string(row.createdAt()), row.actorName(),
                    row.actorRole(), row.actionLabel(), row.action(), row.entityType(),
                    string(row.entityId()), row.before(), row.after(), row.ip(),
                    row.userAgent(), row.traceId()));
        }
        return CsvUtil.withBom(csv.toString());
    }

    // Tạo điều kiện truy vấn từ bộ lọc nhật ký.
    private Specification<AuditLog> specification(AuditFilter filter) {
        AuditFilter f = filter == null ? new AuditFilter() : filter;
        LocalDate from = f.getFrom();
        LocalDate to = f.getTo();
        if (from != null && to != null && from.isAfter(to)) {
            throw new BadRequestException("Ngày bắt đầu không được sau ngày kết thúc");
        }
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (f.getActorId() != null) predicates.add(cb.equal(root.get("actorId"), f.getActorId()));
            if (f.getEntityId() != null) predicates.add(cb.equal(root.get("entityId"), f.getEntityId()));
            if (f.getAction() != null && !f.getAction().isBlank())
                predicates.add(cb.equal(root.get("action"), f.getAction().trim()));
            if (f.getEntityType() != null && !f.getEntityType().isBlank())
                predicates.add(cb.equal(cb.lower(root.get("entityType")),
                        f.getEntityType().trim().toLowerCase(Locale.ROOT)));
            if (from != null) predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), from.atStartOfDay()));
            if (to != null) predicates.add(cb.lessThan(root.get("createdAt"), to.plusDays(1).atStartOfDay()));
            if (f.getActorQ() != null && !f.getActorQ().isBlank()) {
                String pattern = "%" + TextUtil.escapeLike(f.getActorQ().trim().toLowerCase(Locale.ROOT)) + "%";
                Subquery<Long> actor = query.subquery(Long.class);
                Root<User> user = actor.from(User.class);
                actor.select(user.get("id")).where(cb.equal(user.get("id"), root.get("actorId")),
                        cb.or(cb.like(cb.lower(user.get("fullName")), pattern, '\\'),
                                cb.like(cb.lower(user.get("email")), pattern, '\\')));
                predicates.add(cb.exists(actor));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    // Lấy thông tin tóm tắt của những người thực hiện.
    private Map<Long, UserSummary> summaries(List<AuditLog> rows) {
        Set<Long> actorIds = rows.stream().map(AuditLog::getActorId)
                .filter(id -> id != null).collect(Collectors.toSet());
        return actorIds.isEmpty() ? Map.of() : users.getSummaries(actorIds);
    }

    // Chuyển bản ghi nhật ký thành dữ liệu hiển thị.
    private AuditLogView view(AuditLog log, Map<Long, UserSummary> actors) {
        UserSummary actor = log.getActorId() == null ? null : actors.get(log.getActorId());
        String actorName = log.getActorId() == null ? "Hệ thống"
                : actor == null ? "Người dùng #" + log.getActorId() : actor.fullName();
        return new AuditLogView(log.getId(), actorName, log.getActorRole(), log.getAction(),
                actionLabel(log.getAction()), log.getEntityType(), log.getEntityId(),
                log.getSummaryBefore(), log.getSummaryAfter(), log.getIp(),
                log.getUserAgent(), log.getTraceId(), log.getCreatedAt());
    }

    // Chuyển giá trị sang chuỗi, dùng chuỗi rỗng nếu null.
    private static String string(Object value) {
        return value == null ? "" : value.toString();
    }

    // Tạo bảng nhãn tiếng Việt cho các hành động.
    private static Map<String, String> labels() {
        Map<String, String> result = new HashMap<>();
        result.put(AuditActions.USER_CREATED, "Tạo tài khoản");
        result.put(AuditActions.USER_UPDATED, "Cập nhật tài khoản");
        result.put(AuditActions.USER_LOCKED, "Khóa tài khoản");
        result.put(AuditActions.USER_UNLOCKED, "Mở khóa tài khoản");
        result.put(AuditActions.USER_DEACTIVATED, "Vô hiệu hóa tài khoản");
        result.put(AuditActions.USER_REACTIVATED, "Kích hoạt lại tài khoản");
        result.put(AuditActions.USER_ROLE_CHANGED, "Đổi vai trò");
        result.put(AuditActions.USER_FORCE_RESET, "Buộc đặt lại mật khẩu");
        result.put(AuditActions.USER_PASSWORD_CHANGED, "Admin đổi mật khẩu người dùng");
        result.put(AuditActions.USER_SESSION_REVOKED, "Thu hồi phiên đăng nhập");
        result.put(AuditActions.USER_IMPORTED, "Nhập sinh viên từ CSV");
        result.put(AuditActions.USER_INVITATION_RESENT, "Gửi lại lời mời kích hoạt");
        result.put(AuditActions.SYSTEM_TEST_EMAIL_QUEUED, "Gửi email kiểm tra hệ thống");
        result.put(AuditActions.VIEW_STUDENT_PROFILE, "Xem hồ sơ sinh viên");
        result.put(AuditActions.REPORT_EXPORT, "Xuất báo cáo CSV");
        result.put(AuditActions.TICKET_REASSIGNED, "Phân công lại yêu cầu");
        result.put(AuditActions.TICKET_PRIORITY_CHANGED, "Đổi ưu tiên yêu cầu");
        result.put(AuditActions.TICKET_CATEGORY_CHANGED, "Đổi chuyên mục yêu cầu");
        result.put(AuditActions.TICKET_FORCE_CLOSED, "Đóng yêu cầu cưỡng bức");
        result.put(AuditActions.APPOINTMENT_BOOKED, "Đặt lịch hẹn");
        result.put(AuditActions.APPOINTMENT_CANCELLED_BY_STUDENT, "Sinh viên hủy lịch hẹn");
        result.put(AuditActions.APPOINTMENT_CANCELLED_BY_MANAGER, "Manager hủy lịch hẹn");
        result.put(AuditActions.APPOINTMENT_RESCHEDULED, "Đổi lịch hẹn");
        result.put(AuditActions.APPOINTMENT_COMPLETED, "Hoàn tất lịch hẹn");
        result.put(AuditActions.APPOINTMENT_NO_SHOW, "Đánh dấu sinh viên vắng");
        result.put(AuditActions.CHAT_SESSION_STARTED, "Bắt đầu phiên chat");
        result.put(AuditActions.CHAT_AGENT_REQUESTED, "Yêu cầu tư vấn viên trong chat");
        result.put(AuditActions.CHAT_AGENT_ACCEPTED, "Tư vấn viên nhận phiên chat");
        result.put(AuditActions.CHAT_SESSION_CANCELLED, "Hủy phiên chat");
        result.put(AuditActions.CHAT_SESSION_ENDED, "Kết thúc phiên chat");
        result.put(AuditActions.CHAT_SESSION_EXPIRED, "Phiên chat hết hạn");
        result.put(AuditActions.CHAT_TRANSFERRED, "Chuyển phiên chat");
        result.put(AuditActions.SETTING_CHANGED, "Đổi cài đặt");
        result.put(AuditActions.SLA_POLICY_CHANGED, "Đổi chính sách SLA");
        result.put(AuditActions.ANNOUNCEMENT_CHANGED, "Đổi thông báo chính thức");
        return Map.copyOf(result);
    }
}
