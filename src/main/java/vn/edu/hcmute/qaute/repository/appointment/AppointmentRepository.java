package vn.edu.hcmute.qaute.repository.appointment;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.entity.appointment.Appointment;

// Khai báo truy vấn lưu trữ dữ liệu Appointment.
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    // Kiểm tra sinh viên đã đặt lịch theo chủ đề hay chưa.
    boolean existsByStudentIdAndTopic(Long studentId, String topic);

    // Tìm lịch hẹn theo các ca và trạng thái.
    List<Appointment> findBySlotIdInAndStatusIn(Collection<Long> slotIds, Collection<AppointmentStatus> statuses);

    // Tìm lịch hẹn thuộc các ca được chỉ định.
    List<Appointment> findBySlotIdIn(Collection<Long> slotIds);

    // Lấy lịch hẹn đã xác nhận của quản lý trong khoảng thời gian.
    @Query(value = "select a from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.managerId = :managerId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
            + "and s.startAt >= :from and s.startAt < :to order by s.startAt asc, a.id asc",
            countQuery = "select count(a) from Appointment a, AppointmentSlot s where a.slotId = s.id "
                    + "and a.managerId = :managerId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
                    + "and s.startAt >= :from and s.startAt < :to")
    Page<Appointment> findManagerToday(@Param("managerId") Long managerId,
            @Param("from") LocalDateTime from, @Param("to") LocalDateTime to, Pageable pageable);

    // Lấy lịch hẹn đã xác nhận sắp tới của quản lý.
    @Query(value = "select a from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.managerId = :managerId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
            + "and s.startAt >= :from order by s.startAt asc, a.id asc",
            countQuery = "select count(a) from Appointment a, AppointmentSlot s where a.slotId = s.id "
                    + "and a.managerId = :managerId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
                    + "and s.startAt >= :from")
    Page<Appointment> findManagerUpcoming(@Param("managerId") Long managerId,
            @Param("from") LocalDateTime from, Pageable pageable);

    // Lấy lịch hẹn trước đây của quản lý.
    @Query(value = "select a from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.managerId = :managerId and (a.status <> vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
            + "or s.startAt < :today) order by s.startAt desc, a.id desc",
            countQuery = "select count(a) from Appointment a, AppointmentSlot s where a.slotId = s.id "
                    + "and a.managerId = :managerId and (a.status <> vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
                    + "or s.startAt < :today)")
    Page<Appointment> findManagerPast(@Param("managerId") Long managerId,
            @Param("today") LocalDateTime today, Pageable pageable);

    // Đếm lịch hẹn sắp tới đã xác nhận của sinh viên.
    @Query("select count(a) from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.studentId = :studentId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
            + "and s.startAt >= :now")
    long countUpcoming(@Param("studentId") Long studentId, @Param("now") LocalDateTime now);

    // Đếm lịch hẹn đã xác nhận trùng với khoảng thời gian.
    @Query("select count(a) from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.studentId = :studentId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
            + "and (:excludeId is null or a.id <> :excludeId) and s.startAt < :endAt and s.endAt > :startAt")
    long countOverlapping(@Param("studentId") Long studentId, @Param("excludeId") Long excludeId,
            @Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt);

    // Lấy lịch hẹn sắp tới đã xác nhận của sinh viên.
    @Query(value = "select a from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.studentId = :studentId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
            + "and s.startAt >= :now order by s.startAt asc, a.id asc",
            countQuery = "select count(a) from Appointment a, AppointmentSlot s where a.slotId = s.id "
                    + "and a.studentId = :studentId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
                    + "and s.startAt >= :now")
    Page<Appointment> findUpcoming(@Param("studentId") Long studentId, @Param("now") LocalDateTime now,
            Pageable pageable);

    // Lấy lịch hẹn đã hoàn thành, vắng mặt hoặc quá giờ của sinh viên.
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

    // Lấy các lịch hẹn đã bị hủy của sinh viên.
    @Query(value = "select a from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.studentId = :studentId and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CANCELLED "
            + "order by s.startAt desc, a.id desc",
            countQuery = "select count(a) from Appointment a where a.studentId = :studentId "
                    + "and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CANCELLED")
    Page<Appointment> findCancelled(@Param("studentId") Long studentId, Pageable pageable);

    // Tìm lịch hẹn cần gửi nhắc nhở trước 24 giờ.
    @Query("select a from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
            + "and s.startAt > :now and s.startAt <= :until and a.reminder24hSent = false order by s.startAt")
    List<Appointment> findDue24h(@Param("now") LocalDateTime now, @Param("until") LocalDateTime until,
            Pageable pageable);

    // Tìm lịch hẹn cần gửi nhắc nhở trước 1 giờ.
    @Query("select a from Appointment a, AppointmentSlot s where a.slotId = s.id "
            + "and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED "
            + "and s.startAt > :now and s.startAt <= :until and a.reminder1hSent = false order by s.startAt")
    List<Appointment> findDue1h(@Param("now") LocalDateTime now, @Param("until") LocalDateTime until,
            Pageable pageable);

    // Đánh dấu lịch hẹn đã gửi nhắc nhở trước 24 giờ.
    @Modifying
    @Query("update Appointment a set a.reminder24hSent = true, a.version = a.version + 1 "
            + "where a.id = :id and a.reminder24hSent = false "
            + "and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED")
    int markReminder24h(@Param("id") Long id);

    // Đánh dấu lịch hẹn đã gửi nhắc nhở trước 1 giờ.
    @Modifying
    @Query("update Appointment a set a.reminder1hSent = true, a.version = a.version + 1 "
            + "where a.id = :id and a.reminder1hSent = false "
            + "and a.status = vn.edu.hcmute.qaute.common.constant.AppointmentStatus.CONFIRMED")
    int markReminder1h(@Param("id") Long id);
}
