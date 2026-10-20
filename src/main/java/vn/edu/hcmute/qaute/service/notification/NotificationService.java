package vn.edu.hcmute.qaute.service.notification;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.dto.view.NotificationView;
import vn.edu.hcmute.qaute.dto.view.PreferenceView;
import vn.edu.hcmute.qaute.dto.request.student.PreferenceInput;

public interface NotificationService {

    void notifyUser(Long userId, NotificationType type, String title, String body,
                    String linkUrl, String entityType, Long entityId);

    void notifyUsers(Collection<Long> userIds, NotificationType type, String title, String body,
                     String linkUrl, String entityType, Long entityId);

    void notifyUser(Long userId, NotificationType type, Map<String, String> details);

    Page<NotificationView> list(Long userId, boolean unreadOnly, Pageable p);

    long unreadCount(Long userId);

    void markRead(Long userId, Long id);

    void markAllRead(Long userId);

    void hide(Long userId, Long id);

    List<PreferenceView> getPreferences(Long userId);

    void savePreferences(Long userId, Map<NotificationType, PreferenceInput> input);
}
