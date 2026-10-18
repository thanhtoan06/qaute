package vn.edu.hcmute.qaute.controller.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.ConflictException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.manager.AssignForm;
import vn.edu.hcmute.qaute.dto.view.ManagerOption;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.ticket.TicketAccessService;
import vn.edu.hcmute.qaute.service.ticket.TicketWorkflowService;

@ExtendWith(MockitoExtension.class)
class ManagerTicketActionControllerTest {
    @Mock TicketWorkflowService workflow;
    @Mock TicketAccessService access;
    @Mock ManagerScopeService scopes;
    @InjectMocks ManagerTicketActionController controller;

    @AfterEach
    void clearSecurity() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void claimConflictReturnsVietnameseFlashAndKeepsSafeQueueUrl() {
        authenticate();
        when(workflow.claim(42L, 7L)).thenThrow(new ConflictException("Conflict"));
        RedirectAttributesModelMap flash = new RedirectAttributesModelMap();

        String result = controller.claim(42L, "/manager/queue?tab=unassigned&page=2", flash);

        assertEquals("redirect:/manager/queue?tab=unassigned&page=2", result);
        assertEquals("Yêu cầu vừa được người khác nhận. Vui lòng tải lại hàng đợi",
                flash.getFlashAttributes().get("flashError"));
    }

    @Test
    void invalidRedirectFallsBackToQueue() {
        assertEquals("redirect:/manager/queue",
                ManagerTicketActionController.redirectTo("https://example.com"));
        assertEquals("redirect:/manager/queue",
                ManagerTicketActionController.redirectTo("/manager/../auth/login"));
        assertEquals("redirect:/manager/queue?tab=mine",
                ManagerTicketActionController.redirectTo("/manager/queue?tab=mine"));
    }

    @Test
    void advisorsChecksTicketVisibilityBeforeLookingUpCategory() {
        authenticate();
        Ticket ticket = Ticket.builder().categoryId(3L).build();
        when(access.requireViewable(42L, 7L, RoleCode.MANAGER)).thenReturn(ticket);
        when(scopes.findAdvisors(3L)).thenReturn(List.of(
                new ManagerOption(8L, "Tư vấn viên", null, null, null)));

        var result = controller.advisors(42L);

        assertEquals(8L, result.getData().getFirst().id());
        var order = inOrder(access, scopes);
        order.verify(access).requireViewable(42L, 7L, RoleCode.MANAGER);
        order.verify(scopes).findAdvisors(3L);
    }

    @Test
    void hiddenTicketDoesNotRevealAdvisors() {
        authenticate();
        when(access.requireViewable(42L, 7L, RoleCode.MANAGER))
                .thenThrow(new NotFoundException("Không tìm thấy ticket"));

        assertThrows(NotFoundException.class, () -> controller.advisors(42L));

        verify(scopes, never()).findAdvisors(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void assignRejectsWhitespaceReasonBeforeWorkflow() {
        AssignForm form = new AssignForm();
        form.setToManagerId(8L);
        form.setReason("x          ");
        RedirectAttributesModelMap flash = new RedirectAttributesModelMap();

        String result = controller.assign(42L, form,
                new BeanPropertyBindingResult(form, "assignForm"), null, flash);

        assertEquals("redirect:/manager/queue", result);
        assertEquals("Vui lòng chọn tư vấn viên và nhập lý do từ 10 đến 500 ký tự",
                flash.getFlashAttributes().get("flashError"));
        verify(workflow, never()).assign(org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any());
    }

    private static void authenticate() {
        QauteUserDetails user = new QauteUserDetails(7L, "manager@example.com", "Manager",
                RoleCode.MANAGER, "test", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }
}
