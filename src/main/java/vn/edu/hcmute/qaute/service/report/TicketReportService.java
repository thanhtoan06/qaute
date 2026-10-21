package vn.edu.hcmute.qaute.service.report;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.dto.view.report.BacklogStats;
import vn.edu.hcmute.qaute.dto.view.report.CsatStats;
import vn.edu.hcmute.qaute.dto.view.report.DailyCount;
import vn.edu.hcmute.qaute.dto.view.report.NamedCount;
import vn.edu.hcmute.qaute.dto.view.report.ReportRange;
import vn.edu.hcmute.qaute.dto.view.report.ReportScope;
import vn.edu.hcmute.qaute.dto.view.report.SlaStats;
import vn.edu.hcmute.qaute.dto.view.report.TicketReport;
import vn.edu.hcmute.qaute.dto.view.report.TimeStats;
import vn.edu.hcmute.qaute.dto.view.report.WorkloadItem;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.report.ReportQueryRepository;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.ticket.SlaService;

@Service
@Transactional(readOnly = true)
public class TicketReportService {
    private static final int TIME_SAMPLE_LIMIT = 5000;
    private final ReportQueryRepository queries;
    private final SlaService sla;
    private final ManagerScopeService scopes;
    private final CategoryQueryService categories;

    public TicketReportService(ReportQueryRepository queries, SlaService sla,
                               ManagerScopeService scopes, CategoryQueryService categories) {
        this.queries = queries;
        this.sla = sla;
        this.scopes = scopes;
        this.categories = categories;
    }

    public TicketReport build(ReportScope scope, ReportRange range) {
        if (scope == null || range == null || range.from() == null || range.to() == null
                || range.from().isAfter(range.to())
                || java.time.temporal.ChronoUnit.DAYS.between(range.from(), range.to()) > 365) {
            throw new BadRequestException("Khoảng báo cáo không hợp lệ");
        }
        scope = effectiveScope(scope);
        List<DailyCount> perDay = daily(queries.createdPerDay(scope, range), range);
        List<NamedCount> byCategory = queries.createdByCategory(scope, range).stream()
                .map(row -> new NamedCount(categories.getDisplayPath(((Number) row[0]).longValue()), number(row[1])))
                .toList();
        List<NamedCount> byStatus = queries.createdByStatus(scope, range).stream()
                .map(row -> new NamedCount(((TicketStatus) row[0]).getLabel(), number(row[1]))).toList();

        List<Double> firstResponse = new ArrayList<>();
        List<Double> resolution = new ArrayList<>();
        for (Ticket ticket : queries.latestCreated(scope, range, TIME_SAMPLE_LIMIT)) {
            if (ticket.getFirstResponseAt() != null)
                firstResponse.add(minutes(ticket, ticket.getFirstResponseAt()));
            if (ticket.getResolvedAt() != null)
                resolution.add(minutes(ticket, ticket.getResolvedAt()));
        }
        long[] slaCounts = queries.slaCounts(scope, range);
        long[] backlog = queries.backlog(scope, LocalDateTime.now());
        long[] resolved = queries.resolvedAndReopened(scope, range);
        return new TicketReport(perDay, byCategory, byStatus, stats(firstResponse), stats(resolution),
                new SlaStats(slaCounts[0], slaCounts[1], percent(slaCounts[1], slaCounts[0])),
                new BacklogStats(backlog[0], backlog[1], backlog[2], backlog[3]),
                percent(resolved[1], resolved[0]), csat(queries.reviews(scope, range)), workload(scope, range));
    }

    private ReportScope effectiveScope(ReportScope requested) {
        if (SecurityUtils.hasRole(RoleCode.ADMIN)) return requested;
        if (!SecurityUtils.hasRole(RoleCode.MANAGER)) throw new ForbiddenException();
        Long managerId = SecurityUtils.requireUserId();
        Set<Long> allowed = new HashSet<>(scopes.getScopeCategoryIds(managerId));
        if (requested.categoryIds() != null) allowed.retainAll(requested.categoryIds());
        return new ReportScope(managerId, allowed);
    }

