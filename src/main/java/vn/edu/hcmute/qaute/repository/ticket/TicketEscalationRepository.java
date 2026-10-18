package vn.edu.hcmute.qaute.repository.ticket;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.ticket.TicketEscalation;

public interface TicketEscalationRepository extends JpaRepository<TicketEscalation, Long> {
    List<TicketEscalation> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
