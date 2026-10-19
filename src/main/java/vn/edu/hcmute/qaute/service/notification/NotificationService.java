package vn.edu.hcmute.qaute.service.notification;

import java.util.Collection;
import java.util.Map;
import vn.edu.hcmute.qaute.common.constant.NotificationType;

public interface NotificationService {

    void notifyUser(Long userId, NotificationType type, String title, String body,
                    String linkUrl, String entityType, Long entityId);

    void notifyUsers(Collection<Long> userIds, NotificationType type, String title, String body,
                     String linkUrl, String entityType, Long entityId);

    void notifyUser(Long userId, NotificationType type, Map<String, String> details);
}
