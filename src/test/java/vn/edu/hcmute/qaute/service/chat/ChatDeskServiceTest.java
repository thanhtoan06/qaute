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
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import org.springframework.data.domain.Pageable;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @Mock TicketRepository tickets;
    @Mock org.springframework.messaging.simp.SimpMessagingTemplate socket;
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

    @Test
    void mineShowsOnlyRecentTicketsWithinManagerScope() {
        ChatSession session = new ChatSession();
        session.setId(8L); session.setStudentId(3L); session.setCategoryId(4L);
        session.setStatus(vn.edu.hcmute.qaute.common.constant.ChatSessionStatus.ACTIVE);
        Ticket ticket = new Ticket(); ticket.setId(9L); ticket.setCode("QA-9"); ticket.setTitle("Cần hỗ trợ");
        when(scopes.getScopeCategoryIds(7L)).thenReturn(List.of(4L));
        when(sessions.findByManagerIdAndStatus(7L,
                vn.edu.hcmute.qaute.common.constant.ChatSessionStatus.ACTIVE)).thenReturn(List.of(session));
        when(tickets.findByStudentIdAndCategoryIdInOrderByCreatedAtDescIdDesc(
                org.mockito.ArgumentMatchers.eq(3L), org.mockito.ArgumentMatchers.eq(List.of(4L)),
                any(Pageable.class))).thenReturn(List.of(ticket));
        assertEquals(9L, service.mine(7L).getFirst().recentTickets().getFirst().id());
    }
}
