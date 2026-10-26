package vn.edu.hcmute.qaute.service.chat;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.ChatContentType;
import vn.edu.hcmute.qaute.common.constant.ChatSenderType;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.ConflictException;
import vn.edu.hcmute.qaute.common.exception.InvalidTransitionException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.view.ChatMessageView;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.chat.ChatMessage;
import vn.edu.hcmute.qaute.entity.chat.ChatSession;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.repository.chat.ChatMessageRepository;
import vn.edu.hcmute.qaute.repository.chat.ChatSessionRepository;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.media.MediaService;
import vn.edu.hcmute.qaute.service.system.SettingsService;

// Xử lý nghiệp vụ ChatMessaging trong hệ thống.
@Service
@Transactional
public class ChatMessagingService {
    private final ChatSessionRepository sessions;
    private final ChatMessageRepository messages;
    private final MediaAssetRepository assets;
    private final MediaService media;
    private final SettingsService settings;
    private final UserLookupService users;
    private final ChatEventPublisher publisher;
    private final ApplicationEventPublisher events;
    private final AuditService audit;

    // Khởi tạo dịch vụ nhắn tin với kho dữ liệu và các dịch vụ phụ trợ.
    public ChatMessagingService(ChatSessionRepository sessions, ChatMessageRepository messages,
            MediaAssetRepository assets, MediaService media, SettingsService settings,
            UserLookupService users, ChatEventPublisher publisher, ApplicationEventPublisher events,
            AuditService audit) {
        this.sessions = sessions;
        this.messages = messages;
        this.assets = assets;
        this.media = media;
        this.settings = settings;
        this.users = users;
        this.publisher = publisher;
        this.events = events;
        this.audit = audit;
    }

