package vn.edu.hcmute.qaute.repository.ticket;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.ticket.TicketReview;

public interface TicketReviewRepository extends JpaRepository<TicketReview, Long> {
    Optional<TicketReview> findByTicketId(Long ticketId);
}
