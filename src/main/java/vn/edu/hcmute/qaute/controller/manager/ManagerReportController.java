package vn.edu.hcmute.qaute.controller.manager;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.dto.view.report.AppointmentReport;
import vn.edu.hcmute.qaute.dto.view.report.ChatReport;
import vn.edu.hcmute.qaute.dto.view.report.ReportRange;
import vn.edu.hcmute.qaute.dto.view.report.ReportScope;
import vn.edu.hcmute.qaute.dto.view.report.TicketReport;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.report.ServiceReportService;
import vn.edu.hcmute.qaute.service.report.TicketReportService;

@Controller
public class ManagerReportController {
    private final TicketReportService tickets;
    private final ServiceReportService services;
    private final ManagerScopeService managerScopes;
    private final CategoryQueryService categories;

    public ManagerReportController(TicketReportService tickets, ServiceReportService services,
                                   ManagerScopeService managerScopes, CategoryQueryService categories) {
        this.tickets = tickets;
        this.services = services;
        this.managerScopes = managerScopes;
        this.categories = categories;
    }

    public record ReportData(TicketReport ticket, ChatReport chat, AppointmentReport appointment) {}
    private record Criteria(ReportRange range, ReportScope scope) {}

    @GetMapping("/manager/reports")
    public String page(@RequestParam(defaultValue = "30") String range,
                       @RequestParam(required = false) String from,
                       @RequestParam(required = false) String to,
                       @RequestParam(required = false) Long categoryId,
                       @RequestParam(defaultValue = "false") boolean mine, Model model) {
        Long managerId = SecurityUtils.requireUserId();
        Criteria criteria = criteria(managerId, range, from, to, categoryId, mine);
        var choices = new LinkedHashMap<Long, String>();
        managerScopes.getScopeCategoryIds(managerId).forEach(id -> {
            String label = categories.getDisplayPath(id);
            choices.put(id, label.isBlank() ? "#" + id : label);
        });
        model.addAttribute("categories", choices);
        model.addAttribute("range", range);
        model.addAttribute("from", criteria.range().from());
        model.addAttribute("to", criteria.range().to());
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("mine", mine);
        model.addAttribute("pageTitle", "Báo cáo trong phạm vi");
        return "manager/reports";
    }

    @GetMapping("/api/manager/reports/data")
    @ResponseBody
    public ApiResponse<ReportData> data(@RequestParam(defaultValue = "30") String range,
                                        @RequestParam(required = false) String from,
                                        @RequestParam(required = false) String to,
                                        @RequestParam(required = false) Long categoryId,
                                        @RequestParam(defaultValue = "false") boolean mine) {
        Criteria criteria = criteria(SecurityUtils.requireUserId(), range, from, to, categoryId, mine);
        return ApiResponse.ok(new ReportData(tickets.build(criteria.scope(), criteria.range()),
                services.chat(criteria.scope(), criteria.range()),
                services.appointment(criteria.scope(), criteria.range())));
    }

    private Criteria criteria(Long managerId, String range, String from, String to,
                              Long categoryId, boolean mine) {
        List<Long> allowed = managerScopes.getScopeCategoryIds(managerId);
        if (categoryId != null && !allowed.contains(categoryId))
            throw new BadRequestException("Chuyên mục không thuộc phạm vi của bạn");
        LocalDate today = LocalDate.now();
        ReportRange dates;
        switch (range) {
            case "7" -> dates = new ReportRange(today.minusDays(6), today);
            case "30" -> dates = new ReportRange(today.minusDays(29), today);
            case "90" -> dates = new ReportRange(today.minusDays(89), today);
            case "custom" -> dates = new ReportRange(parse(from), parse(to));
            default -> throw new BadRequestException("Khoảng báo cáo không hợp lệ");
        }
        if (dates.from().isAfter(dates.to()) || ChronoUnit.DAYS.between(dates.from(), dates.to()) > 365)
            throw new BadRequestException("Khoảng báo cáo tối đa 366 ngày");
        Set<Long> categoryIds = mine ? Set.of() : categoryId == null ? Set.copyOf(allowed) : Set.of(categoryId);
        return new Criteria(dates, new ReportScope(managerId, categoryIds));
    }

    private LocalDate parse(String value) {
        if (value == null || value.isBlank()) throw new BadRequestException("Vui lòng chọn đủ ngày bắt đầu và kết thúc");
        try { return LocalDate.parse(value); }
        catch (DateTimeParseException error) { throw new BadRequestException("Ngày báo cáo không hợp lệ"); }
    }
}
