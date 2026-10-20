package vn.edu.hcmute.qaute.controller.admin;

import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.common.util.CsvUtil;
import vn.edu.hcmute.qaute.dto.view.report.AppointmentReport;
import vn.edu.hcmute.qaute.dto.view.report.ChatReport;
import vn.edu.hcmute.qaute.dto.view.report.DailyCount;
import vn.edu.hcmute.qaute.dto.view.report.DepartmentCompare;
import vn.edu.hcmute.qaute.dto.view.report.HourlyLoad;
import vn.edu.hcmute.qaute.dto.view.report.KnowledgeReport;
import vn.edu.hcmute.qaute.dto.view.report.ManagerPerformance;
import vn.edu.hcmute.qaute.dto.view.report.ReportRange;
import vn.edu.hcmute.qaute.dto.view.report.ReportScope;
import vn.edu.hcmute.qaute.dto.view.report.TicketReport;
import vn.edu.hcmute.qaute.dto.view.report.WeeklyCount;
import vn.edu.hcmute.qaute.entity.organization.Category;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.repository.report.ReportQueryRepository;
import vn.edu.hcmute.qaute.repository.report.ServiceReportQueryRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.report.ServiceReportService;
import vn.edu.hcmute.qaute.service.report.TicketReportService;
import vn.edu.hcmute.qaute.security.SecurityUtils;

@Controller
public class AdminReportController {
    private final TicketReportService ticketReports;
    private final ServiceReportService serviceReports;
    private final ReportQueryRepository ticketQueries;
    private final ServiceReportQueryRepository serviceQueries;
    private final DepartmentRepository departments;
    private final CategoryRepository categories;
    private final CategoryQueryService categoryPaths;
    private final AuditService audit;

    public AdminReportController(TicketReportService ticketReports, ServiceReportService serviceReports,
            ReportQueryRepository ticketQueries, ServiceReportQueryRepository serviceQueries,
            DepartmentRepository departments, CategoryRepository categories,
            CategoryQueryService categoryPaths, AuditService audit) {
        this.ticketReports = ticketReports;
        this.serviceReports = serviceReports;
        this.ticketQueries = ticketQueries;
        this.serviceQueries = serviceQueries;
        this.departments = departments;
        this.categories = categories;
        this.categoryPaths = categoryPaths;
        this.audit = audit;
    }

    public record CategoryOption(Long id, Long departmentId, String label) {}
    public record ReportData(TicketReport ticket, KnowledgeReport knowledge,
            List<DepartmentCompare> departments, List<ManagerPerformance> managers,
            ChatReport chat, AppointmentReport appointment, List<WeeklyCount> weekly,
            List<HourlyLoad> hourly) {}
    private record Criteria(ReportRange range, ReportScope scope) {}

