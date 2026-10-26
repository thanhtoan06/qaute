package vn.edu.hcmute.qaute.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

// Hỗ trợ giao tiếp thời gian thực qua WsSessionRegistry.
@Component
public class WsSessionRegistry {
    public record Entry(WebSocketSession session, Long userId, String jti) {
    }

    private final Map<String, Entry> sessions = new ConcurrentHashMap<>();

    // Ghi nhận phiên WebSocket theo mã phiên.
    public void register(WebSocketSession session, Long userId, String jti) {
        sessions.put(session.getId(), new Entry(session, userId, jti));
    }

    // Xóa phiên khỏi danh sách theo dõi.
    public void unregister(String sessionId) {
        sessions.remove(sessionId);
    }

    // Đóng tất cả phiên của người dùng.
    public void closeAllOf(Long userId) {
        sessions.values().stream().filter(entry -> userId.equals(entry.userId()))
                .forEach(this::close);
    }

    // Đóng các phiên của người dùng theo mã token.
    public void closeByJti(Long userId, String jti) {
        sessions.values().stream().filter(entry -> userId.equals(entry.userId())
                && jti.equals(entry.jti())).forEach(this::close);
    }

    // Đóng phiên và luôn xóa phiên khỏi danh sách theo dõi.
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
