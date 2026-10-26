package vn.edu.hcmute.qaute.repository.ticket;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;

// Khai báo truy vấn lưu trữ dữ liệu Ticket.
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    // Tìm vé theo mã vé.
    Optional<Ticket> findByCode(String code);

    // Tìm vé theo mã gửi yêu cầu.
    Optional<Ticket> findBySubmitToken(String submitToken);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Ticket t WHERE t.id = :id")
    // Tìm và khóa vé theo mã định danh.
    Optional<Ticket> lockById(@Param("id") Long id);

    // Đếm vé khẩn cấp của sinh viên theo các trạng thái.
    long countByStudentIdAndUrgentRequestedTrueAndStatusIn(Long studentId, Collection<TicketStatus> statuses);

    // Đếm vé được giao cho người xử lý theo các trạng thái.
    long countByCurrentAssigneeIdAndStatusIn(Long assigneeId, Collection<TicketStatus> statuses);

    @Query("SELECT t FROM Ticket t WHERE t.studentId = :studentId AND t.id <> :ticketId "
            + "AND t.categoryId IN :categoryIds ORDER BY t.createdAt DESC, t.id DESC")
    // Lấy các vé khác của sinh viên trong các danh mục chỉ định.
    List<Ticket> findOtherStudentTicketsInScope(@Param("studentId") Long studentId,
            @Param("ticketId") Long ticketId, @Param("categoryIds") Collection<Long> categoryIds,
            Pageable pageable);

    // Lấy các vé khác của sinh viên theo thời điểm tạo mới nhất.
    List<Ticket> findByStudentIdAndIdNotOrderByCreatedAtDescIdDesc(Long studentId, Long ticketId,
            Pageable pageable);

    // Lấy vé của sinh viên trong các danh mục theo thời điểm tạo mới nhất.
    List<Ticket> findByStudentIdAndCategoryIdInOrderByCreatedAtDescIdDesc(Long studentId,
            Collection<Long> categoryIds, Pageable pageable);

    @Query("SELECT DISTINCT t.categoryId FROM Ticket t WHERE t.currentAssigneeId = :managerId")
    // Lấy các danh mục đang được giao cho người quản lý.
    List<Long> findAssignedCategoryIds(@Param("managerId") Long managerId);

    @Query("SELECT DISTINCT t.categoryId FROM Ticket t WHERE t.studentId = :studentId")
    // Lấy các danh mục khác nhau của sinh viên.
    List<Long> findDistinctCategoryIdsByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT t FROM Ticket t WHERE t.studentId = :studentId AND t.status IN :statuses ORDER BY t.lastActivityAt DESC, t.id DESC")
    // Lấy vé của sinh viên theo trạng thái và hoạt động gần nhất.
    List<Ticket> findByStudentIdAndStatusInOrderByLastActivityAtDesc(@Param("studentId") Long studentId,
            @Param("statuses") Collection<TicketStatus> statuses, Pageable pageable);

    @Query("SELECT t.createdAt, t.firstResponseAt FROM Ticket t WHERE t.firstResponseAt IS NOT NULL AND t.firstResponseAt >= :since")
    // Lấy thời điểm tạo và phản hồi đầu tiên từ mốc chỉ định.
    List<Object[]> findFirstResponseTimesSince(@Param("since") LocalDateTime since);

    @Modifying
    @Query("UPDATE Ticket t SET t.createdAt = :createdAt, t.updatedAt = :updatedAt WHERE t.id = :id")
    // Cập nhật thời điểm tạo và cập nhật của vé.
    void updateTimestamps(@Param("id") Long id, @Param("createdAt") LocalDateTime createdAt, @Param("updatedAt") LocalDateTime updatedAt);

    @Modifying
    @Query("UPDATE Ticket t SET t.status = vn.edu.hcmute.qaute.common.constant.TicketStatus.ASSIGNED, "
            + "t.currentAssigneeId = :managerId, t.firstAssignedAt = :now, t.lastActivityAt = :now, "
            + "t.version = t.version + 1 WHERE t.id = :id "
            + "AND t.status = vn.edu.hcmute.qaute.common.constant.TicketStatus.NEW "
            + "AND t.currentAssigneeId IS NULL")
    // Gán vé mới cho người quản lý nếu vé vẫn ở trạng thái mới.
    int claimIfNew(@Param("id") Long id, @Param("managerId") Long managerId, @Param("now") LocalDateTime now);

    @Query("SELECT t FROM Ticket t WHERE t.status = vn.edu.hcmute.qaute.common.constant.TicketStatus.RESOLVED AND t.resolvedAt < :cutoff")
    // Lấy các vé đã giải quyết trước thời điểm giới hạn.
    List<Ticket> findExpiredResolved(@Param("cutoff") LocalDateTime cutoff, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.status = vn.edu.hcmute.qaute.common.constant.TicketStatus.WAITING_STUDENT AND t.lastActivityAt < :cutoff")
    // Lấy các vé chờ sinh viên đã quá thời hạn.
    List<Ticket> findExpiredWaiting(@Param("cutoff") LocalDateTime cutoff, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.status = vn.edu.hcmute.qaute.common.constant.TicketStatus.WAITING_STUDENT AND t.lastActivityAt < :cutoff AND t.waitingReminderCount < :maxCount")
    // Lấy các vé chờ cần gửi nhắc nhở.
    List<Ticket> findWaitingNeedingReminder(@Param("cutoff") LocalDateTime cutoff, @Param("maxCount") int maxCount, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.status IN :statuses AND t.slaPausedAt IS NULL AND (t.slaFirstResponseDueAt IS NOT NULL OR t.slaResolutionDueAt IS NOT NULL)")
    // Lấy các vé đang được theo dõi SLA.
    List<Ticket> findActiveSlaTickets(@Param("statuses") Collection<TicketStatus> statuses, Pageable pageable);
}