    @GetMapping("/admin/reports")
    public String page(@RequestParam(defaultValue = "30") String range,
            @RequestParam(required = false) String from, @RequestParam(required = false) String to,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long categoryId, Model model) {
        Criteria criteria = criteria(range, from, to, departmentId, categoryId);
        model.addAttribute("departments", departments.findAllByOrderBySortOrderAscNameAsc());
        model.addAttribute("categories", categories.findAll().stream()
                .map(category -> new CategoryOption(category.getId(), category.getDepartmentId(),
                        categoryPaths.getDisplayPath(category.getId()))).toList());
        model.addAttribute("range", range);
        model.addAttribute("from", criteria.range().from());
        model.addAttribute("to", criteria.range().to());
        model.addAttribute("departmentId", departmentId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("pageTitle", "Báo cáo toàn hệ thống");
        return "admin/reports";
    }

    @GetMapping("/api/admin/reports/data")
    @ResponseBody
    public ApiResponse<ReportData> data(@RequestParam(defaultValue = "30") String range,
            @RequestParam(required = false) String from, @RequestParam(required = false) String to,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long categoryId) {
        if (!SecurityUtils.hasRole(RoleCode.ADMIN)) throw new ForbiddenException();
        Criteria criteria = criteria(range, from, to, departmentId, categoryId);
        ReportRange dates = criteria.range();
        ReportScope scope = criteria.scope();
        TicketReport ticket = ticketReports.build(scope, dates);
        return ApiResponse.ok(new ReportData(ticket, serviceReports.knowledge(dates),
                serviceReports.compareDepartments(dates), serviceReports.managerPerformance(dates),
                serviceReports.chat(scope, dates), serviceReports.appointment(scope, dates),
                weekly(ticket.perDay()), hourly(scope, dates)));
    }

    @GetMapping("/admin/reports/export")
    public ResponseEntity<byte[]> export(@RequestParam String type,
            @RequestParam(required = false) String from, @RequestParam(required = false) String to,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long categoryId) {
        if (!Set.of("tickets", "managers", "departments", "knowledge", "noresult").contains(type))
            throw new BadRequestException("Loại báo cáo không hợp lệ");
        Criteria criteria = criteria("custom", from, to, departmentId, categoryId);
        String csv = csv(type, criteria);
        String filename = "bao-cao-" + type + "-" + criteria.range().from() + "-" + criteria.range().to() + ".csv";
        audit.log(AuditActions.REPORT_EXPORT, "report", null, null,
                type + " " + criteria.range().from() + ".." + criteria.range().to());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename, StandardCharsets.UTF_8).build());
        headers.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));
        return ResponseEntity.ok().headers(headers).body(CsvUtil.withBom(csv));
    }

    private String csv(String type, Criteria criteria) {
        ReportRange dates = criteria.range();
        StringBuilder csv = new StringBuilder();
        switch (type) {
            case "tickets" -> {
                TicketReport report = ticketReports.build(criteria.scope(), dates);
                csv.append(CsvUtil.row("Mục", "Tên", "Giá trị"));
                for (DailyCount item : report.perDay()) csv.append(CsvUtil.row("Theo ngày", item.day().toString(), String.valueOf(item.count())));
                report.byCategory().forEach(item -> csv.append(CsvUtil.row("Chuyên mục", item.name(), String.valueOf(item.count()))));
                report.byStatus().forEach(item -> csv.append(CsvUtil.row("Trạng thái", item.name(), String.valueOf(item.count()))));
                csv.append(CsvUtil.row("KPI", "FRT trung vị (phút)", String.valueOf(report.firstResponse().medianMinutes())));
                csv.append(CsvUtil.row("KPI", "Giải quyết trung vị (phút)", String.valueOf(report.resolution().medianMinutes())));
                csv.append(CsvUtil.row("KPI", "SLA đạt (%)", String.valueOf(report.sla().percent())));
                csv.append(CsvUtil.row("KPI", "Tồn đọng", String.valueOf(report.backlog().open())));
                csv.append(CsvUtil.row("KPI", "CSAT trung bình", String.valueOf(report.csat().avg())));
                csv.append(CsvUtil.row("KPI", "Mở lại (%)", String.valueOf(report.reopenRate())));
                ChatReport chat = serviceReports.chat(criteria.scope(), dates);
                AppointmentReport appointments = serviceReports.appointment(criteria.scope(), dates);
                csv.append(CsvUtil.row("Chat", "Số phiên", String.valueOf(chat.total())));
                csv.append(CsvUtil.row("Chat", "Chờ trung bình (giây)", String.valueOf(chat.avgWaitSeconds())));
                csv.append(CsvUtil.row("Chat", "Bỏ cuộc (%)", String.valueOf(chat.abandonedPercent())));
                csv.append(CsvUtil.row("Lịch hẹn", "Đã đặt", String.valueOf(appointments.booked())));
                csv.append(CsvUtil.row("Lịch hẹn", "Hoàn thành", String.valueOf(appointments.completed())));
                csv.append(CsvUtil.row("Lịch hẹn", "Sử dụng slot (%)", String.valueOf(appointments.utilizationPercent())));
            }
            case "managers" -> {
                csv.append(CsvUtil.row("ID", "Manager", "Đã giải quyết", "FRT trung bình (phút)", "CSAT", "Chat"));
                for (ManagerPerformance item : serviceReports.managerPerformance(dates))
                    csv.append(CsvUtil.row(String.valueOf(item.managerId()), item.managerName(),
                            String.valueOf(item.resolved()), String.valueOf(item.avgFirstResponseMinutes()),
                            String.valueOf(item.csat()), String.valueOf(item.chats())));
            }
            case "departments" -> {
                csv.append(CsvUtil.row("Phòng ban", "Ticket", "FRT trung bình (phút)", "SLA đạt (%)", "CSAT"));
                for (DepartmentCompare item : serviceReports.compareDepartments(dates))
                    csv.append(CsvUtil.row(item.departmentName(), String.valueOf(item.tickets()),
                            String.valueOf(item.avgFirstResponseMinutes()), String.valueOf(item.slaPercent()),
                            String.valueOf(item.csat())));
            }
            case "knowledge" -> {
                KnowledgeReport item = serviceReports.knowledge(dates);
                csv.append(CsvUtil.row("Chỉ số", "Giá trị"));
                csv.append(CsvUtil.row("Lượt xem FAQ có ghi nhận", String.valueOf(item.faqViews())));
                csv.append(CsvUtil.row("Hữu ích (%)", String.valueOf(item.helpfulPercent())));
                csv.append(CsvUtil.row("Không kết quả (%)", String.valueOf(item.noResultPercent())));
                csv.append(CsvUtil.row("Tìm kiếm đến click (%)", String.valueOf(item.searchToClickPercent())));
                csv.append(CsvUtil.row("Proxy tránh tạo ticket (%)", String.valueOf(item.deflectionProxyPercent())));
            }
            case "noresult" -> {
                csv.append(CsvUtil.row("Truy vấn không có kết quả", "Số lần"));
                serviceReports.knowledge(dates).topNoResultQueries().forEach(item ->
                        csv.append(CsvUtil.row(item.name(), String.valueOf(item.count()))));
            }
            default -> throw new BadRequestException("Loại báo cáo không hợp lệ");
        }
        return csv.toString();
    }

    private Criteria criteria(String range, String from, String to, Long departmentId, Long categoryId) {
        LocalDate today = LocalDate.now();
        ReportRange dates = switch (range) {
            case "7" -> new ReportRange(today.minusDays(6), today);
            case "30" -> new ReportRange(today.minusDays(29), today);
            case "90" -> new ReportRange(today.minusDays(89), today);
            case "custom" -> new ReportRange(parse(from), parse(to));
            default -> throw new BadRequestException("Khoảng báo cáo không hợp lệ");
        };
        if (dates.from().isAfter(dates.to()) || ChronoUnit.DAYS.between(dates.from(), dates.to()) > 365)
            throw new BadRequestException("Khoảng báo cáo tối đa 366 ngày");
        if (departmentId != null && !departments.existsById(departmentId))
            throw new BadRequestException("Phòng ban không tồn tại");
        Set<Long> ids = null;
        if (departmentId != null) {
            Set<Long> departmentCategories = new HashSet<>();
            categories.findByDepartmentIdOrderBySortOrderAscNameAsc(departmentId)
                    .forEach(category -> departmentCategories.add(category.getId()));
            ids = departmentCategories;
        }
        if (categoryId != null) {
            Category category = categories.findById(categoryId)
                    .orElseThrow(() -> new BadRequestException("Chuyên mục không tồn tại"));
            if (departmentId != null && !departmentId.equals(category.getDepartmentId()))
                throw new BadRequestException("Chuyên mục không thuộc phòng ban đã chọn");
            Set<Long> descendants = Set.copyOf(categoryPaths.getDescendantIds(categoryId));
            if (ids == null) ids = descendants;
            else ids.retainAll(descendants);
        }
        return new Criteria(dates, new ReportScope(null, ids));
    }

    private LocalDate parse(String value) {
        if (value == null || value.isBlank()) throw new BadRequestException("Vui lòng chọn đủ ngày bắt đầu và kết thúc");
        try { return LocalDate.parse(value); }
        catch (DateTimeParseException error) { throw new BadRequestException("Ngày báo cáo không hợp lệ"); }
    }

    private List<WeeklyCount> weekly(List<DailyCount> days) {
        var counts = new TreeMap<LocalDate, Long>();
        for (DailyCount item : days) {
            LocalDate monday = item.day().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            counts.merge(monday, item.count(), Long::sum);
        }
        return counts.entrySet().stream().map(item -> new WeeklyCount(item.getKey(), item.getValue())).toList();
    }

    private List<HourlyLoad> hourly(ReportScope scope, ReportRange range) {
        long[] ticketCounts = new long[24];
        long[] chatCounts = new long[24];
        ticketQueries.createdPerHour(scope, range).forEach(row -> ticketCounts[((Number) row[0]).intValue()] = ((Number) row[1]).longValue());
        serviceQueries.chatsPerHour(scope, range).forEach(row -> chatCounts[((Number) row[0]).intValue()] = ((Number) row[1]).longValue());
        List<HourlyLoad> hours = new ArrayList<>(24);
        for (int hour = 0; hour < 24; hour++) hours.add(new HourlyLoad(hour, ticketCounts[hour], chatCounts[hour]));
        return hours;
    }
}
