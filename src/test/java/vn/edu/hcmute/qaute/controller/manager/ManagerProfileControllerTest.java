package vn.edu.hcmute.qaute.controller.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.dto.view.AuthSessionView;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;
import vn.edu.hcmute.qaute.service.identity.PasswordService;
import vn.edu.hcmute.qaute.service.manager.ManagerProfileService;

@ExtendWith(MockitoExtension.class)
class ManagerProfileControllerTest {
    @Mock ManagerProfileService profiles;
    @Mock PasswordService passwords;
    @Mock AuthSessionService sessions;
    @InjectMocks ManagerProfileController controller;

    @AfterEach
    void clearSecurity() { SecurityContextHolder.clearContext(); }

    @Test
    void statusChangesReturnToInternalManagerPage() {
        authenticate();
        assertEquals("redirect:/manager/faq", controller.status(AvailabilityStatus.BUSY,
                "/manager/faq", new RedirectAttributesModelMap()));
        verify(profiles).setStatus(7L, AvailabilityStatus.BUSY);
        assertEquals("redirect:/manager/queue", controller.status(AvailabilityStatus.ONLINE,
                "https://evil.example/manager/faq", new RedirectAttributesModelMap()));
    }

    @Test
    void currentSessionCannotBeRevokedFromSecurityPage() {
        authenticate();
        when(sessions.listSessions(7L, "test")).thenReturn(List.of(
                new AuthSessionView(10L, "Browser", "127.0.0.1", null, null,
                        false, false, true)));

        assertEquals("redirect:/manager/profile/security", controller.revokeSession(10L,
                new RedirectAttributesModelMap()));

        verify(sessions, never()).revokeSession(10L, 7L);
    }

    private static void authenticate() {
        QauteUserDetails user = new QauteUserDetails(7L, "manager@example.com", "Manager",
                RoleCode.MANAGER, "test", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }
}
