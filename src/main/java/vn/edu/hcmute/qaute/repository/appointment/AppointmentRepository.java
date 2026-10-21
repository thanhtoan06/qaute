package vn.edu.hcmute.qaute.repository.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Collection;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.entity.appointment.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    boolean existsByStudentIdAndTopic(Long studentId, String topic);

    List<Appointment> findBySlotIdInAndStatusIn(Collection<Long> slotIds, Collection<AppointmentStatus> statuses);
    List<Appointment> findBySlotIdIn(Collection<Long> slotIds);

    @Query(value = "select a from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.managerId = :managerId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
            + "and s.startAt >= :from and s.startAt < :to order by s.startAt asc, a.id asc",
            countQuery = "select count(a) from Appointment a, AppointmentSlot s where a.slotId = s.id "
                    + "and a.managerId = :managerId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
                    + "and s.startAt >= :from and s.startAt < :to")
    Page<Appointment> findManagerToday(@Param("managerId") Long managerId,
            @Param("from") LocalDateTime from, @Param("to") LocalDateTime to, Pageable pageable);

    @Query(value = "select a from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.managerId = :managerId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
            + "and s.startAt >= :from order by s.startAt asc, a.id asc",
            countQuery = "select count(a) from Appointment a, AppointmentSlot s where a.slotId = s.id "
                    + "and a.managerId = :managerId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
                    + "and s.startAt >= :from")
    Page<Appointment> findManagerUpcoming(@Param("managerId") Long managerId,
            @Param("from") LocalDateTime from, Pageable pageable);

    @Query(value = "select a from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.managerId = :managerId and (a.status <> vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
            + "or s.startAt < :today) order by s.startAt desc, a.id desc",
            countQuery = "select count(a) from Appointment a, AppointmentSlot s where a.slotId = s.id "
                    + "and a.managerId = :managerId and (a.status <> vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
                    + "or s.startAt < :today)")
    Page<Appointment> findManagerPast(@Param("managerId") Long managerId,
            @Param("today") LocalDateTime today, Pageable pageable);

    @Query("select count(a) from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.studentId = :studentId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
            + "and s.startAt >= :now")
    long countUpcoming(@Param("studentId") Long studentId, @Param("now") LocalDateTime now);

    @Query("select count(a) from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.studentId = :studentId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
            + "and (:excludeId is null or a.id <> :excludeId) and s.startAt < :endAt and s.endAt > :startAt")
    long countOverlapping(@Param("studentId") Long studentId, @Param("excludeId") Long excludeId,
            @Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt);

    @Query(value = "select a from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.studentId = :studentId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
            + "and s.startAt >= :now order by s.startAt asc, a.id asc",
            countQuery = "select count(a) from Appointment a, AppointmentSlot s where a.slotId = s.id "
                    + "and a.studentId = :studentId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
                    + "and s.startAt >= :now")
    Page<Appointment> findUpcoming(@Param("studentId") Long studentId, @Param("now") LocalDateTime now,
            Pageable pageable);

    @Query(value = "select a from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.studentId = :studentId and (a.status in "
            + "(vn.edu.hcmute.qaute.common.constant.AppointmentStatus.COMPLETED, "
            + "vn.edu.hcmute.qaute.common.constant.AppointmentStatus.NO_SHOW) or "
            + "(a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED and s.startAt < :now)) "
            + "order by s.startAt desc, a.id desc",
            countQuery = "select count(a) from Appointment a, AppointmentSlot s where a.slotId = s.id "
                    + "and a.studentId = :studentId and (a.status in "
                    + "(vn.edu.hcmute.qaute.common.constant.AppointmentStatus.COMPLETED, "
                    + "vn.edu.hcmute.qaute.common.constant.AppointmentStatus.NO_SHOW) or "
                    + "(a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED and s.startAt < :now))")
    Page<Appointment> findPast(@Param("studentId") Long studentId, @Param("now") LocalDateTime now,
            Pageable pageable);

    @Query(value = "select a from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.studentId = :studentId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CANCELLED "
            + "order by s.startAt desc, a.id desc",
            countQuery = "select count(a) from Appointment a where a.studentId = :studentId "
                    + "and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CANCELLED")
    Page<Appointment> findCancelled(@Param("studentId") Long studentId, Pageable pageable);

    @Query("select a from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
            + "and s.startAt > :now and s.startAt <= :until and a.reminder24hSent = false order by s.startAt")
    List<Appointment> findDue24h(@Param("now") LocalDateTime now, @Param("until") LocalDateTime until,
            Pageable pageable);

    @Query("select a from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
            + "and s.startAt > :now and s.startAt <= :until and a.reminder1hSent = false order by s.startAt")
    List<Appointment> findDue1h(@Param("now") LocalDateTime now, @Param("until") LocalDateTime until,
            Pageable pageable);

    @Modifying
    @Query("update Appointment a set a.reminder24hSent = true, a.version = a.version + 1 "
            + "where a.id = :id and a.reminder24hSent = false "
            + "and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED")
    int markReminder24h(@Param("id") Long id);

    @Modifying
    @Query("update Appointment a set a.reminder1hSent = true, a.version = a.version + 1 "
            + "where a.id = :id and a.reminder1hSent = false "
            + "and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED")
    int markReminder1h(@Param("id") Long id);
}
