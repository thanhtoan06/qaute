package vn.edu.hcmute.qaute.repository.ticket;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.common.constant.SenderType;
import vn.edu.hcmute.qaute.entity.ticket.TicketMessage;

// Khai báo truy vấn lưu trữ dữ liệu TicketMessage.
public interface TicketMessageRepository extends JpaRepository<TicketMessage, Long> {
    // Lấy danh sách tin nhắn của vé theo thời điểm tăng dần.
    List<TicketMessage> findByTicketIdOrderByCreatedAtAsc(Long ticketId);

    // Kiểm tra vé đã có tin nhắn từ loại người gửi chỉ định chưa.
    boolean existsByTicketIdAndSenderType(Long ticketId, SenderType senderType);
}
