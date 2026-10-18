package vn.edu.hcmute.qaute.repository.ticket;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.common.constant.SenderType;
import vn.edu.hcmute.qaute.entity.ticket.TicketMessage;

public interface TicketMessageRepository extends JpaRepository<TicketMessage, Long> {
    List<TicketMessage> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
    boolean existsByTicketIdAndSenderType(Long ticketId, SenderType senderType);
}
