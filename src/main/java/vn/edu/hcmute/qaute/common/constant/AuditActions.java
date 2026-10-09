package vn.edu.hcmute.qaute.common.constant;

/**
 * Danh mục mã hành động ghi vào nhật ký hệ thống (cột audit_logs.action, tối đa 60 ký tự).
 * Giá trị của mỗi hằng đúng bằng tên hằng. Khi thêm hành động mới, thêm hằng vào đây rồi gọi
 * AuditService.log(AuditActions.XXX, ...), không viết chuỗi trực tiếp ở nơi gọi.
 */
public final class AuditActions {

    // ----- Xác thực -----
    public static final String AUTH_LOCKED = "AUTH_LOCKED";
    public static final String AUTH_PASSWORD_CHANGED = "AUTH_PASSWORD_CHANGED";
    public static final String AUTH_PASSWORD_RESET = "AUTH_PASSWORD_RESET";
    public static final String AUTH_ACTIVATED = "AUTH_ACTIVATED";

    // ----- Người dùng -----
    public static final String USER_CREATED = "USER_CREATED";
    public static final String USER_UPDATED = "USER_UPDATED";
    public static final String USER_LOCKED = "USER_LOCKED";
    public static final String USER_UNLOCKED = "USER_UNLOCKED";
    public static final String USER_DEACTIVATED = "USER_DEACTIVATED";
    public static final String USER_REACTIVATED = "USER_REACTIVATED";
    public static final String USER_ROLE_CHANGED = "USER_ROLE_CHANGED";
    public static final String USER_FORCE_RESET = "USER_FORCE_RESET";
    public static final String USER_SESSION_REVOKED = "USER_SESSION_REVOKED";
    public static final String USER_IMPORTED = "USER_IMPORTED";

    // ----- Tổ chức -----
    public static final String DEPARTMENT_CREATED = "DEPARTMENT_CREATED";
    public static final String DEPARTMENT_UPDATED = "DEPARTMENT_UPDATED";
    public static final String DEPARTMENT_TOGGLED = "DEPARTMENT_TOGGLED";
    public static final String CATEGORY_CREATED = "CATEGORY_CREATED";
    public static final String CATEGORY_UPDATED = "CATEGORY_UPDATED";
    public static final String CATEGORY_TOGGLED = "CATEGORY_TOGGLED";
    public static final String MANAGER_SCOPE_CHANGED = "MANAGER_SCOPE_CHANGED";

    // ----- Tri thức (FAQ, thẻ, từ đồng nghĩa) -----
    public static final String FAQ_CREATED = "FAQ_CREATED";
    public static final String FAQ_UPDATED = "FAQ_UPDATED";
    public static final String FAQ_SUBMITTED = "FAQ_SUBMITTED";
    public static final String FAQ_PUBLISHED = "FAQ_PUBLISHED";
    public static final String FAQ_REJECTED = "FAQ_REJECTED";
    public static final String FAQ_ARCHIVED = "FAQ_ARCHIVED";
    public static final String FAQ_RESTORED = "FAQ_RESTORED";
    public static final String FAQ_REVISION_RESTORED = "FAQ_REVISION_RESTORED";
    public static final String FAQ_FROM_TICKET = "FAQ_FROM_TICKET";
    public static final String TAG_CHANGED = "TAG_CHANGED";
    public static final String SYNONYM_CHANGED = "SYNONYM_CHANGED";

    // ----- Chính sách, cấu hình, thông báo chính thức -----
    public static final String SLA_POLICY_CHANGED = "SLA_POLICY_CHANGED";
    public static final String BUSINESS_HOURS_CHANGED = "BUSINESS_HOURS_CHANGED";
    public static final String HOLIDAY_CHANGED = "HOLIDAY_CHANGED";
    public static final String SETTING_CHANGED = "SETTING_CHANGED";
    public static final String ANNOUNCEMENT_CHANGED = "ANNOUNCEMENT_CHANGED";

    // ----- Yêu cầu tư vấn -----
    public static final String TICKET_REASSIGNED = "TICKET_REASSIGNED";
    public static final String TICKET_PRIORITY_CHANGED = "TICKET_PRIORITY_CHANGED";
    public static final String TICKET_CATEGORY_CHANGED = "TICKET_CATEGORY_CHANGED";
    public static final String TICKET_FORCE_CLOSED = "TICKET_FORCE_CLOSED";

    // ----- Hành động nhạy cảm khác -----
    public static final String VIEW_STUDENT_PROFILE = "VIEW_STUDENT_PROFILE";
    public static final String APPOINTMENT_CANCELLED_BY_MANAGER = "APPOINTMENT_CANCELLED_BY_MANAGER";
    public static final String CHAT_TRANSFERRED = "CHAT_TRANSFERRED";
    public static final String MEDIA_DELETED = "MEDIA_DELETED";

    private AuditActions() {
    }
}
