package vn.edu.hcmute.qaute.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.web.ClientIpResolver;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.auth.LoginForm;
import vn.edu.hcmute.qaute.dto.view.LoginOutcome;
import vn.edu.hcmute.qaute.security.CookieService;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.identity.LoginService;

@Controller
public class LoginController {

    private final LoginService loginService;
    private final CookieService cookieService;

    public LoginController(LoginService loginService, CookieService cookieService) {
        this.loginService = loginService;
        this.cookieService = cookieService;
    }

    @GetMapping("/auth/login")
    public String loginPage(@RequestParam(defaultValue = "") String returnUrl,
                            @RequestParam(defaultValue = "false") boolean verified,
                            Model model) {
        if (SecurityUtils.currentUser().isPresent()) {
            return dashboardRedirect();
        }
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new LoginForm());
        }
        model.addAttribute("returnUrl", returnUrl);
        model.addAttribute("verified", verified);
        return "auth/login";
    }

    @PostMapping("/auth/login")
    public String login(@Valid @ModelAttribute("form") LoginForm form,
                        BindingResult bindingResult,
                        @RequestParam(defaultValue = "") String returnUrl,
                        Model model, HttpServletRequest request,
                        HttpServletResponse response, RedirectAttributes redirectAttributes) {
        if (SecurityUtils.currentUser().isPresent()) {
            return dashboardRedirect();
        }
        model.addAttribute("returnUrl", returnUrl);
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", FormErrors.of(bindingResult));
            return "auth/login";
        }

        LoginOutcome outcome = loginService.login(form, ClientIpResolver.resolve(request),
                request.getHeader("User-Agent"));
        return switch (outcome.kind()) {
            case OK -> {
                cookieService.writeTokens(request, response, outcome.tokens());
                yield "redirect:" + safeRedirect(returnUrl, outcome.redirectRole());
            }
            case NEED_VERIFY -> {
                redirectAttributes.addFlashAttribute("flashError",
                        "Tài khoản chưa được xác thực, vui lòng nhập mã OTP");
                yield "redirect:/auth/verify-otp?email="
                        + URLEncoder.encode(outcome.email(), StandardCharsets.UTF_8);
            }
            case LOCKED_ADMIN -> showError(model, "Tài khoản đang bị khóa, vui lòng liên hệ quản trị");
            case LOCKED_TEMP -> showError(model,
                    "Tài khoản bị khóa tạm thời, thử lại sau "
                            + outcome.lockedMinutes() + " phút");
            case BAD_CREDENTIALS -> showError(model, "Thông tin đăng nhập không đúng");
        };
    }

    private String showError(Model model, String message) {
        model.addAttribute("errors", Map.of("global", message));
        return "auth/login";
    }

    private String safeRedirect(String returnUrl, String roleName) {
        if (returnUrl != null && returnUrl.startsWith("/") && !returnUrl.startsWith("//")
                && !returnUrl.contains("://") && !returnUrl.contains("\\")) {
            RoleCode role = RoleCode.valueOf(roleName);
            if ((role == RoleCode.STUDENT && returnUrl.startsWith("/student/"))
                    || (role == RoleCode.MANAGER && returnUrl.startsWith("/manager/"))
                    || (role == RoleCode.ADMIN && returnUrl.startsWith("/admin/"))
                    || (!returnUrl.startsWith("/student/") && !returnUrl.startsWith("/manager/")
                    && !returnUrl.startsWith("/admin/"))) {
                return returnUrl;
            }
        }
        return dashboardPath(RoleCode.valueOf(roleName));
    }

    private String dashboardRedirect() {
        return SecurityUtils.currentUser().map(QauteUserDetails::getRoleCode)
                .map(this::dashboardPath).orElse("redirect:/");
    }

    private String dashboardPath(RoleCode role) {
        return switch (role) {
            case STUDENT -> "/student/dashboard";
            case MANAGER -> "/manager/dashboard";
            case ADMIN -> "/admin/dashboard";
        };
    }
}
