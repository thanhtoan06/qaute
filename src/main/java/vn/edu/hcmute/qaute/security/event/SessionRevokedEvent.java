package vn.edu.hcmute.qaute.security.event;

public record SessionRevokedEvent(Long userId, String jti) {
}
