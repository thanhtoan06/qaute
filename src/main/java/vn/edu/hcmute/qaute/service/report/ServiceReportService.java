package vn.edu.hcmute.qaute.service.report;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.dto.view.report.AppointmentReport;
import vn.edu.hcmute.qaute.dto.view.report.ChatReport;
import vn.edu.hcmute.qaute.dto.view.report.DepartmentCompare;
import vn.edu.hcmute.qaute.dto.view.report.KnowledgeReport;
import vn.edu.hcmute.qaute.dto.view.report.ManagerPerformance;
import vn.edu.hcmute.qaute.dto.view.report.NamedCount;
import vn.edu.hcmute.qaute.dto.view.report.ReportRange;
import vn.edu.hcmute.qaute.dto.view.report.ReportScope;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.repository.report.ReportQueryRepository;
import vn.edu.hcmute.qaute.repository.report.ServiceReportQueryRepository;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.ticket.SlaService;

// Xử lý nghiệp vụ ServiceReport trong hệ thống.
@Service
@Transactional(readOnly = true)
public class ServiceReportService {
    private final ServiceReportQueryRepository serviceQueries;
    private final ReportQueryRepository ticketQueries;
    private final ManagerScopeService scopes;
    private final DepartmentRepository departments;
    private final CategoryRepository categories;
    private final SlaService sla;

    // Khởi tạo dịch vụ tổng hợp báo cáo dịch vụ.
    public ServiceReportService(ServiceReportQueryRepository serviceQueries, ReportQueryRepository ticketQueries,
                                ManagerScopeService scopes, DepartmentRepository departments,
                                CategoryRepository categories, SlaService sla) {
        this.serviceQueries = serviceQueries;
        this.ticketQueries = ticketQueries;
        this.scopes = scopes;
        this.departments = departments;
        this.categories = categories;
        this.sla = sla;
    }

    // Tổng hợp số liệu trò chuyện theo phạm vi và thời gian.
    public ChatReport chat(ReportScope scope, ReportRange range) {
        validate(scope, range);
        scope = effectiveScope(scope);
        Object[] values = serviceQueries.chatTotals(scope, range);
        long total = number(values[0]);
        List<NamedCount> perManager = serviceQueries.chatsPerManager(scope, range).stream()
                .map(row -> new NamedCount((String) row[0], number(row[1]))).toList();
        return new ChatReport(total, decimal(values[1]), decimal(values[2]),
                percent(number(values[3]), total), perManager);
    }

    // Tổng hợp số liệu đặt lịch theo phạm vi và thời gian.
    public AppointmentReport appointment(ReportScope scope, ReportRange range) {
        validate(scope, range);
        scope = effectiveScope(scope);
        Object[] values = serviceQueries.appointmentTotals(scope, range);
        long booked = number(values[0]);
        long[] slots = serviceQueries.slotTotals(range, slotManagerIds(scope));
        return new AppointmentReport(booked, number(values[1]), percent(number(values[2]), booked),
                percent(number(values[3]), booked), percent(slots[1], slots[0]));
    }

    // Tổng hợp số liệu phản hồi và tìm kiếm tri thức.
    public KnowledgeReport knowledge(ReportRange range) {
        requireAdmin();
        validateRange(range);
        long[] feedback = serviceQueries.feedback(range);
        long[] searches = serviceQueries.searches(range);
        long[] deflection = serviceQueries.deflection(range);
        List<NamedCount> topNoResult = serviceQueries.topNoResultQueries(range).stream()
                .map(row -> new NamedCount((String) row[0], number(row[1]))).toList();
        List<NamedCount> lowHelpful = serviceQueries.lowHelpfulFaqs().stream()
                .map(row -> new NamedCount((String) row[0], number(row[1]) + number(row[2]))).toList();
        // faq_views stores one cumulative row per user and FAQ. Counting recently viewed pairs
        // avoids treating the cumulative view_count as views made inside this date range.
        return new KnowledgeReport(serviceQueries.faqViews(range), percent(feedback[1], feedback[0]),
                percent(searches[1], searches[0]), percent(searches[2], searches[0]),
                topNoResult, lowHelpful, percent(deflection[1], deflection[0]));
    }

    // So sánh chỉ số báo cáo giữa các phòng ban.
    public List<DepartmentCompare> compareDepartments(ReportRange range) {
        requireAdmin();
        validateRange(range);
        List<DepartmentCompare> result = new ArrayList<>();
        for (var department : departments.findAllByOrderBySortOrderAscNameAsc()) {
            Set<Long> categoryIds = new HashSet<>();
            categories.findByDepartmentIdOrderBySortOrderAscNameAsc(department.getId())
                    .forEach(category -> categoryIds.add(category.getId()));
            ReportScope scope = new ReportScope(null, categoryIds);
            long count = ticketQueries.createdByCategory(scope, range).stream()
                    .mapToLong(row -> number(row[1])).sum();
            double firstResponse = averageFirstResponse(ticketQueries.latestCreated(scope, range, 5000));
            long[] slaCounts = ticketQueries.slaCounts(scope, range);
            double csat = averageRating(ticketQueries.reviews(scope, range));
            result.add(new DepartmentCompare(department.getName(), count, firstResponse,
                    percent(slaCounts[1], slaCounts[0]), csat));
        }
        return result;
    }

