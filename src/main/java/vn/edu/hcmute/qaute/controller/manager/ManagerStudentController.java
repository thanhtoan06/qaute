package vn.edu.hcmute.qaute.controller.manager;

import vn.edu.hcmute.qaute.common.util.BoundedPages;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.manager.ManagerStudentService;

@Controller
@RequestMapping("/manager/students")
public class ManagerStudentController {
    private final ManagerStudentService students;

    public ManagerStudentController(ManagerStudentService students) {
        this.students = students;
    }

    @GetMapping
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
    public String detail(@PathVariable Long id, @RequestParam(defaultValue="0") int page, Model model) {
        model.addAttribute("detail", students.detail(SecurityUtils.requireUserId(), id, BoundedPages.of(page, 15)));
        model.addAttribute("pageTitle", "Hồ sơ sinh viên");
        return "manager/student-detail";
    }
}
