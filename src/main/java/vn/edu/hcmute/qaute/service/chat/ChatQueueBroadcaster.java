package vn.edu.hcmute.qaute.service.chat;

import java.util.Comparator;
import java.util.Map;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.entity.chat.ChatSession;
import vn.edu.hcmute.qaute.repository.chat.ChatSessionRepository;

@Component
public class ChatQueueBroadcaster {
    private final ChatAvailabilityService availability;
    private final ChatDeskService desk;
    private final ChatSessionRepository sessions;
    private final ChatEventPublisher events;
    private final SimpMessagingTemplate messaging;

    public ChatQueueBroadcaster(ChatAvailabilityService availability, ChatDeskService desk,
            ChatSessionRepository sessions, ChatEventPublisher events, SimpMessagingTemplate messaging) {
        this.availability = availability;
        this.desk = desk;
        this.sessions = sessions;
        this.events = events;
        this.messaging = messaging;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void changed(ChatQueueChangedEvent event) {
        for (Long managerId : availability.eligibleManagerIds(event.categoryId())) {
            messaging.convertAndSendToUser(String.valueOf(managerId), "/queue/chat-queue",
                    Map.of("type", "QUEUE_UPDATED", "waiting", desk.queueSnapshot(managerId)));
        }
        var waiting = sessions.findByStatusOrderByStartedAtAsc(ChatSessionStatus.WAITING).stream()
                .filter(session -> event.categoryId().equals(session.getCategoryId()))
                .sorted(Comparator.comparing(ChatSession::getStartedAt).thenComparing(ChatSession::getId))
                .toList();
        int position = 1;
        for (ChatSession session : waiting) {
            events.sendToSession(session.getId(), Map.of("type", "QUEUE_POSITION_CHANGED",
                    "queuePosition", position++));
        }
    }
}
