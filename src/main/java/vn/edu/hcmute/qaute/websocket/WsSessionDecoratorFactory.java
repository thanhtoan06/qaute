package vn.edu.hcmute.qaute.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import vn.edu.hcmute.qaute.security.QauteUserDetails;

@Component
public class WsSessionDecoratorFactory implements WebSocketHandlerDecoratorFactory {
    private final WsSessionRegistry registry;

    public WsSessionDecoratorFactory(WsSessionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                QauteUserDetails user = (QauteUserDetails) session.getAttributes().get("userDetails");
                if (user != null) {
                    registry.register(session, user.getId(), user.getSessionJti());
                }
                try {
                    super.afterConnectionEstablished(session);
                } catch (Exception exception) {
                    registry.unregister(session.getId());
                    throw exception;
                }
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                registry.unregister(session.getId());
                super.afterConnectionClosed(session, closeStatus);
            }
        };
    }
}
