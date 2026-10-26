package vn.edu.hcmute.qaute.websocket;

import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.util.TokenUtil;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.RateLimitService;
import vn.edu.hcmute.qaute.service.chat.ChatAccessChecker;
import vn.edu.hcmute.qaute.service.ticket.TicketAccessService;

// Hỗ trợ giao tiếp thời gian thực qua StompChannelInterceptor.
@Component
public class StompChannelInterceptor implements ChannelInterceptor {
    private final ObjectProvider<ChatAccessChecker> chatAccess;
    private final TicketAccessService ticketAccess;
    private final RateLimitService rateLimits;

    // Khởi tạo bộ kiểm tra quyền và giới hạn tần suất.
    public StompChannelInterceptor(ObjectProvider<ChatAccessChecker> chatAccess,
            TicketAccessService ticketAccess, RateLimitService rateLimits) {
        this.chatAccess = chatAccess;
        this.ticketAccess = ticketAccess;
        this.rateLimits = rateLimits;
    }

    @Override
    // Kiểm tra thông điệp STOMP trước khi gửi vào kênh.
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        if (command != StompCommand.CONNECT && command != StompCommand.SUBSCRIBE
                && command != StompCommand.SEND) {
            return message;
        }
        Map<String, Object> attributes = accessor.getSessionAttributes();
        QauteUserDetails user = attributes == null ? null
                : (QauteUserDetails) attributes.get("userDetails");
        if (user == null || !user.isEnabled()) {
            throw new MessagingException("Phiên kết nối không hợp lệ");
        }
        if (command == StompCommand.CONNECT) {
            String cookieToken = (String) attributes.get("xsrf");
            String headerToken = accessor.getFirstNativeHeader("X-XSRF-TOKEN");
            if (cookieToken == null || cookieToken.isBlank() || headerToken == null
                    || !TokenUtil.constantTimeEquals(cookieToken, headerToken)) {
                throw new MessagingException("Phiên kết nối không hợp lệ");
            }
        } else if (command == StompCommand.SUBSCRIBE) {
            checkSubscribe(accessor.getDestination(), user);
        } else {
            String destination = accessor.getDestination();
            if (destination == null || !destination.startsWith("/app/chat.")) {
                throw new MessagingException("Đích gửi không hợp lệ");
            }
            int limit = "/app/chat.send".equals(destination) ? 20 : 120;
            if (!rateLimits.tryConsume("ws:" + user.getId() + ":" + destination,
                    limit, Duration.ofMinutes(1))) {
                throw new MessagingException("Bạn gửi quá nhanh");
            }
        }
        return message;
    }

    // Xác minh quyền đăng ký theo dõi đích đến.
    private void checkSubscribe(String destination, QauteUserDetails user) {
        if ("/user/queue/notifications".equals(destination)) {
            return;
        }
        if (("/user/queue/chat-queue".equals(destination) || "/user/queue/errors".equals(destination))
                && user.getRoleCode() == RoleCode.MANAGER) {
            return;
        }
        Long chatId = pathId(destination, "/topic/chat/");
        if (chatId != null) {
            ChatAccessChecker checker = chatAccess.getIfAvailable();
            if (checker != null && checker.isParticipant(chatId, user.getId())) {
                return;
            }
        }
        Long ticketId = pathId(destination, "/topic/ticket/");
        if (ticketId != null && ticketAccess.canView(ticketId, user.getId(), user.getRoleCode())) {
            return;
        }
        throw new MessagingException("Bạn không có quyền theo dõi kênh này");
    }

    // Đọc mã định danh số từ đường dẫn đích đến.
    private Long pathId(String destination, String prefix) {
        if (destination == null || !destination.startsWith(prefix)) {
            return null;
        }
        String value = destination.substring(prefix.length());
        if (!value.matches("[1-9][0-9]*")) {
            return null;
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
