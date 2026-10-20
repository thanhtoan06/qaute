package vn.edu.hcmute.qaute.websocket;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

class WsSessionRegistryTest {
    @Test
    void revokeSpecificJtiOnlyClosesMatchingSession() throws Exception {
        WsSessionRegistry registry = new WsSessionRegistry();
        WebSocketSession revoked = org.mockito.Mockito.mock(WebSocketSession.class);
        WebSocketSession retained = org.mockito.Mockito.mock(WebSocketSession.class);
        when(revoked.getId()).thenReturn("one");
        when(retained.getId()).thenReturn("two");
        when(revoked.isOpen()).thenReturn(true);
        registry.register(revoked, 7L, "jti-one");
        registry.register(retained, 7L, "jti-two");

        registry.closeByJti(7L, "jti-one");

        verify(revoked).close(CloseStatus.POLICY_VIOLATION);
        org.mockito.Mockito.verify(retained, org.mockito.Mockito.never()).close(org.mockito.ArgumentMatchers.any());
    }
}
