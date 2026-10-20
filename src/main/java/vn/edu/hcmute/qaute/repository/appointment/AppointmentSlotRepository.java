package vn.edu.hcmute.qaute.repository.appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.common.constant.SlotStatus;
import vn.edu.hcmute.qaute.entity.appointment.AppointmentSlot;

public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {
    Optional<AppointmentSlot> findByManagerIdAndStartAt(Long managerId, LocalDateTime startAt);
    List<AppointmentSlot> findByManagerIdAndStartAtBetweenOrderByStartAtAsc(
            Long managerId, LocalDateTime from, LocalDateTime to);
    List<AppointmentSlot> findByManagerIdInAndStatusAndStartAtGreaterThanEqualAndStartAtLessThanOrderByStartAtAsc(
            Collection<Long> managerIds, SlotStatus status, LocalDateTime from, LocalDateTime to);

    @Query("select s from AppointmentSlot s where s.managerId = :managerId "
            + "and s.startAt < :endAt and s.endAt > :startAt order by s.startAt")
    List<AppointmentSlot> findOverlapping(@Param("managerId") Long managerId,
            @Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt);

    long countByManagerIdAndStartAtGreaterThanAndStatus(Long managerId, LocalDateTime startAt, SlotStatus status);

    @Modifying
    @Query("update AppointmentSlot s set s.status = vn.edu.hcmute.qaute.common.constant.SlotStatus.BOOKED, "
            + "s.version = s.version + 1 where s.id = :id and s.status = "
            + "vn.edu.hcmute.qaute.common.constant.SlotStatus.AVAILABLE")
    int markBooked(@Param("id") Long id);

    @Modifying
    @Query("update AppointmentSlot s set s.status = vn.edu.hcmute.qaute.common.constant.SlotStatus.AVAILABLE, "
            + "s.version = s.version + 1 where s.id = :id and s.status = "
            + "vn.edu.hcmute.qaute.common.constant.SlotStatus.BOOKED and s.startAt > :now")
    int releaseBooked(@Param("id") Long id, @Param("now") LocalDateTime now);

    @Modifying
    @Query("update AppointmentSlot s set s.status = vn.edu.hcmute.qaute.common.constant.SlotStatus.EXPIRED, "
            + "s.version = s.version + 1 where s.startAt < :now and s.status = "
            + "vn.edu.hcmute.qaute.common.constant.SlotStatus.AVAILABLE")
    int expirePast(@Param("now") LocalDateTime now);
}
