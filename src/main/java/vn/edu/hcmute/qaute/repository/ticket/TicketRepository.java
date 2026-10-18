package vn.edu.hcmute.qaute.repository.ticket;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
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

    @Modifying
    @Query("UPDATE Ticket t SET t.status = vn.edu.hcmute.qaute.common.constant.TicketStatus.ASSIGNED, "
            + "t.currentAssigneeId = :managerId, t.firstAssignedAt = :now, t.lastActivityAt = :now, "
            + "t.version = t.version + 1 WHERE t.id = :id AND t.status = vn.edu.hcmute.qaute.common.constant.TicketStatus.NEW")
    int claimIfNew(@Param("id") Long id, @Param("managerId") Long managerId, @Param("now") LocalDateTime now);
}
