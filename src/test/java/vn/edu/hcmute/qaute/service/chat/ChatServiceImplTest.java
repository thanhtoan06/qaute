package vn.edu.hcmute.qaute.service.chat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.mockito.ArgumentCaptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.entity.chat.ChatSession;
import vn.edu.hcmute.qaute.entity.chat.ChatMessage;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.chat.ChatMessageRepository;
import vn.edu.hcmute.qaute.repository.chat.ChatSessionRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.knowledge.FaqQueryService;
import vn.edu.hcmute.qaute.service.notification.NotificationService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.ticket.TicketWorkflowService;
import vn.edu.hcmute.qaute.service.ticket.command.CreateTicketCommand;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {
    @Mock ChatSessionRepository sessions;
    @Mock ChatMessageRepository messages;
    @Mock ChatAvailabilityService availability;
    @Mock CategoryQueryService categories;
    @Mock FaqQueryService faqs;
    @Mock UserLookupService users;
    @Mock NotificationService notifications;
    @Mock ApplicationEventPublisher events;
    @Mock EntityManager entityManager;
    @Mock TicketWorkflowService ticketWorkflow;
    @InjectMocks ChatServiceImpl service;

    @Test
    void startReturnsExistingSessionWithoutCreatingAnother() {
        ChatSession existing = session(ChatSessionStatus.BOT);
        when(entityManager.find(User.class, 3L, LockModeType.PESSIMISTIC_WRITE)).thenReturn(new User());
        when(sessions.findFirstByStudentIdAndStatusInOrderByIdDesc(eq(3L), any()))
                .thenReturn(Optional.of(existing));
        when(faqs.suggest("Hỏi về lịch học", 3)).thenReturn(List.of());
        assertEquals(9L, service.start(3L, 7L, "Câu hỏi mới").sessionId());
        verify(sessions, never()).saveAndFlush(any());
        verify(messages, never()).save(any());
    }

    @Test
    void requestAgentWithoutWorkingHoursExpiresSession() {
        ChatSession session = session(ChatSessionStatus.BOT);
        when(sessions.findById(9L)).thenReturn(Optional.of(session));
        when(availability.isWorkingNow()).thenReturn(false);
        var view = service.requestAgent(9L, 3L);
        assertEquals(ChatSessionStatus.EXPIRED, view.status());
        assertEquals("NO_AGENT", session.getEndReason());
        assertTrue(view.message().contains("chuyển thành yêu cầu tư vấn"));
        verify(sessions).saveAndFlush(session);
        verify(notifications, never()).notifyUsers(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void outsiderCannotReadMessages() {
        when(sessions.findById(9L)).thenReturn(Optional.of(session(ChatSessionStatus.ACTIVE)));
        assertThrows(NotFoundException.class, () -> service.messages(9L, 99L, 0L));
        verify(messages, never()).findTop200BySessionIdAndIdGreaterThanOrderByIdAsc(any(), any());
    }

    @Test
    void ticketTranscriptEscapesChatContentAndKeepsStudentOwner() {
        ChatSession session = session(ChatSessionStatus.ENDED);
        when(sessions.findLockedById(9L)).thenReturn(Optional.of(session));
        ChatMessage message = new ChatMessage();
        message.setSenderType(vn.edu.hcmute.qaute.common.constant.ChatSenderType.STUDENT);
        message.setContent("<script>alert('x')</script>");
        message.setSentAt(LocalDateTime.now());
        when(messages.findBySessionIdOrderByIdAsc(9L)).thenReturn(List.of(message));
        Ticket ticket = new Ticket();
        ticket.setId(50L);
        when(ticketWorkflow.createTicket(eq(3L), any(CreateTicketCommand.class))).thenReturn(ticket);

        assertEquals(50L, service.createTicketFromChat(9L, 3L));
        ArgumentCaptor<CreateTicketCommand> command = ArgumentCaptor.forClass(CreateTicketCommand.class);
        verify(ticketWorkflow).createTicket(eq(3L), command.capture());
        assertTrue(command.getValue().descriptionHtml().contains("&lt;script&gt;"));
        assertTrue(!command.getValue().descriptionHtml().contains("<script>"));
        assertEquals(50L, session.getTicketId());
    }

    private ChatSession session(ChatSessionStatus status) {
        ChatSession session = new ChatSession();
        session.setId(9L);
        session.setStudentId(3L);
        session.setCategoryId(7L);
        session.setInitialQuestion("Hỏi về lịch học");
        session.setStartedAt(LocalDateTime.now());
        session.setStatus(status);
        return session;
    }
}
