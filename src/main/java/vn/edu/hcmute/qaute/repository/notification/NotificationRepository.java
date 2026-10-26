package vn.edu.hcmute.qaute.repository.notification;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.entity.notification.Notification;

// Khai báo truy vấn lưu trữ dữ liệu Notification.
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Lấy thông báo chưa xóa của người dùng, mới nhất trước.
    Page<Notification> findByUserIdAndDeletedAtIsNullOrderByIdDesc(Long userId, Pageable pageable);

    // Lấy thông báo chưa đọc và chưa xóa của người dùng.
    Page<Notification> findByUserIdAndDeletedAtIsNullAndReadAtIsNullOrderByIdDesc(Long userId, Pageable pageable);

    // Đếm thông báo chưa đọc và chưa xóa của người dùng.
    long countByUserIdAndDeletedAtIsNullAndReadAtIsNull(Long userId);

    // Đánh dấu các thông báo chưa đọc, chưa xóa là đã đọc.
    @Modifying
    @Query("UPDATE Notification n SET n.readAt = :now WHERE n.userId = :userId AND n.readAt IS NULL AND n.deletedAt IS NULL")
    int markAllRead(@Param("userId") Long userId, @Param("now") LocalDateTime now);
}

