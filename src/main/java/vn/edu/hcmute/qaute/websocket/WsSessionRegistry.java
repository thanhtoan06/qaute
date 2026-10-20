package vn.edu.hcmute.qaute.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WsSessionRegistry {
    public record Entry(WebSocketSession session, Long userId, String jti) {
    }

    private final Map<String, Entry> sessions = new ConcurrentHashMap<>();

    public void register(WebSocketSession session, Long userId, String jti) {
        sessions.put(session.getId(), new Entry(session, userId, jti));
    }

    public void unregister(String sessionId) {
        sessions.remove(sessionId);
    }

    public void closeAllOf(Long userId) {
        sessions.values().stream().filter(entry -> userId.equals(entry.userId()))
                .forEach(this::close);
    }

    public void closeByJti(Long userId, String jti) {
        sessions.values().stream().filter(entry -> userId.equals(entry.userId())
                && jti.equals(entry.jti())).forEach(this::close);
    }

    private void close(Entry entry) {
        try {
            if (entry.session().isOpen()) {
                entry.session().close(CloseStatus.POLICY_VIOLATION);
            }
        } catch (IOException ignored) {
            // The connection may have closed concurrently.
        } finally {
            unregister(entry.session().getId());
        }
    }
}
