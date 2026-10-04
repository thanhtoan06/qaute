package vn.edu.hcmute.qaute.common.constant;

public enum NotificationType {
    TICKET_CREATED("Yêu cầu đã được tạo", false, true),
    TICKET_NEW_IN_QUEUE("Có yêu cầu mới trong hàng đợi", false, false),
    TICKET_ASSIGNED("Yêu cầu đã được nhận xử lý", false, true),
    TICKET_ADVISOR_REPLIED("Tư vấn viên đã phản hồi", false, true),
    TICKET_STUDENT_REPLIED("Sinh viên đã phản hồi", false, false),
    TICKET_INFO_REQUESTED("Cần bạn bổ sung thông tin", true, true),
    TICKET_REMINDER_WAITING("Nhắc phản hồi yêu cầu", false, true),
    TICKET_RESOLVED("Yêu cầu đã được giải quyết", false, true),
    TICKET_AUTO_CLOSED("Yêu cầu đã tự động đóng", false, true),
    TICKET_REOPENED("Yêu cầu được mở lại", false, false),
    TICKET_ESCALATED("Yêu cầu được chuyển cấp", false, true),
    TICKET_FORCE_CLOSED("Yêu cầu bị đóng bởi quản trị", false, true),
    SLA_AT_RISK("Yêu cầu sắp quá hạn xử lý", false, false),
    SLA_BREACHED("Yêu cầu đã quá hạn xử lý", false, false),
    REVIEW_LOW_RATING("Có đánh giá thấp", false, false),
    APPOINTMENT_CONFIRMED("Lịch hẹn đã xác nhận", false, true),
    APPOINTMENT_CHANGED("Lịch hẹn đã thay đổi", false, true),
    APPOINTMENT_CANCELLED("Lịch hẹn đã hủy", false, true),
    APPOINTMENT_REMINDER("Nhắc lịch hẹn", false, true),
    CHAT_WAITING_NEW("Có sinh viên đang chờ chat", false, false),
    CHAT_MISSED("Phiên chat không được tiếp nhận", false, false),
    FAQ_REVIEW_REQUESTED("Có bài FAQ chờ duyệt", false, false),
    FAQ_APPROVED("Bài FAQ đã được duyệt", false, false),
    FAQ_REJECTED("Bài FAQ bị từ chối", false, false),
    FAQ_FROM_TICKET_PUBLISHED("Câu hỏi của bạn đã trở thành FAQ", false, false),
    ANNOUNCEMENT_PUBLISHED("Thông báo mới từ nhà trường", false, false),
    SECURITY_ACCOUNT_LOCKED("Tài khoản bị khóa", true, true),
    SECURITY_PASSWORD_CHANGED("Mật khẩu đã được đổi", true, true),
    SECURITY_ROLE_CHANGED("Quyền tài khoản đã thay đổi", true, true);

    private final String label;
    private final boolean mandatoryEmail;
    private final boolean defaultEmail;

    NotificationType(String label, boolean mandatoryEmail, boolean defaultEmail) {
        this.label = label;
        this.mandatoryEmail = mandatoryEmail;
        this.defaultEmail = defaultEmail;
    }

    public String getLabel() {
        return label;
    }

    public boolean isMandatoryEmail() {
        return mandatoryEmail;
    }

    public boolean isDefaultEmail() {
        return defaultEmail;
    }
}
