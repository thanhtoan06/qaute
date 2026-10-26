package vn.edu.hcmute.qaute.service.notification;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import vn.edu.hcmute.qaute.service.ticket.event.TicketDomainEvent;

// Xử lý nghiệp vụ TicketRealtimeListener trong hệ thống.
@Component
public class TicketRealtimeListener {

    private final ObjectProvider<SimpMessagingTemplate> templateProvider;

    // Khởi tạo bộ lắng nghe và nhận nhà cung cấp kênh gửi sự kiện.
    public TicketRealtimeListener(ObjectProvider<SimpMessagingTemplate> templateProvider) {
        this.templateProvider = templateProvider;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    // Phát sự kiện cập nhật yêu cầu đến kênh thời gian thực sau khi giao dịch hoàn tất.
    public void onTicketEvent(TicketDomainEvent event) {
        if (event == null || event.ticketId() == null) {
            return;
        }
        SimpMessagingTemplate template = templateProvider.getIfAvailable();
        if (template == null) {
            return;
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", event.type() != null ? event.type().name() : null);
        payload.put("ticketId", event.ticketId());
        payload.put("at", LocalDateTime.now());

        try {
            template.convertAndSend("/topic/ticket/" + event.ticketId(), payload);
        } catch (Exception ignored) {
        }
    }
}
