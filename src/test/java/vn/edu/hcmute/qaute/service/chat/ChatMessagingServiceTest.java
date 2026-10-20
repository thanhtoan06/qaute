package vn.edu.hcmute.qaute.service.chat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import vn.edu.hcmute.qaute.common.constant.ChatContentType;
import vn.edu.hcmute.qaute.common.constant.ChatSenderType;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.ConflictException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.entity.chat.ChatMessage;
import vn.edu.hcmute.qaute.entity.chat.ChatSession;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.repository.chat.ChatMessageRepository;
import vn.edu.hcmute.qaute.repository.chat.ChatSessionRepository;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.media.MediaService;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@ExtendWith(MockitoExtension.class)
class ChatMessagingServiceTest {
    @Mock ChatSessionRepository sessions;
    @Mock ChatMessageRepository messages;
    @Mock MediaAssetRepository assets;
    @Mock MediaService media;
    @Mock SettingsService settings;
    @Mock UserLookupService users;
    @Mock ChatEventPublisher publisher;
    @Mock ApplicationEventPublisher events;
    @InjectMocks ChatMessagingService service;

    @Test
    void repeatedClientMessageIdReturnsStoredMessage() {
        when(sessions.findLockedById(8L)).thenReturn(Optional.of(session()));
        ChatMessage existing = new ChatMessage();
        existing.setId(12L);
        existing.setSessionId(8L);
        existing.setSenderId(3L);
        existing.setSenderType(ChatSenderType.STUDENT);
        existing.setContent("Xin chào");
        existing.setContentType(ChatContentType.TEXT);
        existing.setSentAt(LocalDateTime.now());
        when(messages.findBySessionIdAndClientMessageId(8L, "client-1"))
                .thenReturn(Optional.of(existing));
        assertEquals(12L, service.send(8L, 3L, "client-1", "Tin gửi lại", null).id());
        verify(messages, never()).saveAndFlush(any());
        verify(publisher, never()).sendToSession(any(), any());
    }

    @Test
    void anotherParticipantCannotReuseClientMessageId() {
        when(sessions.findLockedById(8L)).thenReturn(Optional.of(session()));
        ChatMessage existing = new ChatMessage();
        existing.setSenderId(7L);
        when(messages.findBySessionIdAndClientMessageId(8L, "client-1"))
                .thenReturn(Optional.of(existing));
        assertThrows(ConflictException.class, () -> service.send(8L, 3L, "client-1", "Xin chào", null));
        verify(messages, never()).saveAndFlush(any());
    }

    @Test
    void outsiderCannotSendEvenWithValidSessionId() {
        when(sessions.findLockedById(8L)).thenReturn(Optional.of(session()));
        assertThrows(NotFoundException.class, () -> service.send(8L, 99L, null, "Xin chào", null));
        verify(messages, never()).saveAndFlush(any());
    }

    @Test
    void rejectsOrphanFileOwnedByAnotherUser() {
        when(sessions.findLockedById(8L)).thenReturn(Optional.of(session()));
        MediaAsset asset = new MediaAsset();
        asset.setOwnerId(99L);
        asset.setUsageType(MediaUsageType.CHAT_ATTACHMENT);
        asset.setStatus(MediaStatus.ORPHAN);
        when(assets.findById(20L)).thenReturn(Optional.of(asset));
        assertThrows(BadRequestException.class, () -> service.send(8L, 3L, null, "Tệp đính kèm", 20L));
        verify(media, never()).attach(any(), any(), any(), any());
    }

    private ChatSession session() {
        ChatSession session = new ChatSession();
        session.setId(8L);
        session.setStudentId(3L);
        session.setManagerId(7L);
        session.setStatus(ChatSessionStatus.ACTIVE);
        return session;
    }
}
