package vn.edu.hcmute.qaute.repository.organization;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.edu.hcmute.qaute.entity.organization.ManagerProfile;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;

// Khai báo truy vấn lưu trữ dữ liệu ManagerProfile.
public interface ManagerProfileRepository extends JpaRepository<ManagerProfile, Long> {

    // Tìm hồ sơ quản lý theo mã người dùng.
    Optional<ManagerProfile> findByUserId(Long userId);

    // Lấy hồ sơ quản lý theo danh sách mã người dùng.
    List<ManagerProfile> findByUserIdIn(Collection<Long> userIds);

    @Modifying
    @Query("UPDATE ManagerProfile p SET p.lastSeenAt = :seenAt WHERE p.userId = :userId")
    // Cập nhật thời điểm hoạt động gần nhất của quản lý.
    int touchLastSeen(@Param("userId") Long userId, @Param("seenAt") LocalDateTime seenAt);

    @Modifying
    @Query("UPDATE ManagerProfile p SET p.availabilityStatus = :offline "
            + "WHERE p.availabilityStatus IN :active AND "
            + "(p.lastSeenAt IS NULL OR p.lastSeenAt < :cutoff)")
    // Chuyển quản lý không hoạt động sang trạng thái ngoại tuyến.
    int markStaleOffline(@Param("active") Collection<AvailabilityStatus> active,
            @Param("offline") AvailabilityStatus offline, @Param("cutoff") LocalDateTime cutoff);
}
