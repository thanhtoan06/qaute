package vn.edu.hcmute.qaute.service.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.dto.view.report.ReportRange;
import vn.edu.hcmute.qaute.dto.view.report.ReportScope;
import vn.edu.hcmute.qaute.dto.view.report.TicketReport;
import vn.edu.hcmute.qaute.repository.report.DashboardQueryRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.chat.ChatDeskService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.report.TicketReportService;

@ExtendWith(MockitoExtension.class)
class ManagerDashboardServiceTest {
    @Mock ManagerQueueService queue;
    @Mock ManagerScopeService scopes;
    @Mock TicketReportService reports;
    @Mock ChatDeskService chats;
    @Mock ManagerAppointmentService appointments;
    @Mock DashboardQueryRepository queries;
    @Mock TicketRepository tickets;
    @InjectMocks ManagerDashboardService service;

    @Test
    void dashboardKeepsReportInManagersScopeAndCountsOnlyOpenAssignedTickets() {
        TicketReport report = mock(TicketReport.class, RETURNS_DEEP_STUBS);
        when(scopes.getScopeCategoryIds(7L)).thenReturn(List.of(11L, 12L));
        when(reports.build(any(), any())).thenReturn(report);
        when(report.perDay()).thenReturn(List.of());
        when(report.byCategory()).thenReturn(List.of());
        when(queue.tabCounts(7L)).thenReturn(Map.of("unassigned", 3L, "waiting", 2L, "slarisk", 1L));
        when(tickets.countByCurrentAssigneeIdAndStatusIn(eq(7L), any())).thenReturn(4L);
        when(queries.managerRisk(eq(7L), anyInt())).thenReturn(List.of());
        when(queries.oldUnassignedInScope(eq(List.of(11L, 12L)), any(), eq(5))).thenReturn(List.of());
        when(queries.nextAppointments(eq(7L), any(), eq(3))).thenReturn(List.of());
        when(appointments.list(eq(7L), eq("today"), any(Pageable.class))).thenReturn(Page.empty());
        when(chats.queueCount(7L)).thenReturn(101);

        var dashboard = service.get(7L);

        assertEquals(101, dashboard.waitingChats());
        assertEquals(3, dashboard.unassigned());
        assertEquals(4, dashboard.mine());
        assertEquals(2, dashboard.waitingStudent());
        ArgumentCaptor<ReportScope> scope = ArgumentCaptor.forClass(ReportScope.class);
        verify(reports, org.mockito.Mockito.times(2)).build(scope.capture(), any(ReportRange.class));
        assertEquals(Set.of(11L, 12L), scope.getAllValues().get(0).categoryIds());
        assertEquals(7L, scope.getAllValues().get(0).managerId());
        verify(tickets).countByCurrentAssigneeIdAndStatusIn(7L,
                List.of(TicketStatus.ASSIGNED, TicketStatus.IN_PROGRESS, TicketStatus.WAITING_STUDENT));
    }
}
