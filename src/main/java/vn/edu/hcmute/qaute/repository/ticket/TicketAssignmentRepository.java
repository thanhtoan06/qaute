package vn.edu.hcmute.qaute.repository.ticket;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.ticket.TicketAssignment;

public interface TicketAssignmentRepository extends JpaRepository<TicketAssignment, Long> {
    List<TicketAssignment> findByTicketIdOrderByAssignedAtAsc(Long ticketId);
    Optional<TicketAssignment> findFirstByTicketIdAndEndedAtIsNullOrderByIdDesc(Long ticketId);
}
