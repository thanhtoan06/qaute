package vn.edu.hcmute.qaute.controller.manager;

import vn.edu.hcmute.qaute.dto.request.admin.TagNameForm;
import org.springframework.validation.BeanPropertyBindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.dto.view.TicketDraftView;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.service.manager.TicketNoteService;

@ExtendWith(MockitoExtension.class)
class ManagerTicketNoteControllerTest {
    @Mock TicketNoteService service;
    @InjectMocks ManagerTicketNoteController controller;

    @AfterEach
    void clearSecurity() { SecurityContextHolder.clearContext(); }

    @Test
    void draftApiReadsOnlyAuthenticatedManagersDraft() {
        authenticate();
        when(service.getDraft(42L, 7L)).thenReturn(new TicketDraftView("<p>Nháp</p>", null));

        assertEquals("<p>Nháp</p>", controller.getDraft(42L).getData().contentHtml());
        verify(service).getDraft(42L, 7L);
    }

    @Test
    void pinReturnsToNotesTicket() {
        authenticate();
        when(service.togglePin(5L, 7L)).thenReturn(42L);

        assertEquals("redirect:/manager/tickets/42",
                controller.pin(5L, new RedirectAttributesModelMap()));
    }

    @Test
    void tagValidationErrorReturnsToTicket() {
        authenticate();
        org.mockito.Mockito.doThrow(new BadRequestException("Tối đa 6 tag"))
                .when(service).addTag(42L, 7L, "quá nhiều");
        var flash = new RedirectAttributesModelMap();

        TagNameForm form = new TagNameForm(); form.setName("quá nhiều");
        assertEquals("redirect:/manager/tickets/42", controller.addTag(42L, form, new BeanPropertyBindingResult(form, "form"), flash));
        assertEquals("Tối đa 6 tag", flash.getFlashAttributes().get("flashError"));
    }

    private static void authenticate() {
        QauteUserDetails user = new QauteUserDetails(7L, "manager@example.com", "Manager",
                RoleCode.MANAGER, "test", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }
}
