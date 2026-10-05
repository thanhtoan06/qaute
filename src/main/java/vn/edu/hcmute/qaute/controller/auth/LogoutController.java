package vn.edu.hcmute.qaute.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.edu.hcmute.qaute.security.CookieService;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;

@Controller
public class LogoutController {

    private final AuthSessionService authSessionService;
    private final CookieService cookieService;

    public LogoutController(AuthSessionService authSessionService, CookieService cookieService) {
        this.authSessionService = authSessionService;
        this.cookieService = cookieService;
    }

    @PostMapping("/auth/logout")
    public String logout(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        SecurityUtils.currentUser()
                .map(QauteUserDetails::getSessionJti)
                .ifPresent(jti -> authSessionService.revokeByJti(jti, "logout"));
        cookieService.clear(response);
        SecurityContextHolder.clearContext();
        redirectAttributes.addFlashAttribute("flashSuccess", "Bạn đã đăng xuất");
        return "redirect:/";
    }
}
