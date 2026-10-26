package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;

// Đóng gói dữ liệu AuthSession để hiển thị hoặc trả về API.
public record AuthSessionView(
        Long id,
        String deviceInfo,
        String ip,
        LocalDateTime createdAt,
        LocalDateTime lastUsedAt,
        boolean revoked,
        boolean expired,
        boolean current) {
}
