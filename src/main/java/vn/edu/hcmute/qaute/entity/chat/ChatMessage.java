package vn.edu.hcmute.qaute.entity.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.ChatContentType;
import vn.edu.hcmute.qaute.common.constant.ChatSenderType;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

@Entity
@Table(name = "chat_messages")
@Getter @Setter
public class ChatMessage extends AuditableEntity {
    @Column(name = "session_id", nullable = false) private Long sessionId;
    @Column(name = "sender_id") private Long senderId;
    @Enumerated(EnumType.STRING) @Column(name = "sender_type", nullable = false, length = 10) private ChatSenderType senderType;
    @Column(name = "client_message_id", length = 40) private String clientMessageId;
    @Column(nullable = false, length = 2000) private String content;
    @Enumerated(EnumType.STRING) @Column(name = "content_type", nullable = false, length = 12) private ChatContentType contentType;
    @Column(name = "asset_id") private Long assetId;
    @Column(name = "sent_at", nullable = false) private LocalDateTime sentAt;
    @Column(name = "read_at") private LocalDateTime readAt;
}
