package vn.edu.hcmute.qaute.controller.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.dto.request.student.ChangePasswordForm;
import vn.edu.hcmute.qaute.dto.view.AdminProfileView;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.service.admin.AdminProfileService;

@ExtendWith(MockitoExtension.class)
class AdminProfileControllerTest {
    @Mock AdminProfileService profiles;
    @InjectMocks AdminProfileController controller;

    @AfterEach
    void clearSecurity() { SecurityContextHolder.clearContext(); }

    @Test
    void profileRejectsStudentEvenWithoutSecurityFilter() {
        authenticate(RoleCode.STUDENT);
        assertThrows(ForbiddenException.class, () -> controller.profile(new ExtendedModelMap()));
        verifyNoInteractions(profiles);
    }

    @Test
    void selfPasswordSuccessUsesCurrentAdminAndClearsSubmittedSecrets() {
        authenticate(RoleCode.ADMIN);
        ChangePasswordForm form = passwordForm();
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();
        String view = controller.changePassword(form, new BeanPropertyBindingResult(form, "passwordForm"),
                new ExtendedModelMap(), redirect);
        assertEquals("redirect:/admin/profile", view);
        verify(profiles).changePassword(7L, form);
        assertNull(form.getCurrentPassword());
        assertNull(form.getNewPassword());
        assertNull(form.getConfirmPassword());
    }

    @Test
    void invalidPasswordDoesNotRetainRejectedPlaintextInRenderedModel() {
        authenticate(RoleCode.ADMIN);
        ChangePasswordForm form = passwordForm();
        form.setNewPassword("weak");
        BindingResult result = new BeanPropertyBindingResult(form, "passwordForm");
        result.addError(new FieldError("passwordForm", "newPassword", "weak", false, null, null,
                "Mật khẩu không đủ mạnh"));
        ExtendedModelMap model = new ExtendedModelMap();
        model.addAttribute("passwordForm", form);
        model.addAttribute(BindingResult.MODEL_KEY_PREFIX + "passwordForm", result);
        when(profiles.get(7L)).thenReturn(new AdminProfileView("Admin", "admin@example.com", null, null));

        assertEquals("admin/profile", controller.changePassword(form, result, model,
                new RedirectAttributesModelMap()));

        assertFalse(model.containsAttribute(BindingResult.MODEL_KEY_PREFIX + "passwordForm"));
        assertNull(((ChangePasswordForm) model.get("passwordForm")).getNewPassword());
        assertNull(form.getCurrentPassword());
        verify(profiles, never()).changePassword(eq(7L), any());
    }

    private static ChangePasswordForm passwordForm() {
        ChangePasswordForm form = new ChangePasswordForm();
        form.setCurrentPassword("Current@12345"); form.setNewPassword("Changed@12345");
        form.setConfirmPassword("Changed@12345");
        return form;
    }

    private static void authenticate(RoleCode role) {
        QauteUserDetails user = new QauteUserDetails(7L, "admin@example.com", "Admin", role, "test", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }
}
