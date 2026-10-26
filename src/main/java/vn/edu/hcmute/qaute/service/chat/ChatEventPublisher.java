package vn.edu.hcmute.qaute.service.chat;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

// Xử lý nghiệp vụ ChatEventPublisher trong hệ thống.
@Component
public class ChatEventPublisher {
    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private final SimpMessagingTemplate messaging;

    // Khởi tạo bộ phát sự kiện bằng kênh gửi thông điệp WebSocket.
    public ChatEventPublisher(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    // Gửi sự kiện đến chủ đề của phiên chat kèm mã phiên và thời điểm phát.
    public void sendToSession(Long sessionId, Map<String, Object> event) {
        if (sessionId == null || event == null || !(event.get("type") instanceof String type)
                || type.isBlank()) {
            throw new IllegalArgumentException("Sự kiện chat cần type và sessionId");
        }
        Map<String, Object> payload = new HashMap<>(event);
        payload.put("sessionId", sessionId);
        payload.put("at", OffsetDateTime.now(ZONE).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        messaging.convertAndSend("/topic/chat/" + sessionId, payload);
    }
}
