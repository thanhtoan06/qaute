package vn.edu.hcmute.qaute.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.security.event.SessionRevokedEvent;

// Hỗ trợ giao tiếp thời gian thực qua WsSessionEventListener.
@Component
public class WsSessionEventListener {
    private final WsSessionRegistry registry;

    // Khởi tạo bộ đăng ký phiên.
    public WsSessionEventListener(WsSessionRegistry registry) {
        this.registry = registry;
    }

    @EventListener
    // Đóng các phiên tương ứng khi phiên đăng nhập bị thu hồi.
    public void onRevoked(SessionRevokedEvent event) {
        if (event.jti() == null) {
            registry.closeAllOf(event.userId());
        } else {
            registry.closeByJti(event.userId(), event.jti());
        }
    }
}
