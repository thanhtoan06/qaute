package vn.edu.hcmute.qaute.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.security.event.SessionRevokedEvent;

@Component
public class WsSessionEventListener {
    private final WsSessionRegistry registry;

    public WsSessionEventListener(WsSessionRegistry registry) {
        this.registry = registry;
    }

    @EventListener
    public void onRevoked(SessionRevokedEvent event) {
        if (event.jti() == null) {
            registry.closeAllOf(event.userId());
        } else {
            registry.closeByJti(event.userId(), event.jti());
        }
    }
}
