package vn.edu.hcmute.qaute.websocket;

import java.security.Principal;
import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import vn.edu.hcmute.qaute.security.QauteUserDetails;

@Component
public class WsHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler handler,
            Map<String, Object> attributes) {
        QauteUserDetails details = (QauteUserDetails) attributes.get("userDetails");
        return details == null ? null : new StompPrincipal(details.getId());
    }
}
