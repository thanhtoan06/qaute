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

// Hỗ trợ giao tiếp thời gian thực qua PresenceService.
@Component
public class PresenceService {
    private final Map<String, Long> connections = new ConcurrentHashMap<>();

    @EventListener
    // Ghi nhận phiên WebSocket vừa kết nối.
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
    // Xóa phiên WebSocket đã ngắt kết nối.
    public void disconnected(SessionDisconnectEvent event) {
        connections.remove(event.getSessionId());
    }

    // Kiểm tra người dùng có phiên đang hoạt động hay không.
    public boolean isOnline(Long userId) {
        return connections.containsValue(userId);
    }

    // Trả về tập người dùng đang trực tuyến.
    public Set<Long> onlineUserIds() {
        return connections.values().stream().collect(Collectors.toUnmodifiableSet());
    }

    // Đếm số phiên WebSocket đang hoạt động.
    public int connectionCount() {
        return connections.size();
    }
}
