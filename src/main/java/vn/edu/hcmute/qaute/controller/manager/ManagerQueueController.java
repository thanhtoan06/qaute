package vn.edu.hcmute.qaute.controller.manager;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.util.PageUtil;
import vn.edu.hcmute.qaute.dto.request.manager.QueueFilter;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.manager.ManagerQueueService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho ManagerQueue.
@Controller
public class ManagerQueueController {
    private final ManagerQueueService queue;

    // Khởi tạo bộ điều khiển hàng đợi yêu cầu.
    public ManagerQueueController(ManagerQueueService queue) {
        this.queue = queue;
    }

    @GetMapping("/manager/queue")
    // Hiển thị hàng đợi cùng bộ lọc và thông tin phân trang.
    public String list(@ModelAttribute("filter") QueueFilter filter,
                       @RequestParam(defaultValue = "0") int page, Model model,
                       HttpServletRequest request) {
        Long managerId = SecurityUtils.requireUserId();
        filter.setTab(ManagerQueueService.tab(filter.getTab()));
        filter.setMineStatus(ManagerQueueService.mineStatus(filter.getMineStatus()));
        Pageable pageable = PageUtil.of(page, 15, filter.getSort(), true,
                ManagerQueueService.SORT_FIELDS, "createdAt");
        model.addAttribute("tickets", queue.list(managerId, filter, pageable));
        model.addAttribute("counts", queue.tabCounts(managerId));
        model.addAttribute("categories", queue.scopeCategories(managerId));
        model.addAttribute("priorities", TicketPriority.values());
        model.addAttribute("statuses", TicketStatus.values());
        model.addAttribute("paginationParams", params(filter, true));
        String tabParams = params(filter, false);
        model.addAttribute("tabParams", tabParams.isEmpty() ? "" : tabParams + "&amp;");
        String rawQuery = request.getQueryString();
        String returnPath = "/manager/queue" + (rawQuery == null || rawQuery.isBlank() ? "" : "?" + rawQuery);
        model.addAttribute("queueReturn", returnPath);
        model.addAttribute("queueReturnEncoded", URLEncoder.encode(returnPath, StandardCharsets.UTF_8));
        model.addAttribute("pageTitle", "Hàng đợi yêu cầu");
        return "manager/queue";
    }

    // Tạo chuỗi tham số cho bộ lọc hàng đợi.
    private static String params(QueueFilter filter, boolean includeTab) {
        StringJoiner params = new StringJoiner("&amp;");
        if (includeTab) add(params, "tab", filter.getTab());
        add(params, "categoryId", filter.getCategoryId());
        if (includeTab && "mine".equals(filter.getTab())) add(params, "mineStatus", filter.getMineStatus());
        add(params, "priority", filter.getPriority());
        add(params, "status", filter.getStatus());
        add(params, "urgentRequested", filter.getUrgentRequested());
        add(params, "assigneeId", filter.getAssigneeId());
        add(params, "minAgeHours", filter.getMinAgeHours());
        add(params, "q", filter.getQ());
        add(params, "sort", filter.getSort());
        return params.toString();
    }

    // Thêm tham số lọc khi giá trị không rỗng.
    private static void add(StringJoiner params, String key, Object value) {
        if (value != null && !value.toString().isBlank()) {
            params.add(key + "=" + URLEncoder.encode(value.toString(), StandardCharsets.UTF_8));
        }
    }
}
