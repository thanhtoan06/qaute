package vn.edu.hcmute.qaute.repository.ticket;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
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
    long countByStudentIdAndUrgentRequestedTrueAndStatusIn(Long studentId, Collection<TicketStatus> statuses);
    long countByCurrentAssigneeIdAndStatusIn(Long assigneeId, Collection<TicketStatus> statuses);

    @Query("SELECT t FROM Ticket t WHERE t.studentId = :studentId AND t.id <> :ticketId "
            + "AND t.categoryId IN :categoryIds ORDER BY t.createdAt DESC, t.id DESC")
    List<Ticket> findOtherStudentTicketsInScope(@Param("studentId") Long studentId,
            @Param("ticketId") Long ticketId, @Param("categoryIds") Collection<Long> categoryIds,
            Pageable pageable);

    @Query("SELECT DISTINCT t.categoryId FROM Ticket t WHERE t.currentAssigneeId = :managerId")
    List<Long> findAssignedCategoryIds(@Param("managerId") Long managerId);

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

