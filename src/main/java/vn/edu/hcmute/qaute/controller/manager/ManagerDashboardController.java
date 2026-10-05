package vn.edu.hcmute.qaute.controller.manager;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vn.edu.hcmute.qaute.security.QauteUserDetails;

/**
 * Trang tổng quan tạm của Manager (nội dung thật làm ở bước T2-45).
 * Quyền truy cập /manager/** (chỉ MANAGER) đã được SecurityConfig kiểm soát.
 */
@Controller
public class ManagerDashboardController {

    @GetMapping("/manager/dashboard")
    public String dashboard(@AuthenticationPrincipal QauteUserDetails me, Model model) {
        model.addAttribute("me", me);
        model.addAttribute("pageTitle", "Tổng quan");
        return "manager/dashboard";
    }
}
