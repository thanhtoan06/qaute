package vn.edu.hcmute.qaute.dto.view.report;

import java.util.List;
public record TicketReport(List<DailyCount> perDay, List<NamedCount> byCategory, List<NamedCount> byStatus, TimeStats firstResponse, TimeStats resolution, SlaStats sla, BacklogStats backlog, double reopenRate, CsatStats csat, List<WorkloadItem> workload) {}
