package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;

public record AuthSessionView(
        Long id,
        String deviceInfo,
        String ip,
        LocalDateTime createdAt,
        LocalDateTime lastUsedAt,
        boolean revoked,
        boolean current) {
}
