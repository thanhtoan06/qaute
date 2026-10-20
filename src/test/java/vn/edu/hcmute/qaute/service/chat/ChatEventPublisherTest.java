package vn.edu.hcmute.qaute.service.chat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.time.OffsetDateTime;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

class ChatEventPublisherTest {
    @Test
    void everySessionEventContainsContractFields() {
        SimpMessagingTemplate template = org.mockito.Mockito.mock(SimpMessagingTemplate.class);
        ChatEventPublisher publisher = new ChatEventPublisher(template);
        publisher.sendToSession(8L, Map.of("type", "SESSION_ENDED"));

        ArgumentCaptor<Object> payload = ArgumentCaptor.forClass(Object.class);
        verify(template).convertAndSend(eq("/topic/chat/8"), payload.capture());
        @SuppressWarnings("unchecked")
        Map<String, Object> event = (Map<String, Object>) payload.getValue();
        assertEquals("SESSION_ENDED", event.get("type"));
        assertEquals(8L, event.get("sessionId"));
        assertNotNull(OffsetDateTime.parse((String) event.get("at")));
    }
}
