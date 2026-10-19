package vn.edu.hcmute.qaute.service.notification;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.NotificationType;

public record NotificationCreatedEvent(
        Long id,
        Long userId,
        NotificationType type,
        String title,
        String body,
        String linkUrl,
        LocalDateTime createdAt) {
}

