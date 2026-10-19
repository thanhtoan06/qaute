package vn.edu.hcmute.qaute.repository.notification;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.entity.notification.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByUserIdAndDeletedAtIsNullOrderByIdDesc(Long userId, Pageable pageable);

    Page<Notification> findByUserIdAndDeletedAtIsNullAndReadAtIsNullOrderByIdDesc(Long userId, Pageable pageable);

    long countByUserIdAndDeletedAtIsNullAndReadAtIsNull(Long userId);

    @Modifying
    @Query("UPDATE Notification n SET n.readAt = :now WHERE n.userId = :userId AND n.readAt IS NULL AND n.deletedAt IS NULL")
    int markAllRead(@Param("userId") Long userId, @Param("now") LocalDateTime now);
}