    private List<DailyCount> daily(List<Object[]> rows, ReportRange range) {
        Map<LocalDate, Long> counts = new HashMap<>();
        for (Object[] row : rows) counts.put(day(row[0]), number(row[1]));
        List<DailyCount> result = new ArrayList<>();
        for (LocalDate date = range.from(); !date.isAfter(range.to()); date = date.plusDays(1))
            result.add(new DailyCount(date, counts.getOrDefault(date, 0L)));
        return result;
    }

    private LocalDate day(Object value) {
        if (value instanceof LocalDate date) return date;
        if (value instanceof Date date) return date.toLocalDate();
        if (value instanceof java.util.Date date) return new Date(date.getTime()).toLocalDate();
        return LocalDate.parse(value.toString());
    }

    private double minutes(Ticket ticket, LocalDateTime end) {
        return Math.max(0, sla.businessMinutesBetween(ticket.getCreatedAt(), end)
                - ticket.getSlaPausedSeconds() / 60.0);
    }

    private TimeStats stats(List<Double> values) {
        if (values.isEmpty()) return new TimeStats(0, 0, 0, 0);
        values.sort(Double::compareTo);
        int n = values.size();
        double median = n % 2 == 0 ? (values.get(n / 2 - 1) + values.get(n / 2)) / 2.0 : values.get(n / 2);
        return new TimeStats(n, values.stream().mapToDouble(Double::doubleValue).average().orElse(0),
                median, values.get((int) Math.ceil(n * 0.9) - 1));
    }

    private CsatStats csat(List<Object[]> reviews) {
        long[] distribution = new long[5];
        Map<String, Long> reasons = new HashMap<>();
        long total = 0;
        long satisfied = 0;
        long sum = 0;
        for (Object[] row : reviews) {
            int rating = ((Number) row[0]).intValue();
            if (rating < 1 || rating > 5) continue;
            total++; sum += rating; distribution[rating - 1]++;
            if (rating >= 4) satisfied++;
            if (row[1] instanceof String csv && !csv.isBlank()) {
                Set<String> unique = new HashSet<>();
                Arrays.stream(csv.split(",")).map(String::strip).filter(s -> !s.isEmpty()).forEach(unique::add);
                unique.forEach(reason -> reasons.merge(reason, 1L, Long::sum));
            }
        }
        List<NamedCount> top = reasons.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry::getKey))
                .limit(10).map(entry -> new NamedCount(entry.getKey(), entry.getValue())).toList();
        return new CsatStats(total, total == 0 ? 0 : (double) sum / total,
                percent(satisfied, total), distribution, top);
    }

    private List<WorkloadItem> workload(ReportScope scope, ReportRange range) {
        if (scope.managerId() == null && scope.categoryIds() != null && scope.categoryIds().isEmpty()) return List.of();
        List<WorkloadItem> result = new ArrayList<>();
        for (Object[] row : queries.managerCandidates()) {
            Long managerId = ((Number) row[0]).longValue();
            boolean self = managerId.equals(scope.managerId());
            if (!self && scope.categoryIds() != null
                    && scopes.getScopeCategoryIds(managerId).stream().noneMatch(scope.categoryIds()::contains)) continue;
            if (!self && scope.managerId() != null && scope.categoryIds() == null) continue;
            long[] values = queries.managerWorkload(managerId, scope, range);
            result.add(new WorkloadItem(managerId, (String) row[1], values[0], ((Number) row[2]).intValue(), values[1]));
        }
        return result;
    }

    private long number(Object value) { return value == null ? 0 : ((Number) value).longValue(); }
    private double percent(long numerator, long denominator) {
        return denominator == 0 ? 0 : 100.0 * numerator / denominator;
    }
}
