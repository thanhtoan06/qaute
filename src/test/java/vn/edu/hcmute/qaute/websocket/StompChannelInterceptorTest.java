package vn.edu.hcmute.qaute.websocket;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.RateLimitService;
import vn.edu.hcmute.qaute.service.chat.ChatAccessChecker;
import vn.edu.hcmute.qaute.service.ticket.TicketAccessService;

class StompChannelInterceptorTest {
    @SuppressWarnings("unchecked")
    private final ObjectProvider<ChatAccessChecker> chats = mock(ObjectProvider.class);
    private final TicketAccessService tickets = mock(TicketAccessService.class);
    private final RateLimitService rates = mock(RateLimitService.class);
    private final StompChannelInterceptor interceptor = new StompChannelInterceptor(chats, tickets, rates);
    private final QauteUserDetails student = new QauteUserDetails(5L, "s@example.edu", "SV",
            RoleCode.STUDENT, "jti", true);

    @Test
    void connectRequiresMatchingCookieCsrfToken() {
        assertThrows(MessagingException.class, () -> interceptor.preSend(message(StompCommand.CONNECT,
                null, null), null));
        assertThrows(MessagingException.class, () -> interceptor.preSend(message(StompCommand.CONNECT,
                null, "wrong"), null));
        Message<?> allowed = message(StompCommand.CONNECT, null, "secret");
        assertSame(allowed, interceptor.preSend(allowed, null));
    }

    @Test
    void subscribeRejectsUnknownAndUnownedChannels() {
        assertThrows(MessagingException.class, () -> interceptor.preSend(message(StompCommand.SUBSCRIBE,
                "/topic/random", null), null));
        assertThrows(MessagingException.class, () -> interceptor.preSend(message(StompCommand.SUBSCRIBE,
                "/topic/chat/4", null), null));
        assertThrows(MessagingException.class, () -> interceptor.preSend(message(StompCommand.SUBSCRIBE,
                "/user/queue/chat-queue", null), null));
        Message<?> allowed = message(StompCommand.SUBSCRIBE, "/user/queue/notifications", null);
        assertSame(allowed, interceptor.preSend(allowed, null));
    }

    @Test
    void subscribeChecksTicketAccess() {
        when(tickets.canView(10L, 5L, RoleCode.STUDENT)).thenReturn(true);
        Message<?> allowed = message(StompCommand.SUBSCRIBE, "/topic/ticket/10", null);
        assertSame(allowed, interceptor.preSend(allowed, null));
        assertThrows(MessagingException.class, () -> interceptor.preSend(message(StompCommand.SUBSCRIBE,
                "/topic/ticket/11", null), null));
    }

    @Test
    void sendRequiresChatDestinationAndEnforcesRateLimit() {
        assertThrows(MessagingException.class, () -> interceptor.preSend(message(StompCommand.SEND,
                "/topic/ticket/10", null), null));
        Message<?> send = message(StompCommand.SEND, "/app/chat.send", null);
        assertThrows(MessagingException.class, () -> interceptor.preSend(send, null));
        when(rates.tryConsume("ws:5:/app/chat.send", 20, Duration.ofMinutes(1))).thenReturn(true);
        assertSame(send, interceptor.preSend(send, null));
    }

    private Message<?> message(StompCommand command, String destination, String csrf) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(command);
        accessor.setSessionId("session-1");
        accessor.setSessionAttributes(Map.of("userDetails", student, "xsrf", "secret"));
        if (destination != null) accessor.setDestination(destination);
        if (csrf != null) accessor.setNativeHeader("X-XSRF-TOKEN", csrf);
        return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
    }
}
