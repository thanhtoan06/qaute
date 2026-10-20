package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.ChatContentType;
import vn.edu.hcmute.qaute.common.constant.ChatSenderType;

public record ChatMessageView(Long id, Long sessionId, ChatSenderType senderType, Long senderId,
        String senderName, String content, ChatContentType contentType, Long assetId,
        String clientMessageId, LocalDateTime sentAt, LocalDateTime readAt) {
}
