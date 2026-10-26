package vn.edu.hcmute.qaute.repository.appointment;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.common.constant.SlotStatus;
import vn.edu.hcmute.qaute.entity.appointment.AppointmentSlot;

// Khai báo truy vấn lưu trữ dữ liệu AppointmentSlot.
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {
    // Tìm ca của quản lý theo thời điểm bắt đầu.
    Optional<AppointmentSlot> findByManagerIdAndStartAt(Long managerId, LocalDateTime startAt);

    // Tìm các ca của quản lý trong khoảng thời gian.
    List<AppointmentSlot> findByManagerIdAndStartAtBetweenOrderByStartAtAsc(
            Long managerId, LocalDateTime from, LocalDateTime to);

    // Tìm các ca của những quản lý theo trạng thái và thời gian.
    List<AppointmentSlot> findByManagerIdInAndStatusAndStartAtGreaterThanEqualAndStartAtLessThanOrderByStartAtAsc(
            Collection<Long> managerIds, SlotStatus status, LocalDateTime from, LocalDateTime to);

    // Tìm các ca giao với khoảng thời gian của quản lý.
    @Query("select s from AppointmentSlot s where s.managerId = :managerId "
            + "and s.startAt < :endAt and s.endAt > :startAt order by s.startAt")
    List<AppointmentSlot> findOverlapping(@Param("managerId") Long managerId,
            @Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt);

    // Đếm các ca bắt đầu sau thời điểm chỉ định theo trạng thái.
    long countByManagerIdAndStartAtGreaterThanAndStatus(Long managerId, LocalDateTime startAt, SlotStatus status);

    // Đánh dấu ca là đã được đặt.
    @Modifying
    @Query("update AppointmentSlot s set s.status = vn.edu.hcmute.qaute.common.constant.SlotStatus.BOOKED, "
            + "s.version = s.version + 1 where s.id = :id and s.status = "
            + "vn.edu.hcmute.qaute.common.constant.SlotStatus.AVAILABLE")
    int markBooked(@Param("id") Long id);

    // Giải phóng ca đã đặt nếu ca chưa bắt đầu.
    @Modifying
    @Query("update AppointmentSlot s set s.status = vn.edu.hcmute.qaute.common.constant.SlotStatus.AVAILABLE, "
            + "s.version = s.version + 1 where s.id = :id and s.status = "
            + "vn.edu.hcmute.qaute.common.constant.SlotStatus.BOOKED and s.startAt > :now")
    int releaseBooked(@Param("id") Long id, @Param("now") LocalDateTime now);

    // Đánh dấu các ca trống đã qua là hết hạn.
    @Modifying
    @Query("update AppointmentSlot s set s.status = vn.edu.hcmute.qaute.common.constant.SlotStatus.EXPIRED, "
            + "s.version = s.version + 1 where s.startAt < :now and s.status = "
            + "vn.edu.hcmute.qaute.common.constant.SlotStatus.AVAILABLE")
    int expirePast(@Param("now") LocalDateTime now);
}
