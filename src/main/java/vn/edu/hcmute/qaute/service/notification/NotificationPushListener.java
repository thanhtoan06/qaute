package vn.edu.hcmute.qaute.service.notification;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificationPushListener {

    private final ObjectProvider<SimpMessagingTemplate> templateProvider;

    public NotificationPushListener(ObjectProvider<SimpMessagingTemplate> templateProvider) {
        this.templateProvider = templateProvider;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onNotificationCreated(NotificationCreatedEvent event) {
        if (event == null || event.userId() == null) {
            return;
        }
        SimpMessagingTemplate template = templateProvider.getIfAvailable();
        if (template == null) {
            return;
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", event.id());
        payload.put("type", event.type() != null ? event.type().name() : null);
        payload.put("title", event.title());
        payload.put("body", event.body());
        payload.put("linkUrl", event.linkUrl());
        payload.put("createdAt", event.createdAt());

        try {
            template.convertAndSendToUser(String.valueOf(event.userId()), "/queue/notifications", payload);
        } catch (Exception ignored) {
        }
    }
}

