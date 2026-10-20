package vn.edu.hcmute.qaute.service.admin;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.dto.view.report.DailyCount;
import vn.edu.hcmute.qaute.dto.view.report.DepartmentCompare;
import vn.edu.hcmute.qaute.dto.view.report.NamedCount;
import vn.edu.hcmute.qaute.dto.view.report.ReportRange;
import vn.edu.hcmute.qaute.dto.view.report.ReportScope;
import vn.edu.hcmute.qaute.repository.governance.AuditLogRepository;
import vn.edu.hcmute.qaute.repository.report.DashboardQueryRepository;
import vn.edu.hcmute.qaute.repository.report.ReportQueryRepository;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.report.ServiceReportService;
import vn.edu.hcmute.qaute.service.report.TicketReportService;
import vn.edu.hcmute.qaute.service.ticket.SlaService;

@Service
@Transactional(readOnly = true)
public class AdminDashboardService {
    public record Alert(String title, String detail, String url) {}
    public record WeeklyFrt(LocalDate weekStart, double minutes, long count) {}
    public record Activity(String action, LocalDateTime at) {}
    public record Dashboard(long ticketsToday, long ticketsMonth, long unassigned, long backlogOver24h,
            double slaPercent, long slaSamples, double csat, long csatSamples,
            long activeChats, long waitingChats,
            long appointmentsToday, long activeUsers7d, List<DailyCount> daily30,
            List<DepartmentCompare> departments, List<WeeklyFrt> frtWeekly,
            List<Alert> slaAlerts, List<Alert> uncoveredCategories,
            List<NamedCount> lowHelpfulFaqs, List<NamedCount> noResultQueries,
            List<Alert> lowRatings, long failedEmails, List<Activity> recentActivity) {}

    private final DashboardQueryRepository queries;
    private final TicketReportService tickets;
    private final ServiceReportService services;
    private final ReportQueryRepository ticketQueries;
    private final CategoryQueryService categories;
    private final ManagerScopeService scopes;
    private final SlaService sla;
    private final AuditLogRepository auditLogs;

    public AdminDashboardService(DashboardQueryRepository queries, TicketReportService tickets,
            ServiceReportService services, ReportQueryRepository ticketQueries,
            CategoryQueryService categories, ManagerScopeService scopes, SlaService sla,
            AuditLogRepository auditLogs) {
        this.queries = queries; this.tickets = tickets; this.services = services;
        this.ticketQueries = ticketQueries; this.categories = categories; this.scopes = scopes;
        this.sla = sla; this.auditLogs = auditLogs;
    }

    public Dashboard get() {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = today.atStartOfDay();
        ReportRange range = new ReportRange(today.minusDays(29), today);
        ReportScope all = new ReportScope(null, null);
        var report = tickets.build(all, range);
        var knowledge = services.knowledge(range);
        List<Alert> uncovered = new ArrayList<>();
        for (var category : categories.findAllActiveLeaf()) {
            if (scopes.findManagerIdsInScope(category.id()).isEmpty())
                uncovered.add(new Alert(category.name(), "Chưa có Manager ACTIVE phụ trách",
                        "/admin/categories"));
        }
        List<Alert> breached = queries.breachedAlerts(8).stream()
                .map(ticket -> new Alert(ticket.getCode(), ticket.getTitle(),
                        "/admin/tickets/" + ticket.getId())).toList();
        List<Alert> lowRatings = queries.lowRatings(now.minusDays(30), 8).stream()
                .map(row -> new Alert(String.valueOf(row[1]) + " · " + row[2] + "/5",
                        row[3] == null ? "Đánh giá thấp" : String.valueOf(row[3]),
                        "/admin/tickets/" + row[0])).toList();
        List<Activity> activity = auditLogs.findAll(PageRequest.of(0, 5,
                Sort.by(Sort.Direction.DESC, "createdAt"))).stream()
                .map(log -> new Activity(log.getAction(), log.getCreatedAt())).toList();
        return new Dashboard(queries.ticketsCreated(start, start.plusDays(1)),
                queries.ticketsCreated(today.withDayOfMonth(1).atStartOfDay(), start.plusDays(1)),
                queries.unassigned(), queries.oldBacklog(now.minusHours(24)), report.sla().percent(),
                report.sla().total(), report.csat().avg(), report.csat().count(),
                queries.chatCount(ChatSessionStatus.ACTIVE),
                queries.chatCount(ChatSessionStatus.WAITING), queries.appointmentsToday(start, start.plusDays(1)),
                queries.recentlyActiveUsers(now.minusDays(7)), report.perDay(),
                services.compareDepartments(range), weeklyFrt(all, range), breached, uncovered,
                knowledge.lowHelpfulFaqs(), knowledge.topNoResultQueries(), lowRatings,
                queries.failedEmails(), activity);
    }

    private List<WeeklyFrt> weeklyFrt(ReportScope scope, ReportRange range) {
        Map<LocalDate, double[]> weeks = new LinkedHashMap<>();
        for (var ticket : ticketQueries.latestCreated(scope, range, 5000)) {
            if (ticket.getFirstResponseAt() == null) continue;
            LocalDate monday = ticket.getCreatedAt().toLocalDate()
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            double[] aggregate = weeks.computeIfAbsent(monday, ignored -> new double[2]);
            aggregate[0] += Math.max(0, sla.businessMinutesBetween(ticket.getCreatedAt(),
                    ticket.getFirstResponseAt()) - ticket.getSlaPausedSeconds() / 60.0);
            aggregate[1]++;
        }
        return weeks.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(entry -> new WeeklyFrt(entry.getKey(), entry.getValue()[0] / entry.getValue()[1],
                        (long) entry.getValue()[1])).toList();
    }
}
