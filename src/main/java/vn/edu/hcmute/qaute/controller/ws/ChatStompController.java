package vn.edu.hcmute.qaute.controller.ws;

import java.security.Principal;
import java.util.Map;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.dto.request.chat.ChatReadPayload;
import vn.edu.hcmute.qaute.dto.request.chat.ChatSendPayload;
import vn.edu.hcmute.qaute.dto.request.chat.ChatSessionPayload;
import vn.edu.hcmute.qaute.dto.request.chat.ChatTransferPayload;
import vn.edu.hcmute.qaute.dto.request.chat.ChatTypingPayload;
import vn.edu.hcmute.qaute.service.chat.ChatAccessChecker;
import vn.edu.hcmute.qaute.service.chat.ChatDeskService;
import vn.edu.hcmute.qaute.service.chat.ChatEventPublisher;
import vn.edu.hcmute.qaute.service.chat.ChatMessagingService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho ChatStomp.
@Controller
public class ChatStompController {
    private final ChatMessagingService messaging;
    private final ChatDeskService desk;
    private final ChatAccessChecker access;
    private final ChatEventPublisher publisher;
    private final SimpMessagingTemplate socket;

    // Khởi tạo bộ điều khiển tin nhắn chat qua WebSocket.
    public ChatStompController(ChatMessagingService messaging, ChatDeskService desk,
            ChatAccessChecker access, ChatEventPublisher publisher, SimpMessagingTemplate socket) {
        this.messaging = messaging;
        this.desk = desk;
        this.access = access;
        this.publisher = publisher;
        this.socket = socket;
    }

    // Gửi tin nhắn chat từ người dùng kết nối.
    @MessageMapping("chat.send")
    public void send(ChatSendPayload payload, Principal principal) {
        handle(payload == null ? null : payload.sessionId(), principal, userId -> messaging.send(
                payload.sessionId(), userId, payload.clientMessageId(), payload.content(), payload.assetId()));
    }

    // Cập nhật trạng thái đang nhập của phiên chat.
    @MessageMapping("chat.typing")
    public void typing(ChatTypingPayload payload, Principal principal) {
        handle(payload == null ? null : payload.sessionId(), principal, userId -> {
            if (payload.isTyping() == null) throw new BadRequestException("Thiếu trạng thái đang nhập");
            messaging.typing(payload.sessionId(), userId, payload.isTyping());
        });
    }

    // Cập nhật mốc tin nhắn đã đọc của phiên chat.
    @MessageMapping("chat.read")
    public void read(ChatReadPayload payload, Principal principal) {
        handle(payload == null ? null : payload.sessionId(), principal,
                userId -> messaging.read(payload.sessionId(), userId, payload.lastReadMessageId()));
    }

    // Kết thúc phiên chat.
    @MessageMapping("chat.end")
    public void end(ChatSessionPayload payload, Principal principal) {
        handle(payload == null ? null : payload.sessionId(), principal,
                userId -> messaging.end(payload.sessionId(), userId));
    }

    // Chấp nhận phiên chat đang chờ.
    @MessageMapping("chat.accept")
    public void accept(ChatSessionPayload payload, Principal principal) {
        handle(payload == null ? null : payload.sessionId(), principal,
                userId -> desk.accept(payload.sessionId(), userId));
    }

    // Chuyển phiên chat cho quản lý được chỉ định.
    @MessageMapping("chat.transfer")
    public void transfer(ChatTransferPayload payload, Principal principal) {
        handle(payload == null ? null : payload.sessionId(), principal,
                userId -> desk.transfer(payload.sessionId(), userId, payload.toManagerId()));
    }

    // Kiểm tra người gửi rồi thực thi thao tác chat.
    private void handle(Long sessionId, Principal principal, Action action) {
        if (principal == null || sessionId == null || sessionId <= 0) {
            return;
        }
        Long userId;
        try {
            userId = Long.valueOf(principal.getName());
        } catch (NumberFormatException exception) {
            return;
        }
        try {
            action.run(userId);
        } catch (AppException exception) {
            socket.convertAndSendToUser(String.valueOf(userId), "/queue/errors",
                    Map.of("type", "ERROR", "sessionId", sessionId, "message", exception.getMessage()));
            if (access.isParticipant(sessionId, userId)) {
                publisher.sendToSession(sessionId, Map.of("type", "ERROR", "message", exception.getMessage()));
            }
        }
    }

    @FunctionalInterface
    private interface Action {
        // Thực thi thao tác chat với mã người dùng.
        void run(Long userId);
    }
}
