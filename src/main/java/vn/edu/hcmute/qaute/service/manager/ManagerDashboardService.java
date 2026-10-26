package vn.edu.hcmute.qaute.service.manager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.dto.view.report.DailyCount;
import vn.edu.hcmute.qaute.dto.view.report.NamedCount;
import vn.edu.hcmute.qaute.dto.view.report.ReportRange;
import vn.edu.hcmute.qaute.dto.view.report.ReportScope;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.repository.report.DashboardQueryRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.chat.ChatDeskService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.report.TicketReportService;

// Xử lý nghiệp vụ ManagerDashboard trong hệ thống.
@Service
@Transactional(readOnly = true)
public class ManagerDashboardService {
    public record Task(String kind, String title, String detail, String url) {}
    public record Dashboard(long unassigned, long mine, long waitingStudent, long slaRisk,
            long appointmentsToday, long waitingChats, double csat30, long csatSamples,
            double medianFrt30, long frtSamples,
            List<DailyCount> daily14, List<NamedCount> byCategory, List<Task> urgent) {}

    private final ManagerQueueService queue;
    private final ManagerScopeService scopes;
    private final TicketReportService reports;
    private final ChatDeskService chats;
    private final ManagerAppointmentService appointments;
    private final DashboardQueryRepository queries;
    private final TicketRepository tickets;

    // Khởi tạo dịch vụ tổng quan của tư vấn viên.
    public ManagerDashboardService(ManagerQueueService queue, ManagerScopeService scopes,
            TicketReportService reports, ChatDeskService chats, ManagerAppointmentService appointments,
            DashboardQueryRepository queries, TicketRepository tickets) {
        this.queue = queue; this.scopes = scopes; this.reports = reports; this.chats = chats;
        this.appointments = appointments; this.queries = queries;
        this.tickets = tickets;
    }

    // Tổng hợp số liệu và công việc cần chú ý của tư vấn viên.
    public Dashboard get(Long managerId) {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        List<Long> categoryIds = scopes.getScopeCategoryIds(managerId);
        ReportScope scope = new ReportScope(managerId, java.util.Set.copyOf(categoryIds));
        var month = reports.build(scope, new ReportRange(today.minusDays(29), today));
        var fortnight = reports.build(scope, new ReportRange(today.minusDays(13), today));
        Map<String, Long> counts = queue.tabCounts(managerId);
        List<Task> tasks = new ArrayList<>();
        for (Ticket ticket : queries.managerRisk(managerId, 5))
            tasks.add(new Task("SLA", ticket.getCode(), ticket.getTitle(), "/manager/tickets/" + ticket.getId()));
        for (Ticket ticket : queries.oldUnassignedInScope(categoryIds, now.minusHours(24), 5))
            tasks.add(new Task("Chưa nhận > 24 giờ", ticket.getCode(), ticket.getTitle(),
                    "/manager/tickets/" + ticket.getId()));
        queries.nextAppointments(managerId, now, 3).forEach(item -> {
            var view = appointments.detail(item.getId(), managerId);
            tasks.add(new Task("Lịch hẹn", view.topic(), view.startAt().toString(),
                    "/manager/appointments/" + view.id()));
        });
        return new Dashboard(counts.getOrDefault("unassigned", 0L),
                tickets.countByCurrentAssigneeIdAndStatusIn(managerId, List.of(TicketStatus.ASSIGNED,
                        TicketStatus.IN_PROGRESS, TicketStatus.WAITING_STUDENT)),
                counts.getOrDefault("waiting", 0L), counts.getOrDefault("slarisk", 0L),
                appointments.list(managerId, "today", PageRequest.of(0, 1)).getTotalElements(),
                chats.queueCount(managerId), month.csat().avg(), month.csat().count(),
                month.firstResponse().medianMinutes(), month.firstResponse().samples(),
                fortnight.perDay(), fortnight.byCategory(), tasks);
    }
}
