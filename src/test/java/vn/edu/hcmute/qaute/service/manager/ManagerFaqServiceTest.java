package vn.edu.hcmute.qaute.service.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.util.HtmlUtils;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.SenderType;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.admin.FaqForm;
import vn.edu.hcmute.qaute.dto.view.StudentSummary;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.knowledge.Faq;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.entity.ticket.TicketMessage;
import vn.edu.hcmute.qaute.repository.knowledge.FaqRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketMessageRepository;
import vn.edu.hcmute.qaute.service.admin.AdminFaqService;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.ticket.TicketAccessService;

@ExtendWith(MockitoExtension.class)
class ManagerFaqServiceTest {
    @Mock AdminFaqService adminFaqs;
    @Mock FaqRepository faqs;
    @Mock ManagerScopeService scopes;
    @Mock TicketAccessService ticketAccess;
    @Mock TicketMessageRepository messages;
    @Mock UserLookupService users;
    @Mock AuditService audit;
    @InjectMocks ManagerFaqService service;

    @Test
    void saveRejectsCategoryOutsideManagerScope() {
        FaqForm form = new FaqForm();
        form.setCategoryId(4L);

        assertThrows(BadRequestException.class, () -> service.save(form, 7L));
        verify(adminFaqs, never()).save(any(), any());
    }

    @Test
    void otherManagersDraftIsNotEditable() {
        Faq faq = Faq.builder().authorId(8L).status(FaqStatus.DRAFT).build();
        when(faqs.findById(12L)).thenReturn(Optional.of(faq));

        assertThrows(NotFoundException.class, () -> service.getForm(12L, 7L));
        verify(adminFaqs, never()).getForm(12L);
    }

    @Test
    void createFromTicketRedactsPersonalDataAndLinksDraft() {
        Ticket ticket = Ticket.builder().studentId(2L).categoryId(4L)
                .title("Cần hỗ trợ Nguyễn Văn A về học phí")
                .descriptionHtml("<p>Nguyễn Văn A, 12345678, sv@example.com, 0912345678 cần giúp.</p>")
                .status(TicketStatus.RESOLVED).allowPublicFaq(true).build();
        ticket.setId(42L);
        TicketMessage reply = TicketMessage.builder().senderType(SenderType.MANAGER)
                .contentHtml("<p>Liên hệ sv@example.com để xử lý.</p>").build();
        Faq saved = Faq.builder().authorId(7L).status(FaqStatus.DRAFT).build();
        saved.setId(99L);
        when(ticketAccess.requireViewable(42L, 7L, RoleCode.MANAGER)).thenReturn(ticket);
        when(scopes.isInScope(7L, 4L)).thenReturn(true);
        when(users.getStudentSummary(2L)).thenReturn(new StudentSummary(
                new UserSummary(2L, "Nguyễn Văn A", "sv@example.com", "12345678",
                        null, RoleCode.STUDENT, null), "0912345678", null, null, null, null));
        when(messages.findByTicketIdOrderByCreatedAtAsc(42L)).thenReturn(List.of(reply));
        when(adminFaqs.save(any(), any())).thenReturn(99L);
        when(faqs.findById(99L)).thenReturn(Optional.of(saved));

        assertEquals(99L, service.createFromTicket(42L, 7L));

        ArgumentCaptor<FaqForm> form = ArgumentCaptor.forClass(FaqForm.class);
        verify(adminFaqs).save(form.capture(), org.mockito.ArgumentMatchers.eq(7L));
        assertEquals(4L, form.getValue().getCategoryId());
        assertTrue(form.getValue().getBodyHtml().contains("<h3>Câu hỏi</h3>"));
        assertTrue(form.getValue().getBodyHtml().contains("<h3>Trả lời</h3>"));
        assertTrue(HtmlUtils.htmlUnescape(form.getValue().getBodyHtml()).contains("[đã ẩn]"));
        assertFalse(form.getValue().getBodyHtml().contains("sv@example.com"));
        assertFalse(form.getValue().getBodyHtml().contains("12345678"));
        assertFalse(form.getValue().getBodyHtml().contains("0912345678"));
        assertFalse(form.getValue().getBodyHtml().contains("Nguyễn Văn A"));
        assertEquals(42L, saved.getSourceTicketId());
        verify(audit).log(AuditActions.FAQ_FROM_TICKET, "Faq", 99L, null, "ticketId=42");
    }

    @Test
    void ticketRequiresConsentAndFinalStatus() {
        Ticket ticket = Ticket.builder().status(TicketStatus.IN_PROGRESS)
                .allowPublicFaq(true).build();
        when(ticketAccess.requireViewable(42L, 7L, RoleCode.MANAGER)).thenReturn(ticket);

        assertThrows(BadRequestException.class, () -> service.createFromTicket(42L, 7L));
        verify(adminFaqs, never()).save(any(), any());
    }
}
