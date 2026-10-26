package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;

// Đóng gói dữ liệu AuditLog để hiển thị hoặc trả về API.
public record AuditLogView(Long id, String actorName, String actorRole, String action,
                           String actionLabel, String entityType, Long entityId,
                           String before, String after, String ip, String userAgent,
                           String traceId, LocalDateTime createdAt) {
}