    // Gửi tin nhắn văn bản hoặc tệp đính kèm vào phiên chat.
    public ChatMessageView send(Long sessionId, Long senderId, String clientMessageId,
            String content, Long assetId) {
        ChatSession session = sessions.findLockedById(sessionId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên chat"));
        requireParticipant(session, senderId);
        if (session.getStatus() != ChatSessionStatus.ACTIVE) {
            throw new InvalidTransitionException("Phiên chat chưa hoạt động hoặc đã kết thúc");
        }
        String clientId = clientMessageId == null || clientMessageId.isBlank() ? null : clientMessageId.trim();
        if (clientId != null && clientId.length() > 40) {
            throw new BadRequestException("Mã tin nhắn tối đa 40 ký tự");
        }
        if (clientId != null) {
            var previous = messages.findBySessionIdAndClientMessageId(sessionId, clientId);
            if (previous.isPresent()) {
                if (!Objects.equals(previous.get().getSenderId(), senderId)) {
                    throw new ConflictException("Mã tin nhắn đã được người khác sử dụng");
                }
                return toView(previous.get());
            }
        }
        MediaAsset asset = null;
        if (assetId != null) {
            asset = assets.findById(assetId).orElseThrow(() -> new NotFoundException("Không tìm thấy tệp"));
            if (asset.getStatus() != MediaStatus.ORPHAN || asset.getUsageType() != MediaUsageType.CHAT_ATTACHMENT
                    || !Objects.equals(asset.getOwnerId(), senderId) || asset.getUsageRefId() != null) {
                throw new BadRequestException("Tệp đính kèm không hợp lệ");
            }
        }
        String text = content == null || content.isBlank()
                ? asset == null ? "" : asset.getOriginalFilename() : content.trim();
        int max = settings.getInt(SettingKeys.CHAT_MESSAGE_MAX_LENGTH);
        if (text.isBlank() || text.length() > max || text.length() > 2000) {
            throw new BadRequestException("Tin nhắn phải dài 1–" + Math.min(max, 2000) + " ký tự");
        }
        if (asset != null) {
            media.attach(assetId, MediaUsageType.CHAT_ATTACHMENT, sessionId, senderId);
        }
        ChatMessage message = new ChatMessage();
        message.setSessionId(sessionId);
        message.setSenderId(senderId);
        message.setSenderType(Objects.equals(senderId, session.getStudentId())
                ? ChatSenderType.STUDENT : ChatSenderType.MANAGER);
        message.setClientMessageId(clientId);
        message.setContent(text);
        message.setContentType(asset == null ? ChatContentType.TEXT : ChatContentType.ATTACHMENT);
        message.setAssetId(assetId);
        message.setSentAt(LocalDateTime.now());
        message = messages.saveAndFlush(message);
        session.setLastMessageAt(message.getSentAt());
        sessions.save(session);
        ChatMessageView view = toView(message);
        publisher.sendToSession(sessionId, Map.of("type", "NEW_MESSAGE", "message", view));
        return view;
    }

    @Transactional(readOnly = true)
    // Phát trạng thái nhập liệu của người tham gia phiên chat.
    public void typing(Long sessionId, Long userId, boolean isTyping) {
        ChatSession session = requireSession(sessionId);
        requireParticipant(session, userId);
        if (session.getStatus() != ChatSessionStatus.ACTIVE) {
            throw new InvalidTransitionException("Phiên chat không hoạt động");
        }
        ChatSenderType senderType = Objects.equals(userId, session.getStudentId())
                ? ChatSenderType.STUDENT : ChatSenderType.MANAGER;
        publisher.sendToSession(sessionId, Map.of("type", "TYPING", "sessionId", sessionId, "userId", userId,
                "senderType", senderType, "isTyping", isTyping));
    }

    // Đánh dấu tin nhắn đã đọc và thông báo cập nhật nếu có thay đổi.
    public void read(Long sessionId, Long userId, Long lastReadMessageId) {
        ChatSession session = requireSession(sessionId);
        requireParticipant(session, userId);
        if (lastReadMessageId == null || lastReadMessageId <= 0
                || messages.findById(lastReadMessageId)
                        .filter(message -> sessionId.equals(message.getSessionId())).isEmpty()) {
            throw new BadRequestException("Mã tin nhắn đã đọc không hợp lệ");
        }
        int updated = messages.markRead(sessionId, userId, lastReadMessageId, LocalDateTime.now());
        if (updated > 0) {
            publisher.sendToSession(sessionId, Map.of("type", "MESSAGE_READ",
                    "messageId", lastReadMessageId));
        }
    }

    // Kết thúc phiên chat do một trong các bên tham gia yêu cầu.
    public void end(Long sessionId, Long userId) {
        ChatSession session = sessions.findLockedById(sessionId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên chat"));
        requireParticipant(session, userId);
        ChatSessionStatus previous = session.getStatus();
        if (previous != ChatSessionStatus.ACTIVE && previous != ChatSessionStatus.WAITING
                && previous != ChatSessionStatus.BOT) {
            throw new InvalidTransitionException("Phiên chat đã kết thúc");
        }
        session.setStatus(ChatSessionStatus.ENDED);
        session.setEndedBy(userId);
        session.setEndReason(Objects.equals(userId, session.getStudentId()) ? "STUDENT_END" : "MANAGER_END");
        session.setEndedAt(LocalDateTime.now());
        sessions.saveAndFlush(session);
        audit.log(AuditActions.CHAT_SESSION_ENDED, "ChatSession", sessionId,
                "status=" + previous, "status=ENDED; reason=" + session.getEndReason());
        systemMessage(session, "Phiên chat đã kết thúc.");
        publisher.sendToSession(sessionId, Map.of("type", "SESSION_ENDED"));
        if (previous == ChatSessionStatus.WAITING) {
            events.publishEvent(new ChatQueueChangedEvent(session.getCategoryId()));
        }
    }

    // Tạo và phát tin nhắn hệ thống trong phiên chat.
    public ChatMessageView systemMessage(ChatSession session, String content) {
        ChatMessage message = new ChatMessage();
        message.setSessionId(session.getId());
        message.setSenderType(ChatSenderType.SYSTEM);
        message.setContentType(ChatContentType.SYSTEM);
        message.setContent(content);
        message.setSentAt(LocalDateTime.now());
        message = messages.saveAndFlush(message);
        session.setLastMessageAt(message.getSentAt());
        sessions.save(session);
        ChatMessageView view = toView(message);
        publisher.sendToSession(session.getId(), Map.of("type", "NEW_MESSAGE", "message", view));
        return view;
    }

    // Tìm phiên chat theo mã hoặc báo không tìm thấy.
    private ChatSession requireSession(Long sessionId) {
        return sessions.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên chat"));
    }

    // Xác nhận người dùng là sinh viên hoặc tư vấn viên của phiên chat.
    private void requireParticipant(ChatSession session, Long userId) {
        if (userId == null || (!Objects.equals(session.getStudentId(), userId)
                && !Objects.equals(session.getManagerId(), userId))) {
            throw new NotFoundException("Không tìm thấy phiên chat");
        }
    }

    // Chuyển tin nhắn thành dữ liệu hiển thị kèm tên người gửi.
    private ChatMessageView toView(ChatMessage message) {
        UserSummary sender = message.getSenderId() == null ? null : users.getSummary(message.getSenderId());
        String senderName = sender == null
                ? message.getSenderType() == ChatSenderType.BOT ? "Trợ lý QAUTE" : "Hệ thống"
                : sender.fullName();
        return new ChatMessageView(message.getId(), message.getSessionId(), message.getSenderType(),
                message.getSenderId(), senderName, message.getContent(), message.getContentType(),
                message.getAssetId(), message.getClientMessageId(), message.getSentAt(), message.getReadAt());
    }
}
