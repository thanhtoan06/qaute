package vn.edu.hcmute.qaute.controller.student;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.hcmute.qaute.security.QauteUserDetails;

@Controller
public class StudentDashboardController {

    @GetMapping("/student/dashboard")
    public String dashboard(@AuthenticationPrincipal QauteUserDetails me, Model model) {
        model.addAttribute("me", me);
        return "student/dashboard";
    }
}
