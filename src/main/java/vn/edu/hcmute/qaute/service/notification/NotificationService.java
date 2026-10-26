package vn.edu.hcmute.qaute.service.notification;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.dto.view.NotificationView;
import vn.edu.hcmute.qaute.dto.view.PreferenceView;
import vn.edu.hcmute.qaute.dto.request.student.PreferenceInput;

// Xử lý nghiệp vụ Notification trong hệ thống.
public interface NotificationService {

    // Tạo thông báo cho một người dùng với nội dung và đối tượng liên quan.
    void notifyUser(Long userId, NotificationType type, String title, String body,
                    String linkUrl, String entityType, Long entityId);

    // Tạo thông báo giống nhau cho nhiều người dùng.
    void notifyUsers(Collection<Long> userIds, NotificationType type, String title, String body,
                     String linkUrl, String entityType, Long entityId);

    // Tạo thông báo cho một người dùng từ các thông tin dạng bản đồ.
    void notifyUser(Long userId, NotificationType type, Map<String, String> details);

    // Lấy danh sách thông báo theo trạng thái đọc của người dùng.
    Page<NotificationView> list(Long userId, boolean unreadOnly, Pageable p);

    // Đếm số thông báo chưa đọc của người dùng.
    long unreadCount(Long userId);

    // Đánh dấu một thông báo là đã đọc.
    void markRead(Long userId, Long id);

    // Đánh dấu tất cả thông báo của người dùng là đã đọc.
    void markAllRead(Long userId);

    // Ẩn một thông báo khỏi danh sách của người dùng.
    void hide(Long userId, Long id);

    // Lấy tùy chọn nhận thông báo của người dùng.
    List<PreferenceView> getPreferences(Long userId);

    // Lấy tùy chọn thông báo theo vai trò người dùng.
    List<PreferenceView> getPreferences(Long userId, RoleCode role);

    // Lưu tùy chọn nhận thông báo của người dùng.
    void savePreferences(Long userId, Map<NotificationType, PreferenceInput> input);

    // Lưu tùy chọn thông báo hợp lệ theo vai trò người dùng.
    void savePreferences(Long userId, RoleCode role, Map<NotificationType, PreferenceInput> input);
}
