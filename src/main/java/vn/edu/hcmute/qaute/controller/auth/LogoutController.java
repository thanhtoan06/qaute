package vn.edu.hcmute.qaute.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.security.CookieService;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho Logout.
@Controller
public class LogoutController {

    private final AuthSessionService authSessionService;
    private final CookieService cookieService;

    // Khởi tạo bộ điều khiển đăng xuất.
    public LogoutController(AuthSessionService authSessionService, CookieService cookieService) {
        this.authSessionService = authSessionService;
        this.cookieService = cookieService;
    }

    @PostMapping("/auth/logout")
    // Đăng xuất người dùng bằng yêu cầu POST.
    public String logout(HttpServletRequest request, HttpServletResponse response,
                         RedirectAttributes redirectAttributes) {
        performLogout(request, response);
        redirectAttributes.addFlashAttribute("flashSuccess", "Bạn đã đăng xuất");
        return "redirect:/";
    }

    @GetMapping("/auth/logout")
    // Đăng xuất người dùng bằng yêu cầu GET.
    public String logoutGet(HttpServletRequest request, HttpServletResponse response,
                            RedirectAttributes redirectAttributes) {
        performLogout(request, response);
        redirectAttributes.addFlashAttribute("flashSuccess", "Bạn đã đăng xuất");
        return "redirect:/";
    }

    // Thu hồi phiên, xóa cookie và kết thúc phiên HTTP.
    private void performLogout(HttpServletRequest request, HttpServletResponse response) {
        SecurityUtils.currentUser()
                .map(QauteUserDetails::getSessionJti)
                .ifPresent(jti -> authSessionService.revokeByJti(jti, "logout"));
        cookieService.clear(response);
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
