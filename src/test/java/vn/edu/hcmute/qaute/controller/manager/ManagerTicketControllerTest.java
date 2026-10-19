package vn.edu.hcmute.qaute.controller.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.ConflictException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.manager.CategoryChangeForm;
import vn.edu.hcmute.qaute.dto.request.manager.EscalateForm;
import vn.edu.hcmute.qaute.dto.request.manager.ReplyForm;
import vn.edu.hcmute.qaute.dto.request.manager.ResolveForm;
import vn.edu.hcmute.qaute.dto.view.CategoryView;
import vn.edu.hcmute.qaute.dto.view.ManagerOption;
import vn.edu.hcmute.qaute.dto.view.ManagerTicketDetailView;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.service.manager.ManagerTicketDetailService;
import vn.edu.hcmute.qaute.service.manager.TicketNoteService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.ticket.TicketWorkflowService;

@ExtendWith(MockitoExtension.class)
class ManagerTicketControllerTest {
    @Mock ManagerTicketDetailService details;
    @Mock TicketWorkflowService workflow;
    @Mock CategoryQueryService categories;
    @Mock ManagerScopeService scopes;
    @Mock TicketNoteService notes;
    @InjectMocks ManagerTicketController controller;

    @AfterEach
    void clearSecurity() { SecurityContextHolder.clearContext(); }

    @Test
    void replyUsesAuthenticatedManagerAndPreservesUploadedAssetIds() {
        authenticate();
        ReplyForm form = new ReplyForm();
        form.setContentHtml("<p>Xin chào</p>");
        form.setAssetIds(List.of(8L, 9L));
        var flash = new RedirectAttributesModelMap();

        String location = controller.reply(42L, form,
                new BeanPropertyBindingResult(form, "replyForm"), flash);

        assertEquals("redirect:/manager/tickets/42", location);
        verify(workflow).addAdvisorReply(42L, 7L, "<p>Xin chào</p>", List.of(8L, 9L));
        verify(notes).deleteDraft(42L, 7L);
        assertEquals("Đã gửi phản hồi", flash.getFlashAttributes().get("flashSuccess"));
    }

    @Test
    void invalidResolveDoesNotCallWorkflow() {
        ResolveForm form = new ResolveForm();
        var binding = new BeanPropertyBindingResult(form, "resolveForm");
        binding.rejectValue("resolutionNote", "NotBlank", "Vui lòng nhập tóm tắt giải pháp");
        var flash = new RedirectAttributesModelMap();

        assertEquals("redirect:/manager/tickets/42", controller.resolve(42L, form, binding, flash));
        assertNotNull(flash.getFlashAttributes().get("flashError"));
        verify(workflow, never()).resolve(any(), any(), any());
    }

    @Test
    void workflowConflictReturnsFlashError() {
        authenticate();
        CategoryChangeForm form = new CategoryChangeForm();
        form.setNewCategoryId(11L);
        form.setReason("Chuyển sang chuyên mục phù hợp");
        when(workflow.changeCategory(42L, 7L, 11L, form.getReason()))
                .thenThrow(new ConflictException("Yêu cầu vừa được cập nhật"));
        var flash = new RedirectAttributesModelMap();

        assertEquals("redirect:/manager/tickets/42", controller.category(42L, form,
                new BeanPropertyBindingResult(form, "categoryChangeForm"), flash));
        assertEquals("Yêu cầu vừa được cập nhật", flash.getFlashAttributes().get("flashError"));
    }

    @Test
    void detailLoadsActiveLeafCategoriesOnlyWhenCategoryActionIsAvailable() {
        authenticate();
        var view = new ManagerTicketDetailView(null, null, List.of(), List.of(), List.of(),
                List.of(), List.of(), List.of(), List.of(), false, false, false, false, false, false, true,
                false, false);
        when(details.getDetail(7L, 42L)).thenReturn(view);
        when(categories.findAllActiveLeaf()).thenReturn(List.of(new CategoryView(
                11L, 2L, "Phòng đào tạo", null, "ACA", "Học vụ", null, 1, true)));
        when(categories.getDisplayPath(11L)).thenReturn("Phòng đào tạo › Học vụ");
        var model = new ExtendedModelMap();

        assertEquals("manager/ticket-detail", controller.detail(42L, model));
        assertEquals("Phòng đào tạo › Học vụ",
                ((java.util.Map<?, ?>) model.get("categoryOptions")).get(11L));
    }

    @Test
    void escalationRedirectsToQueueAfterWorkflowSucceeds() {
        authenticate();
        EscalateForm form = new EscalateForm();
        form.setToCategoryId(11L);
        form.setToManagerId(8L);
        form.setReason("Chuyển đến bộ phận phù hợp");
        var flash = new RedirectAttributesModelMap();

        assertEquals("redirect:/manager/queue", controller.escalate(42L, form,
                new BeanPropertyBindingResult(form, "escalateForm"), flash));
        verify(workflow).escalate(42L, 7L, 11L, 8L, form.getReason());
        assertEquals("Đã chuyển cấp yêu cầu", flash.getFlashAttributes().get("flashSuccess"));
    }

    @Test
    void invalidEscalationDoesNotCallWorkflow() {
        EscalateForm form = new EscalateForm();
        var binding = new BeanPropertyBindingResult(form, "escalateForm");
        binding.rejectValue("toCategoryId", "NotNull", "Vui lòng chọn chuyên mục đích");

        assertEquals("redirect:/manager/tickets/42", controller.escalate(42L, form,
                binding, new RedirectAttributesModelMap()));
        verify(workflow, never()).escalate(any(), any(), any(), any(), any());
    }

    @Test
    void advisorApiAcceptsActiveLeafCategory() {
        when(categories.findAllActiveLeaf()).thenReturn(List.of(new CategoryView(
                11L, 2L, "Phòng đào tạo", null, "ACA", "Học vụ", null, 1, true)));
        var advisor = new ManagerOption(8L, "Người nhận", null, null, null);
        when(scopes.findAdvisors(11L)).thenReturn(List.of(advisor));

        assertEquals(List.of(advisor), controller.advisors(11L).getData());
        assertThrows(NotFoundException.class, () -> controller.advisors(12L));
    }

    private static void authenticate() {
        QauteUserDetails user = new QauteUserDetails(7L, "manager@example.com", "Manager",
                RoleCode.MANAGER, "test", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }
}
