package vn.edu.hcmute.qaute.websocket;

import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class PresenceService {
    private final Map<String, Long> connections = new ConcurrentHashMap<>();

    @EventListener
    public void connected(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = accessor.getUser();
        if (principal == null || accessor.getSessionId() == null) {
            return;
        }
        try {
            connections.put(accessor.getSessionId(), Long.valueOf(principal.getName()));
        } catch (NumberFormatException ignored) {
            // Only numeric QAUTE principals are tracked.
        }
    }

    @EventListener
    public void disconnected(SessionDisconnectEvent event) {
        connections.remove(event.getSessionId());
    }

    public boolean isOnline(Long userId) {
        return connections.containsValue(userId);
    }

    public Set<Long> onlineUserIds() {
        return connections.values().stream().collect(Collectors.toUnmodifiableSet());
    }

    public int connectionCount() {
        return connections.size();
    }
}
