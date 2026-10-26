package vn.edu.hcmute.qaute.repository.ticket;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.ticket.TicketReview;

// Khai báo truy vấn lưu trữ dữ liệu TicketReview.
public interface TicketReviewRepository extends JpaRepository<TicketReview, Long> {
    // Tìm đánh giá của vé.
    Optional<TicketReview> findByTicketId(Long ticketId);
}
