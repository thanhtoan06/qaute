package vn.edu.hcmute.qaute.controller.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.dto.view.report.KnowledgeReport;
import vn.edu.hcmute.qaute.dto.view.report.NamedCount;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.repository.report.ReportQueryRepository;
import vn.edu.hcmute.qaute.repository.report.ServiceReportQueryRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.report.ServiceReportService;
import vn.edu.hcmute.qaute.service.report.TicketReportService;

@ExtendWith(MockitoExtension.class)
class AdminReportControllerTest {
    @Mock TicketReportService tickets;
    @Mock ServiceReportService services;
    @Mock ReportQueryRepository ticketQueries;
    @Mock ServiceReportQueryRepository serviceQueries;
    @Mock DepartmentRepository departments;
    @Mock CategoryRepository categories;
    @Mock CategoryQueryService categoryPaths;
    @Mock AuditService audit;
    @InjectMocks AdminReportController controller;

    @Test
    void apiRejectsNonAdminBeforeReadingReports() {
        assertThrows(ForbiddenException.class, () -> controller.data("30", null, null, null, null));
        verifyNoInteractions(tickets, services, ticketQueries, serviceQueries);
    }

    @Test
    void csvExportHasBomEscapesSpreadsheetFormulaAndAudits() {
        var range = new vn.edu.hcmute.qaute.dto.view.report.ReportRange(
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2));
        when(services.knowledge(range)).thenReturn(new KnowledgeReport(0, 0, 0, 0,
                List.of(new NamedCount("=HYPERLINK(\"https://example.test\")", 2)), List.of(), 0));

        var response = controller.export("noresult", "2026-10-01", "2026-10-02", null, null);
        String csv = new String(response.getBody(), StandardCharsets.UTF_8);

        assertTrue(csv.startsWith("\uFEFF"));
        assertTrue(csv.contains("\"'=HYPERLINK(\"\"https://example.test\"\")\""));
        assertEquals("bao-cao-noresult-2026-10-01-2026-10-02.csv",
                response.getHeaders().getContentDisposition().getFilename());
        verify(audit).log(eq(AuditActions.REPORT_EXPORT), eq("report"), eq(null), eq(null), any());
    }

    @Test
    void rejectsDateRangesLongerThanOneYear() {
        assertThrows(BadRequestException.class, () ->
                controller.export("noresult", "2025-01-01", "2026-01-02", null, null));
        verifyNoInteractions(services, audit);
    }
}
