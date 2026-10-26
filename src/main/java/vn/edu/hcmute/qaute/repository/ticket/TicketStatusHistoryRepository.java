package vn.edu.hcmute.qaute.repository.ticket;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.ticket.TicketStatusHistory;

// Khai báo truy vấn lưu trữ dữ liệu TicketStatusHistory.
public interface TicketStatusHistoryRepository extends JpaRepository<TicketStatusHistory, Long> {
    // Lấy lịch sử trạng thái của vé theo thời điểm tăng dần.
    List<TicketStatusHistory> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
