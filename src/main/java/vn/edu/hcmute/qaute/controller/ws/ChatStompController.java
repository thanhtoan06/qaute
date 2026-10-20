package vn.edu.hcmute.qaute.controller.ws;

import java.security.Principal;
import java.util.Map;
import org.springframework.messaging.handler.annotation.MessageMapping;
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

@Controller
public class ChatStompController {
    private final ChatMessagingService messaging;
    private final ChatDeskService desk;
    private final ChatAccessChecker access;
    private final ChatEventPublisher publisher;

    public ChatStompController(ChatMessagingService messaging, ChatDeskService desk,
            ChatAccessChecker access, ChatEventPublisher publisher) {
        this.messaging = messaging;
        this.desk = desk;
        this.access = access;
        this.publisher = publisher;
    }

    @MessageMapping("chat.send")
    public void send(ChatSendPayload payload, Principal principal) {
        handle(payload == null ? null : payload.sessionId(), principal, userId -> messaging.send(
                payload.sessionId(), userId, payload.clientMessageId(), payload.content(), payload.assetId()));
    }

    @MessageMapping("chat.typing")
    public void typing(ChatTypingPayload payload, Principal principal) {
        handle(payload == null ? null : payload.sessionId(), principal, userId -> {
            if (payload.isTyping() == null) throw new BadRequestException("Thiếu trạng thái đang nhập");
            messaging.typing(payload.sessionId(), userId, payload.isTyping());
        });
    }

    @MessageMapping("chat.read")
    public void read(ChatReadPayload payload, Principal principal) {
        handle(payload == null ? null : payload.sessionId(), principal,
                userId -> messaging.read(payload.sessionId(), userId, payload.lastReadMessageId()));
    }

    @MessageMapping("chat.end")
    public void end(ChatSessionPayload payload, Principal principal) {
        handle(payload == null ? null : payload.sessionId(), principal,
                userId -> messaging.end(payload.sessionId(), userId));
    }

    @MessageMapping("chat.accept")
    public void accept(ChatSessionPayload payload, Principal principal) {
        handle(payload == null ? null : payload.sessionId(), principal,
                userId -> desk.accept(payload.sessionId(), userId));
    }

    @MessageMapping("chat.transfer")
    public void transfer(ChatTransferPayload payload, Principal principal) {
        handle(payload == null ? null : payload.sessionId(), principal,
                userId -> desk.transfer(payload.sessionId(), userId, payload.toManagerId()));
    }

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
            if (access.isParticipant(sessionId, userId)) {
                publisher.sendToSession(sessionId, Map.of("type", "ERROR", "message", exception.getMessage()));
            }
        }
    }

    @FunctionalInterface
    private interface Action {
        void run(Long userId);
    }
}
