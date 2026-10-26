package vn.edu.hcmute.qaute.controller.manager;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.hcmute.qaute.common.util.BoundedPages;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.manager.ManagerStudentService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho ManagerStudent.
@Controller
@RequestMapping("/manager/students")
public class ManagerStudentController {
    private final ManagerStudentService students;

    // Khởi tạo bộ điều khiển danh sách sinh viên liên quan.
    public ManagerStudentController(ManagerStudentService students) {
        this.students = students;
    }

    @GetMapping
    // Tìm kiếm và hiển thị danh sách sinh viên liên quan.
    public String list(@RequestParam(required = false) String q,
                       @RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("students", students.search(SecurityUtils.requireUserId(), q,
                PageRequest.of(Math.max(0, page), 15)));
        model.addAttribute("q", q == null ? "" : q.trim());
        model.addAttribute("paginationParams", q == null || q.isBlank() ? ""
                : "q=" + URLEncoder.encode(q.trim(), StandardCharsets.UTF_8));
        model.addAttribute("pageTitle", "Sinh viên liên quan");
        return "manager/student-list";
    }

    @GetMapping("/{id}")
    // Hiển thị hồ sơ và lịch sử yêu cầu của sinh viên.
    public String detail(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("detail", students.detail(SecurityUtils.requireUserId(), id, BoundedPages.of(page, 15)));
        model.addAttribute("pageTitle", "Hồ sơ sinh viên");
        return "manager/student-detail";
    }
}
