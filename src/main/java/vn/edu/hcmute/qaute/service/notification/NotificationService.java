package vn.edu.hcmute.qaute.service.notification;

import java.util.Map;
import vn.edu.hcmute.qaute.common.constant.NotificationType;

/** Optional in-app notification integration; callers send mandatory security email separately. */
public interface NotificationService {
    void notifyUser(Long userId, NotificationType type, Map<String, String> details);
}
