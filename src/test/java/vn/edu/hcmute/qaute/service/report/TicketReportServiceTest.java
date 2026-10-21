package vn.edu.hcmute.qaute.service.report;

import org.junit.jupiter.api.AfterEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.dto.view.report.ReportRange;
import vn.edu.hcmute.qaute.dto.view.report.ReportScope;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.report.ReportQueryRepository;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.ticket.SlaService;

@ExtendWith(MockitoExtension.class)
class TicketReportServiceTest {
    @Mock ReportQueryRepository queries;
    @Mock SlaService sla;
    @Mock ManagerScopeService scopes;
    @Mock CategoryQueryService categories;
    @InjectMocks TicketReportService service;

    @AfterEach
    void clearSecurity() { SecurityContextHolder.clearContext(); }

    @Test
    void calculatesSamplePercentilesAndFillsEmptyDays() {
        authenticate(1L, RoleCode.ADMIN);
        ReportScope scope = new ReportScope(9L, Set.of(3L));
        ReportRange range = new ReportRange(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 3));
        LocalDateTime created = range.from().atStartOfDay();
        Ticket a = ticket(created, 30, 60, 0);
        Ticket b = ticket(created, 60, 120, 60);
        Ticket c = ticket(created, 120, 180, 0);
        when(queries.createdPerDay(scope, range)).thenReturn(List.<Object[]>of(new Object[]{range.from(), 3L}));
        when(queries.createdByCategory(scope, range)).thenReturn(List.of());
        when(queries.createdByStatus(scope, range)).thenReturn(List.of());
        when(queries.latestCreated(scope, range, 5000)).thenReturn(List.of(a, b, c));
        when(sla.businessMinutesBetween(any(), any())).thenAnswer(invocation -> Duration.between(
                invocation.getArgument(0, LocalDateTime.class), invocation.getArgument(1, LocalDateTime.class)).toMinutes());
        when(queries.slaCounts(scope, range)).thenReturn(new long[]{3, 2});
        when(queries.backlog(org.mockito.ArgumentMatchers.eq(scope), any())).thenReturn(new long[]{5, 2, 2, 1});
        when(queries.resolvedAndReopened(scope, range)).thenReturn(new long[]{4, 1});
        when(queries.reviews(scope, range)).thenReturn(List.<Object[]>of(
                new Object[]{5, "FAST, CLEAR"}, new Object[]{3, "FAST"}, new Object[]{4, "CLEAR"}));
        when(queries.managerCandidates()).thenReturn(List.<Object[]>of(new Object[]{9L, "Cố vấn", 10}));
        when(queries.managerWorkload(9L, scope, range)).thenReturn(new long[]{2, 3});

        var report = service.build(scope, range);

        assertEquals(List.of(3L, 0L, 0L), report.perDay().stream().map(item -> item.count()).toList());
        assertEquals(3, report.firstResponse().samples());
        assertEquals((30 + 59 + 120) / 3.0, report.firstResponse().avgMinutes(), 0.0001);
        assertEquals(59, report.firstResponse().medianMinutes());
        assertEquals(120, report.firstResponse().p90Minutes());
        assertEquals(25, report.reopenRate());
        assertArrayEquals(new long[]{0, 0, 1, 1, 1}, report.csat().distribution());
        assertEquals(2, report.workload().get(0).open());
        assertEquals(3, report.workload().get(0).resolvedInRange());
    }

    @Test
    void managerCannotRequestGlobalOrAnotherManagersTicketReport() {
        authenticate(9L, RoleCode.MANAGER);
        when(scopes.getScopeCategoryIds(9L)).thenReturn(List.of(2L, 3L));
        ReportScope allowed = new ReportScope(9L, Set.of(3L));
        ReportRange range = new ReportRange(LocalDate.now().minusDays(2), LocalDate.now());
        when(queries.slaCounts(allowed, range)).thenReturn(new long[]{0, 0});
        when(queries.backlog(org.mockito.ArgumentMatchers.eq(allowed), any())).thenReturn(new long[]{0, 0, 0, 0});
        when(queries.resolvedAndReopened(allowed, range)).thenReturn(new long[]{0, 0});
        service.build(new ReportScope(99L, Set.of(3L, 88L)), range);
        verify(queries).createdPerDay(allowed, range);
        verify(queries).latestCreated(allowed, range, 5000);
    }

    @Test
    void studentCannotCallTicketReportsDirectly() {
        authenticate(9L, RoleCode.STUDENT);
        assertThrows(ForbiddenException.class, () -> service.build(new ReportScope(null, null),
                new ReportRange(LocalDate.now(), LocalDate.now())));
        verifyNoInteractions(queries);
    }

    private void authenticate(Long id, RoleCode role) {
        QauteUserDetails user = new QauteUserDetails(id, "user@example.com", "User", role, "test", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    private Ticket ticket(LocalDateTime created, int responseMinutes, int resolutionMinutes, long pausedSeconds) {
        Ticket ticket = new Ticket();
        ticket.setCreatedAt(created);
        ticket.setFirstResponseAt(created.plusMinutes(responseMinutes));
        ticket.setResolvedAt(created.plusMinutes(resolutionMinutes));
        ticket.setSlaPausedSeconds(pausedSeconds);
        ticket.setStatus(TicketStatus.RESOLVED);
        return ticket;
    }
}
