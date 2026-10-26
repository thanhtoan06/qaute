package vn.edu.hcmute.qaute.repository.notification;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.entity.notification.NotificationPreference;

// Khai báo truy vấn lưu trữ dữ liệu NotificationPreference.
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {

    // Lấy danh sách tùy chọn thông báo của người dùng.
    List<NotificationPreference> findByUserId(Long userId);

    // Tìm tùy chọn thông báo theo người dùng và loại sự kiện.
    Optional<NotificationPreference> findByUserIdAndEventType(Long userId, NotificationType eventType);
}

