package vn.edu.hcmute.qaute.security.event;

// Hỗ trợ xác thực và kiểm soát truy cập qua SessionRevokedEvent.
public record SessionRevokedEvent(Long userId, String jti) {
}
