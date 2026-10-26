package vn.edu.hcmute.qaute.service.chat;

// Xử lý nghiệp vụ ChatAccessChecker trong hệ thống.
public interface ChatAccessChecker {
    // Kiểm tra người dùng có tham gia phiên chat hay không.
    boolean isParticipant(Long sessionId, Long userId);
}
