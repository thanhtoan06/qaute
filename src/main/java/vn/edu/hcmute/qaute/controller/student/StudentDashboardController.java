package vn.edu.hcmute.qaute.controller.student;

import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.hcmute.qaute.common.constant.AnnouncementAudience;
import vn.edu.hcmute.qaute.dto.view.AnnouncementView;
import vn.edu.hcmute.qaute.dto.view.StudentDashboardView;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.governance.AnnouncementQueryService;
import vn.edu.hcmute.qaute.service.student.StudentDashboardService;

@Controller
public class StudentDashboardController {

    private final StudentDashboardService dashboardService;
    private final AnnouncementQueryService announcementQueryService;

    public StudentDashboardController(StudentDashboardService dashboardService,
                                      AnnouncementQueryService announcementQueryService) {
        this.dashboardService = dashboardService;
        this.announcementQueryService = announcementQueryService;
    }

    @GetMapping("/student/dashboard")
    public String dashboard(@AuthenticationPrincipal QauteUserDetails me, Model model) {
        Long studentId = SecurityUtils.requireUserId();
        StudentDashboardView dashboard = dashboardService.getDashboard(studentId);
        List<AnnouncementView> announcements = announcementQueryService.findActiveFor(AnnouncementAudience.STUDENT, 2);
        model.addAttribute("me", me);
        model.addAttribute("dashboard", dashboard);
        model.addAttribute("announcements", announcements);
        return "student/dashboard";
    }
}
