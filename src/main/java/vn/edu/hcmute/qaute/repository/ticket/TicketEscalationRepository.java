package vn.edu.hcmute.qaute.repository.ticket;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.ticket.TicketEscalation;

// Khai báo truy vấn lưu trữ dữ liệu TicketEscalation.
public interface TicketEscalationRepository extends JpaRepository<TicketEscalation, Long> {
    // Lấy lịch sử chuyển cấp của vé theo thời điểm tăng dần.
    List<TicketEscalation> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
