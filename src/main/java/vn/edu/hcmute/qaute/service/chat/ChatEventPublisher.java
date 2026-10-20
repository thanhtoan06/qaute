package vn.edu.hcmute.qaute.service.chat;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChatEventPublisher {
    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private final SimpMessagingTemplate messaging;

    public ChatEventPublisher(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

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