    // Tổng hợp hiệu suất báo cáo của từng quản lý.
    public List<ManagerPerformance> managerPerformance(ReportRange range) {
        requireAdmin();
        validateRange(range);
        List<ManagerPerformance> result = new ArrayList<>();
        for (Object[] manager : ticketQueries.managerCandidates()) {
            Long managerId = ((Number) manager[0]).longValue();
            ReportScope scope = new ReportScope(managerId, Set.of());
            long resolved = ticketQueries.resolvedAndReopened(scope, range)[0];
            double firstResponse = averageFirstResponse(ticketQueries.latestCreated(scope, range, 5000));
            double csat = averageRating(ticketQueries.reviews(scope, range));
            long chats = serviceQueries.managerChats(managerId, range);
            result.add(new ManagerPerformance(managerId, (String) manager[1], resolved, firstResponse, csat, chats));
        }
        return result;
    }

    // Lấy các quản lý thuộc phạm vi đặt lịch được yêu cầu.
    private Set<Long> slotManagerIds(ReportScope scope) {
        if (scope.managerId() == null && scope.categoryIds() == null) return null;
        Set<Long> managerIds = new HashSet<>();
        if (scope.managerId() != null) managerIds.add(scope.managerId());
        if (scope.categoryIds() != null) scope.categoryIds().forEach(id -> managerIds.addAll(scopes.findManagerIdsInScope(id)));
        return managerIds;
    }

    // Giới hạn phạm vi báo cáo theo quyền của người dùng hiện tại.
    private ReportScope effectiveScope(ReportScope requested) {
        // The report scope supplied by a Manager is never wider than their assigned categories.
        if (SecurityUtils.hasRole(RoleCode.ADMIN)) return requested;
        if (!SecurityUtils.hasRole(RoleCode.MANAGER)) throw new ForbiddenException();
        Long managerId = SecurityUtils.requireUserId();
        Set<Long> allowed = new HashSet<>(scopes.getScopeCategoryIds(managerId));
        if (requested.categoryIds() != null) allowed.retainAll(requested.categoryIds());
        return new ReportScope(managerId, allowed);
    }

    // Tính thời gian phản hồi trung bình theo phút làm việc.
    private double averageFirstResponse(List<Ticket> tickets) {
        long count = 0;
        double total = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getFirstResponseAt() == null) continue;
            total += Math.max(0, sla.businessMinutesBetween(ticket.getCreatedAt(), ticket.getFirstResponseAt())
                    - ticket.getSlaPausedSeconds() / 60.0);
            count++;
        }
        return count == 0 ? 0 : total / count;
    }

    // Tính điểm đánh giá trung bình từ danh sách phản hồi.
    private double averageRating(List<Object[]> reviews) {
        return reviews.stream().mapToInt(row -> ((Number) row[0]).intValue()).average().orElse(0);
    }

    // Kiểm tra phạm vi và khoảng thời gian báo cáo.
    private void validate(ReportScope scope, ReportRange range) {
        if (scope == null) throw new BadRequestException("Phạm vi báo cáo không hợp lệ");
        validateRange(range);
    }

    // Yêu cầu người dùng hiện tại có quyền quản trị.
    private void requireAdmin() {
        if (!SecurityUtils.hasRole(RoleCode.ADMIN)) throw new ForbiddenException();
    }

    // Kiểm tra khoảng thời gian báo cáo hợp lệ.
    private void validateRange(ReportRange range) {
        if (range == null || range.from() == null || range.to() == null || range.from().isAfter(range.to())
                || java.time.temporal.ChronoUnit.DAYS.between(range.from(), range.to()) > 365)
            throw new BadRequestException("Khoảng báo cáo không hợp lệ");
    }

    // Chuyển giá trị truy vấn thành số nguyên, mặc định bằng không.
    private long number(Object value) { return value == null ? 0 : ((Number) value).longValue(); }

    // Chuyển giá trị truy vấn thành số thập phân, mặc định bằng không.
    private double decimal(Object value) { return value == null ? 0 : ((Number) value).doubleValue(); }

    // Tính tỷ lệ phần trăm an toàn khi mẫu số bằng không.
    private double percent(long numerator, long denominator) {
        return denominator == 0 ? 0 : 100.0 * numerator / denominator;
    }
}
