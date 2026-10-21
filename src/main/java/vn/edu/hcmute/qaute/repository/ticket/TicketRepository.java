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

public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    Optional<Ticket> findByCode(String code);
    Optional<Ticket> findBySubmitToken(String submitToken);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Ticket t WHERE t.id = :id")
    Optional<Ticket> lockById(@Param("id") Long id);
    long countByStudentIdAndUrgentRequestedTrueAndStatusIn(Long studentId, Collection<TicketStatus> statuses);
    long countByCurrentAssigneeIdAndStatusIn(Long assigneeId, Collection<TicketStatus> statuses);

    @Query("SELECT t FROM Ticket t WHERE t.studentId = :studentId AND t.id <> :ticketId "
            + "AND t.categoryId IN :categoryIds ORDER BY t.createdAt DESC, t.id DESC")
    List<Ticket> findOtherStudentTicketsInScope(@Param("studentId") Long studentId,
            @Param("ticketId") Long ticketId, @Param("categoryIds") Collection<Long> categoryIds,
            Pageable pageable);

    List<Ticket> findByStudentIdAndIdNotOrderByCreatedAtDescIdDesc(Long studentId, Long ticketId,
            Pageable pageable);
    List<Ticket> findByStudentIdAndCategoryIdInOrderByCreatedAtDescIdDesc(Long studentId,
            Collection<Long> categoryIds, Pageable pageable);

    @Query("SELECT DISTINCT t.categoryId FROM Ticket t WHERE t.currentAssigneeId = :managerId")
    List<Long> findAssignedCategoryIds(@Param("managerId") Long managerId);

    @Query("SELECT DISTINCT t.categoryId FROM Ticket t WHERE t.studentId = :studentId")
    List<Long> findDistinctCategoryIdsByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT t FROM Ticket t WHERE t.studentId = :studentId AND t.status IN :statuses ORDER BY t.lastActivityAt DESC, t.id DESC")
    List<Ticket> findByStudentIdAndStatusInOrderByLastActivityAtDesc(@Param("studentId") Long studentId,
            @Param("statuses") Collection<TicketStatus> statuses, Pageable pageable);

    @Query("SELECT t.createdAt, t.firstResponseAt FROM Ticket t WHERE t.firstResponseAt IS NOT NULL AND t.firstResponseAt >= :since")
    List<Object[]> findFirstResponseTimesSince(@Param("since") LocalDateTime since);

    @Modifying
    @Query("UPDATE Ticket t SET t.createdAt = :createdAt, t.updatedAt = :updatedAt WHERE t.id = :id")
    void updateTimestamps(@Param("id") Long id, @Param("createdAt") LocalDateTime createdAt, @Param("updatedAt") LocalDateTime updatedAt);

    @Modifying
    @Query("UPDATE Ticket t SET t.status = vn.edu.hcmute.qaute.common.constant.TicketStatus.ASSIGNED, "
            + "t.currentAssigneeId = :managerId, t.firstAssignedAt = :now, t.lastActivityAt = :now, "
            + "t.version = t.version + 1 WHERE t.id = :id AND t.status = vn.edu.hcmute.qaute.common.constant.TicketStatus.NEW")
    int claimIfNew(@Param("id") Long id, @Param("managerId") Long managerId, @Param("now") LocalDateTime now);

    @Query("SELECT t FROM Ticket t WHERE t.status = vn.edu.hcmute.qaute.common.constant.TicketStatus.RESOLVED AND t.resolvedAt < :cutoff")
    List<Ticket> findExpiredResolved(@Param("cutoff") LocalDateTime cutoff, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.status = vn.edu.hcmute.qaute.common.constant.TicketStatus.WAITING_STUDENT AND t.lastActivityAt < :cutoff")
    List<Ticket> findExpiredWaiting(@Param("cutoff") LocalDateTime cutoff, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.status = vn.edu.hcmute.qaute.common.constant.TicketStatus.WAITING_STUDENT AND t.lastActivityAt < :cutoff AND t.waitingReminderCount < :maxCount")
    List<Ticket> findWaitingNeedingReminder(@Param("cutoff") LocalDateTime cutoff, @Param("maxCount") int maxCount, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.status IN :statuses AND t.slaPausedAt IS NULL AND (t.slaFirstResponseDueAt IS NOT NULL OR t.slaResolutionDueAt IS NOT NULL)")
    List<Ticket> findActiveSlaTickets(@Param("statuses") Collection<TicketStatus> statuses, Pageable pageable);
}

