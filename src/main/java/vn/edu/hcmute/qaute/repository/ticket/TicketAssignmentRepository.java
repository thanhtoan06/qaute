package vn.edu.hcmute.qaute.repository.ticket;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.ticket.TicketAssignment;

// Khai báo truy vấn lưu trữ dữ liệu TicketAssignment.
public interface TicketAssignmentRepository extends JpaRepository<TicketAssignment, Long> {
    // Lấy danh sách phân công của vé theo thời điểm tăng dần.
    List<TicketAssignment> findByTicketIdOrderByAssignedAtAsc(Long ticketId);

    // Tìm phân công hiện tại của vé.
    Optional<TicketAssignment> findFirstByTicketIdAndEndedAtIsNullOrderByIdDesc(Long ticketId);
}
