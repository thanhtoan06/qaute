package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.entity.notification.Notification;

public record NotificationView(
        Long id,
        NotificationType type,
        String typeLabel,
        String title,
        String body,
        String linkUrl,
        boolean read,
        LocalDateTime createdAt) {

    public static NotificationView from(Notification notification) {
        if (notification == null) {
            return null;
        }
        return new NotificationView(
                notification.getId(),
                notification.getType(),
                notification.getType() != null ? notification.getType().getLabel() : "",
                notification.getTitle(),
                notification.getBody(),
                notification.getLinkUrl(),
                notification.getReadAt() != null,
                notification.getCreatedAt()
        );
    }
}

