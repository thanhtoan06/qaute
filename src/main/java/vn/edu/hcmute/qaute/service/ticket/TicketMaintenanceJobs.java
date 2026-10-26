package vn.edu.hcmute.qaute.service.ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.common.constant.SlaStatus;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.job.JobExecutionTemplate;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.ticket.event.TicketEventPublisher;
import vn.edu.hcmute.qaute.service.ticket.event.TicketEventType;

// Xử lý nghiệp vụ TicketMaintenanceJobs trong hệ thống.
@Component
public class TicketMaintenanceJobs {

    private static final Set<TicketStatus> ACTIVE_STATUSES = Set.of(
            TicketStatus.NEW, TicketStatus.ASSIGNED, TicketStatus.IN_PROGRESS);
    private static final int BATCH_SIZE = 100;

    private final JobExecutionTemplate jobs;
    private final TicketWorkflowService workflow;
    private final TicketRepository tickets;
    private final SlaService sla;
    private final TicketEventPublisher events;
    private final Set<String> publishedSlaLevels = ConcurrentHashMap.newKeySet();

    // Khởi tạo các tác vụ bảo trì ticket với dịch vụ và kho dữ liệu cần thiết.
    public TicketMaintenanceJobs(JobExecutionTemplate jobs,
                                 TicketWorkflowService workflow,
                                 TicketRepository tickets,
                                 SlaService sla,
                                 TicketEventPublisher events) {
        this.jobs = jobs;
        this.workflow = workflow;
        this.tickets = tickets;
        this.sla = sla;
        this.events = events;
    }

    @Scheduled(fixedDelay = 900000)
    // Chạy tác vụ tự động đóng các ticket quá hạn.
    public void autoClose() {
        jobs.run("ticket-auto-close", workflow::autoCloseExpired);
    }

    @Scheduled(fixedDelay = 60000)
    // Cập nhật trạng thái SLA và phát sự kiện khi ticket gặp rủi ro hoặc vi phạm.
    public void slaMonitor() {
        jobs.run("ticket-sla-monitor", () -> {
            LocalDateTime now = LocalDateTime.now();
            int page = 0;
            while (true) {
                List<Ticket> batch = tickets.findActiveSlaTickets(ACTIVE_STATUSES, PageRequest.of(page, BATCH_SIZE));
                if (batch.isEmpty()) {
                    break;
                }
                for (Ticket ticket : batch) {
                    try {
                        SlaStatus newStatus = sla.evaluate(ticket, now);
                        if (newStatus != null && newStatus != ticket.getSlaStatus()) {
                            ticket.setSlaStatus(newStatus);
                            tickets.save(ticket);
                            if (newStatus == SlaStatus.AT_RISK && publishedSlaLevels.add(ticket.getId() + ":AT_RISK")) {
                                events.publish(TicketEventType.SLA_AT_RISK, ticket, null, Map.of());
                            } else if (newStatus == SlaStatus.BREACHED && publishedSlaLevels.add(ticket.getId() + ":BREACHED")) {
                                events.publish(TicketEventType.SLA_BREACHED, ticket, null, Map.of());
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }
                page++;
            }
        });
    }
}
