package vn.edu.hcmute.qaute.controller.admin;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vn.edu.hcmute.qaute.security.QauteUserDetails;

/**
 * Trang tổng quan tạm của Admin (nội dung thật làm ở bước T2-45).
 * Quyền truy cập /admin/** (chỉ ADMIN) đã được SecurityConfig kiểm soát.
 */
@Controller
public class AdminDashboardController {

    @GetMapping("/admin/dashboard")
    public String dashboard(@AuthenticationPrincipal QauteUserDetails me, Model model) {
        model.addAttribute("me", me);
        model.addAttribute("pageTitle", "Tổng quan");
        return "admin/dashboard";
    }
}
