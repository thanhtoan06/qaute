package vn.edu.hcmute.qaute.service.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.SenderType;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.view.StudentSummary;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.entity.ticket.TicketEscalation;
import vn.edu.hcmute.qaute.entity.ticket.TicketMessage;
import vn.edu.hcmute.qaute.entity.ticket.TicketReview;
import vn.edu.hcmute.qaute.entity.ticket.TicketStatusHistory;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketAssignmentRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketAttachmentRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketEscalationRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketMessageRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketReviewRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketStatusHistoryRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.knowledge.FaqQueryService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.ticket.TicketAccessService;

@ExtendWith(MockitoExtension.class)
class ManagerTicketDetailServiceTest {
    @Mock TicketAccessService access;
    @Mock TicketRepository tickets;
    @Mock TicketMessageRepository messages;
    @Mock TicketAttachmentRepository attachments;
    @Mock TicketStatusHistoryRepository histories;
    @Mock TicketAssignmentRepository assignments;
    @Mock TicketEscalationRepository escalations;
    @Mock MediaAssetRepository assets;
    @Mock UserLookupService users;
    @Mock ManagerScopeService scopes;
    @Mock CategoryQueryService categories;
    @Mock FaqQueryService faqs;
    @Mock TicketNoteService noteService;
    @Mock TicketReviewRepository reviews;
    @InjectMocks ManagerTicketDetailService service;

    @Test
    void viewRequiresAccessBeforeReadingRelatedData() {
        when(access.requireViewable(9L, 1L, RoleCode.MANAGER))
                .thenThrow(new NotFoundException("Không tìm thấy ticket"));
        assertThrows(NotFoundException.class, () -> service.getDetail(1L, 9L));
        verify(users, never()).getStudentSummary(any());
        verify(scopes, never()).getScopeCategoryIds(any());
    }

    @Test
    void detailShowsOnlyOtherTicketsInScopeAndSanitizesContent() {
        Ticket ticket = Ticket.builder().studentId(2L).categoryId(3L).code("TK-9")
                .title("Xin tư vấn học vụ").descriptionHtml("<p>Nội dung</p><script>alert(1)</script>")
                .status(TicketStatus.ASSIGNED).currentAssigneeId(1L).build();
        ticket.setId(9L);
        Ticket other = Ticket.builder().code("TK-10").title("Yêu cầu khác")
                .status(TicketStatus.NEW).build();
        other.setId(10L);
        TicketMessage message = TicketMessage.builder().senderId(2L)
                .senderType(SenderType.STUDENT).contentHtml("<b>Chào</b><script>x</script>").build();
        message.setId(5L);
        message.setCreatedAt(LocalDateTime.of(2026, 1, 2, 10, 0));
        TicketStatusHistory history = TicketStatusHistory.builder().toStatus(TicketStatus.ASSIGNED).build();
        history.setCreatedAt(LocalDateTime.of(2026, 1, 2, 11, 0));
        TicketEscalation escalation = TicketEscalation.builder().fromCategoryId(3L)
                .toCategoryId(4L).fromManagerId(1L).toManagerId(5L)
                .reason("Cần chuyên viên có thẩm quyền xử lý").build();
        escalation.setCreatedAt(LocalDateTime.of(2026, 1, 2, 12, 0));
        when(access.requireViewable(9L, 1L, RoleCode.MANAGER)).thenReturn(ticket);
        when(users.getStudentSummary(2L)).thenReturn(new StudentSummary(
                new UserSummary(2L, "Sinh viên", "sv@example.com", "12345678", null, null, null),
                null, null, null, null, null));
        when(messages.findByTicketIdOrderByCreatedAtAsc(9L)).thenReturn(List.of(message));
        when(histories.findByTicketIdOrderByCreatedAtAsc(9L)).thenReturn(List.of(history));
        when(escalations.findByTicketIdOrderByCreatedAtAsc(9L)).thenReturn(List.of(escalation));
        when(users.getSummaries(any())).thenReturn(Map.of(
                1L, new UserSummary(1L, "Tư vấn viên", null, null, null, null, null),
                5L, new UserSummary(5L, "Người nhận", null, null, null, null, null)));
        when(categories.getDisplayPath(3L)).thenReturn("Học vụ");
        when(categories.getDisplayPath(4L)).thenReturn("Tài chính");
        when(scopes.getScopeCategoryIds(1L)).thenReturn(List.of(3L));
        when(tickets.findOtherStudentTicketsInScope(eq(2L), eq(9L), eq(List.of(3L)),
                any(Pageable.class))).thenReturn(List.of(other));

        var detail = service.getDetail(1L, 9L);

        assertEquals(10L, detail.otherTickets().getFirst().id());
        assertEquals(2, detail.timeline().size());
        assertTrue(detail.timeline().get(1).label().contains("Học vụ sang Tài chính"));
        assertTrue(detail.timeline().get(1).label().contains("người nhận: Người nhận"));
        assertTrue(detail.timeline().get(1).label().contains(escalation.getReason()));
        assertTrue(detail.mine());
        assertTrue(detail.canReply());
        assertTrue(detail.canRequestInfo());
        assertFalse(detail.canResolve());
        assertFalse(detail.ticket().descriptionHtml().contains("<script>"));
        assertFalse(detail.messages().getFirst().contentHtml().contains("<script>"));
        verify(faqs).suggest("Xin tư vấn học vụ", 5);
    }

    @Test
    void adminDetailShowsTicketsOutsideManagerScopeAndStudentReview() {
        Ticket ticket = Ticket.builder().studentId(2L).categoryId(3L).code("TK-9")
                .title("Yêu cầu").descriptionHtml("<p>Nội dung</p>")
                .status(TicketStatus.CLOSED).build();
        ticket.setId(9L);
        Ticket other = Ticket.builder().code("TK-10").title("Yêu cầu khác")
                .status(TicketStatus.NEW).build();
        other.setId(10L);
        TicketReview review = TicketReview.builder().ticketId(9L).studentId(2L)
                .rating(4).comment("Hữu ích").build();
        when(access.requireViewable(9L, 1L, RoleCode.ADMIN)).thenReturn(ticket);
        when(tickets.findByStudentIdAndIdNotOrderByCreatedAtDescIdDesc(eq(2L), eq(9L),
                any(Pageable.class))).thenReturn(List.of(other));
        when(reviews.findByTicketId(9L)).thenReturn(java.util.Optional.of(review));

        var detail = service.getDetail(1L, 9L, RoleCode.ADMIN);

        assertEquals(10L, detail.otherTickets().getFirst().id());
        assertEquals(4, detail.review().rating());
        verify(scopes, never()).getScopeCategoryIds(any());
        verify(noteService).listNotes(9L, 1L, RoleCode.ADMIN);
    }
}
