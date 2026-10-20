package vn.edu.hcmute.qaute.controller.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.dto.view.report.ReportRange;
import vn.edu.hcmute.qaute.dto.view.report.ReportScope;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.report.ServiceReportService;
import vn.edu.hcmute.qaute.service.report.TicketReportService;

@ExtendWith(MockitoExtension.class)
class ManagerReportControllerTest {
    @Mock TicketReportService tickets;
    @Mock ServiceReportService services;
    @Mock ManagerScopeService scopes;
    @Mock CategoryQueryService categories;
    @InjectMocks ManagerReportController controller;

    @BeforeEach
    void login() {
        QauteUserDetails user = new QauteUserDetails(9L, "manager@example.com", "Manager",
                RoleCode.MANAGER, "test", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    @AfterEach
    void logout() { SecurityContextHolder.clearContext(); }

    @Test
    void mineModeUsesAssignedTicketsOnly() {
        when(scopes.getScopeCategoryIds(9L)).thenReturn(List.of(2L, 3L));
        LocalDate today = LocalDate.now();
        ReportRange range = new ReportRange(today.minusDays(29), today);
        ReportScope scope = new ReportScope(9L, Set.of());

        controller.data("30", null, null, null, true);

        verify(tickets).build(scope, range);
        verify(services).chat(scope, range);
        verify(services).appointment(scope, range);
    }

    @Test
    void rejectsCategoryOutsideManagerScope() {
        when(scopes.getScopeCategoryIds(9L)).thenReturn(List.of(2L));
        assertThrows(BadRequestException.class, () -> controller.data("30", null, null, 4L, false));
    }

    @Test
    void rejectsCustomRangeLongerThan366Days() {
        when(scopes.getScopeCategoryIds(9L)).thenReturn(List.of());
        assertThrows(BadRequestException.class,
                () -> controller.data("custom", "2025-01-01", "2026-01-02", null, false));
    }

    @Test
    void acceptsExactly366Days() {
        when(scopes.getScopeCategoryIds(9L)).thenReturn(List.of(2L));
        controller.data("custom", "2025-01-01", "2026-01-01", 2L, false);
        verify(tickets).build(new ReportScope(9L, Set.of(2L)),
                new ReportRange(LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1)));
    }
}
