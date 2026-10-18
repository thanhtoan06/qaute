package vn.edu.hcmute.qaute.repository.ticket;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.ticket.TicketStatusHistory;

public interface TicketStatusHistoryRepository extends JpaRepository<TicketStatusHistory, Long> {
    List<TicketStatusHistory> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
