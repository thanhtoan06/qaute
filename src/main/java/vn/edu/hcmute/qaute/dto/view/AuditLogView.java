package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;

public record AuditLogView(Long id, String actorName, String actorRole, String action,
                           String actionLabel, String entityType, Long entityId,
                           String before, String after, String ip, String userAgent,
                           String traceId, LocalDateTime createdAt) {
}
