package vn.edu.hcmute.qaute.service.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.dto.view.report.ReportRange;
import vn.edu.hcmute.qaute.dto.view.report.ReportScope;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.repository.report.ReportQueryRepository;
import vn.edu.hcmute.qaute.repository.report.ServiceReportQueryRepository;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.ticket.SlaService;

@ExtendWith(MockitoExtension.class)
class ServiceReportServiceTest {
    @Mock ServiceReportQueryRepository serviceQueries;
    @Mock ReportQueryRepository ticketQueries;
    @Mock ManagerScopeService scopes;
    @Mock DepartmentRepository departments;
    @Mock CategoryRepository categories;
    @Mock SlaService sla;
    @InjectMocks ServiceReportService service;
    private final ReportRange range = new ReportRange(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 7));

    @AfterEach
    void clearSecurity() { SecurityContextHolder.clearContext(); }

    @Test
    void managerCannotExpandChatScopeThroughRequest() {
        authenticate(9L, RoleCode.MANAGER);
        when(scopes.getScopeCategoryIds(9L)).thenReturn(List.of(2L, 3L));
        ReportScope allowed = new ReportScope(9L, Set.of(3L));
        when(serviceQueries.chatTotals(allowed, range)).thenReturn(new Object[]{10L, 90.0, 15.0, 2L});
        when(serviceQueries.chatsPerManager(allowed, range)).thenReturn(List.<Object[]>of(new Object[]{"A", 4L}));

        var result = service.chat(new ReportScope(88L, Set.of(3L, 4L)), range);

        assertEquals(20.0, result.abandonedPercent());
        assertEquals(90.0, result.avgWaitSeconds());
        verify(serviceQueries).chatTotals(allowed, range);
    }

    @Test
    void appointmentUtilizationUsesNonBlockedSlots() {
        authenticate(1L, RoleCode.ADMIN);
        ReportScope scope = new ReportScope(null, null);
        when(serviceQueries.appointmentTotals(scope, range)).thenReturn(new Object[]{10L, 5L, 2L, 1L});
        when(serviceQueries.slotTotals(range, null)).thenReturn(new long[]{20, 8});

        var result = service.appointment(scope, range);

        assertEquals(40.0, result.utilizationPercent());
        assertEquals(20.0, result.noShowPercent());
        assertEquals(10.0, result.cancelPercent());
    }

    @Test
    void knowledgeSeparatesSearchEventsFromClickEvents() {
        authenticate(1L, RoleCode.ADMIN);
        when(serviceQueries.feedback(range)).thenReturn(new long[]{10, 6});
        when(serviceQueries.searches(range)).thenReturn(new long[]{20, 5, 4});
        when(serviceQueries.deflection(range)).thenReturn(new long[]{4, 3});
        when(serviceQueries.topNoResultQueries(range)).thenReturn(List.<Object[]>of(new Object[]{"hoc phi", 5L}));
        when(serviceQueries.lowHelpfulFaqs()).thenReturn(List.<Object[]>of(new Object[]{"Câu hỏi", 2, 4}));
        when(serviceQueries.faqViews(range)).thenReturn(12L);

        var report = service.knowledge(range);

        assertEquals(12, report.faqViews());
        assertEquals(60.0, report.helpfulPercent());
        assertEquals(25.0, report.noResultPercent());
        assertEquals(20.0, report.searchToClickPercent());
        assertEquals(75.0, report.deflectionProxyPercent());
        assertEquals(6, report.lowHelpfulFaqs().get(0).count());
    }

    @Test
    void managerCannotReadSystemWideKnowledgeReport() {
        authenticate(9L, RoleCode.MANAGER);
        assertThrows(ForbiddenException.class, () -> service.knowledge(range));
    }

    private void authenticate(Long id, RoleCode role) {
        QauteUserDetails user = new QauteUserDetails(id, "user@example.com", "User", role, "test", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }
}
