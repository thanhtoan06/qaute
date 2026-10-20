package vn.edu.hcmute.qaute.service.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.hcmute.qaute.dto.view.report.KnowledgeReport;
import vn.edu.hcmute.qaute.dto.view.report.ReportRange;
import vn.edu.hcmute.qaute.dto.view.report.ReportScope;
import vn.edu.hcmute.qaute.dto.view.report.TicketReport;
import vn.edu.hcmute.qaute.dto.view.CategoryView;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.governance.AuditLogRepository;
import vn.edu.hcmute.qaute.repository.report.DashboardQueryRepository;
import vn.edu.hcmute.qaute.repository.report.ReportQueryRepository;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.report.ServiceReportService;
import vn.edu.hcmute.qaute.service.report.TicketReportService;
import vn.edu.hcmute.qaute.service.ticket.SlaService;

@ExtendWith(MockitoExtension.class)
class AdminDashboardServiceTest {
    @Mock DashboardQueryRepository queries;
    @Mock TicketReportService tickets;
    @Mock ServiceReportService services;
    @Mock ReportQueryRepository ticketQueries;
    @Mock CategoryQueryService categories;
    @Mock ManagerScopeService scopes;
    @Mock SlaService sla;
    @Mock AuditLogRepository auditLogs;
    @InjectMocks AdminDashboardService service;

    @Test
    void alertsOnUncoveredLeafAndCalculatesWeeklyBusinessTime() {
        TicketReport report = mock(TicketReport.class, RETURNS_DEEP_STUBS);
        when(tickets.build(any(ReportScope.class), any(ReportRange.class))).thenReturn(report);
        when(report.perDay()).thenReturn(List.of());
        when(report.sla().percent()).thenReturn(91.0);
        when(report.csat().avg()).thenReturn(4.2);
        when(services.knowledge(any())).thenReturn(new KnowledgeReport(0, 0, 0, 0,
                List.of(), List.of(), 0));
        when(services.compareDepartments(any())).thenReturn(List.of());
        CategoryView category = new CategoryView(15L, 1L, "Phòng đào tạo", null,
                "HOC_VU", "Học vụ", null, 1, true);
        when(categories.findAllActiveLeaf()).thenReturn(List.of(category));
        when(scopes.findManagerIdsInScope(15L)).thenReturn(List.of());
        when(queries.breachedAlerts(8)).thenReturn(List.of());
        when(queries.lowRatings(any(), anyInt())).thenReturn(List.of());
        when(auditLogs.findAll(any(Pageable.class))).thenReturn(Page.empty());
        Ticket ticket = new Ticket();
        ticket.setCreatedAt(LocalDateTime.of(2026, 10, 19, 8, 0));
        ticket.setFirstResponseAt(ticket.getCreatedAt().plusHours(2));
        when(ticketQueries.latestCreated(any(), any(), anyInt())).thenReturn(List.of(ticket));
        when(sla.businessMinutesBetween(any(), any())).thenReturn(90L);

        var dashboard = service.get();

        assertEquals(1, dashboard.uncoveredCategories().size());
        assertEquals("Học vụ", dashboard.uncoveredCategories().get(0).title());
        assertEquals(90, dashboard.frtWeekly().get(0).minutes());
        assertEquals(91, dashboard.slaPercent());
    }
}
