package vn.edu.hcmute.qaute.service.chat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;
import vn.edu.hcmute.qaute.common.exception.ConflictException;
import vn.edu.hcmute.qaute.dto.view.ManagerOption;
import vn.edu.hcmute.qaute.entity.chat.ChatSession;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.organization.ManagerProfile;
import vn.edu.hcmute.qaute.repository.chat.ChatSessionRepository;
import vn.edu.hcmute.qaute.repository.organization.ManagerProfileRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.system.SettingsService;
import vn.edu.hcmute.qaute.websocket.WsSessionRegistry;

@ExtendWith(MockitoExtension.class)
class ChatDeskServiceTest {
    @Mock ChatSessionRepository sessions;
    @Mock ManagerScopeService scopes;
    @Mock ManagerProfileRepository profiles;
    @Mock ChatAvailabilityService availability;
    @Mock SettingsService settings;
    @Mock UserLookupService users;
    @Mock CategoryQueryService categories;
    @Mock ChatMessagingService messaging;
    @Mock ChatEventPublisher publisher;
    @Mock ApplicationEventPublisher events;
    @Mock AuditService audit;
    @Mock EntityManager entityManager;
    @Mock WsSessionRegistry webSocketSessions;
    @InjectMocks ChatDeskService service;

    @Test
    void concurrentAcceptReportsConflictWhenConditionalUpdateLoses() {
        ChatSession waiting = new ChatSession();
        waiting.setId(8L);
        waiting.setCategoryId(4L);
        waiting.setStatus(vn.edu.hcmute.qaute.common.constant.ChatSessionStatus.WAITING);
        when(sessions.findById(8L)).thenReturn(Optional.of(waiting));
        when(scopes.isInScope(7L, 4L)).thenReturn(true);
        when(scopes.findAdvisors(4L)).thenReturn(List.of(new ManagerOption(
                7L, "Tư vấn viên", null, null, AvailabilityStatus.ONLINE)));
        when(entityManager.find(User.class, 7L, LockModeType.PESSIMISTIC_WRITE)).thenReturn(new User());
        ManagerProfile profile = new ManagerProfile();
        profile.setMaxConcurrentChats(3);
        when(profiles.findByUserId(7L)).thenReturn(Optional.of(profile));
        assertThrows(ConflictException.class, () -> service.accept(8L, 7L));
        verify(messaging, never()).systemMessage(any(), any());
    }
}
