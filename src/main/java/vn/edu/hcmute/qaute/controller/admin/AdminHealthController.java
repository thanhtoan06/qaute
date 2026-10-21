package vn.edu.hcmute.qaute.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.admin.SystemHealthService;

@Controller
public class AdminHealthController {
    private final SystemHealthService health;

    public AdminHealthController(SystemHealthService health) {
        this.health = health;
    }

    @GetMapping("/admin/health")
    public String page(Model model) {
        model.addAttribute("checks", health.checks());
        model.addAttribute("emailConfigured", health.isEmailConfigured());
        model.addAttribute("pageTitle", "Kiểm tra hệ thống");
        return "admin/health";
    }

    @PostMapping("/admin/health/test-email")
    public String testEmail(RedirectAttributes redirect) {
        try {
            health.queueTestEmail(SecurityUtils.requireUser().getEmail());
            redirect.addFlashAttribute("flashSuccess",
                    "Đã thêm thư thử vào hàng đợi. Hãy làm mới trang để xem trạng thái gửi.");
        } catch (BadRequestException exception) {
            redirect.addFlashAttribute("flashError", exception.getMessage());
        } catch (RuntimeException exception) {
            redirect.addFlashAttribute("flashError", "Không thể thêm thư thử vào hàng đợi email.");
        }
        return "redirect:/admin/health";
    }
}
