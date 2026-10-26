package vn.edu.hcmute.qaute.controller.student;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.dto.request.student.TicketFilter;
import vn.edu.hcmute.qaute.dto.view.CategoryView;
import vn.edu.hcmute.qaute.dto.view.TicketListItem;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.student.StudentTicketQueryService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho StudentTicket.
@Controller
public class StudentTicketController {

    private final StudentTicketQueryService queryService;
    private final CategoryQueryService categoryQueryService;

    // Khởi tạo bộ điều khiển danh sách yêu cầu và danh mục.
    public StudentTicketController(StudentTicketQueryService queryService,
                                   CategoryQueryService categoryQueryService) {
        this.queryService = queryService;
        this.categoryQueryService = categoryQueryService;
    }

    @GetMapping("/student/tickets")
    // Hiển thị yêu cầu của sinh viên theo bộ lọc và trang.
    public String list(@ModelAttribute TicketFilter filter,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        Long studentId = SecurityUtils.requireUserId();

        Page<TicketListItem> tickets = queryService.list(studentId, filter,
                PageRequest.of(Math.max(page, 0), size <= 0 ? 10 : Math.min(size, 100)));
        Map<TicketStatus, Long> statusCounts = queryService.countByStatus(studentId);
        java.util.List<CategoryView> categories = categoryQueryService.findAllActiveLeaf();

        model.addAttribute("tickets", tickets);
        model.addAttribute("filter", filter);
        model.addAttribute("statusCounts", statusCounts);
        model.addAttribute("categories", categories);
        model.addAttribute("allStatuses", TicketStatus.values());
        return "student/ticket-list";
    }
}

