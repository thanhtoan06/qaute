package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.entity.notification.Notification;

// Đóng gói dữ liệu Notification để hiển thị hoặc trả về API.
public record NotificationView(
        Long id,
        NotificationType type,
        String typeLabel,
        String title,
        String body,
        String linkUrl,
        boolean read,
        LocalDateTime createdAt) {

    // Chuyển thông báo entity thành dữ liệu hiển thị.
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
