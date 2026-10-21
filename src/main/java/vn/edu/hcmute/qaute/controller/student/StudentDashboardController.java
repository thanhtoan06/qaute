package vn.edu.hcmute.qaute.controller.student;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.dto.view.StudentDashboardView;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.service.student.StudentDashboardService;

@Controller
public class StudentDashboardController {

    private final StudentDashboardService dashboardService;

    public StudentDashboardController(StudentDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/student/dashboard")
    public String dashboard(@AuthenticationPrincipal QauteUserDetails me, Model model) {
        Long studentId = SecurityUtils.requireUserId();
        StudentDashboardView dashboard = dashboardService.getDashboard(studentId);
        model.addAttribute("me", me);
        model.addAttribute("dashboard", dashboard);
        return "student/dashboard";
    }
}
