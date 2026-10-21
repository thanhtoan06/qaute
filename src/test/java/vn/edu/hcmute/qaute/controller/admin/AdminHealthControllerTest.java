package vn.edu.hcmute.qaute.controller.admin;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.service.admin.SystemHealthService;
import vn.edu.hcmute.qaute.service.governance.AuditService;

@ExtendWith(MockitoExtension.class)
class AdminHealthControllerTest {
    @Mock SystemHealthService health;
    @Mock AuditService audit;
    @InjectMocks AdminHealthController controller;

    @AfterEach
    void clearSecurity() { SecurityContextHolder.clearContext(); }

    @Test
    void testEmailUsesAuthenticatedAdminAndRecordsAudit() {
        QauteUserDetails admin = new QauteUserDetails(1L, "admin@example.com", "Admin", RoleCode.ADMIN, "test", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities()));

        controller.testEmail(new RedirectAttributesModelMap());

        verify(health).queueTestEmail("admin@example.com");
        verify(audit).log(eq(AuditActions.SYSTEM_TEST_EMAIL_QUEUED), eq("User"), eq(1L),
                isNull(), eq("testEmailQueued=true"));
    }
}
