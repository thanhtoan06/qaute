package vn.edu.hcmute.qaute.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.admin.UserPasswordForm;
import vn.edu.hcmute.qaute.security.CookieService;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.admin.AdminUserPasswordService;
import vn.edu.hcmute.qaute.service.admin.AdminUserQueryService;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserPasswordController {
    private final AdminUserQueryService userQueries;
    private final AdminUserPasswordService passwords;
    private final CookieService cookies;

    public AdminUserPasswordController(AdminUserQueryService userQueries,
            AdminUserPasswordService passwords, CookieService cookies) {
        this.userQueries = userQueries;
        this.passwords = passwords;
        this.cookies = cookies;
    }

    @GetMapping("/admin/users/{id}/password")
    public String passwordPage(@PathVariable Long id, Model model) {
        return form(id, model, Map.of());
    }

    @PostMapping("/admin/users/{id}/password")
    public String changePassword(@PathVariable Long id,
            @Valid @ModelAttribute("form") UserPasswordForm form, BindingResult result,
            Model model, RedirectAttributes redirect, HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, String> errors = new LinkedHashMap<>(FormErrors.of(result));
        if (!Objects.equals(form.getNewPassword(), form.getConfirmPassword())) {
            errors.put("confirmPassword", "Mật khẩu xác nhận không khớp");
        }
        if (!errors.isEmpty()) return form(id, model, errors);
        try {
            Long actorId = SecurityUtils.requireUserId();
            passwords.changePassword(id, form);
            if (id.equals(actorId)) {
                cookies.clear(response);
                var session = request.getSession(false);
                if (session != null) session.invalidate();
                SecurityContextHolder.clearContext();
                // Không tạo flash session mới sau khi thu hồi phiên của chính Admin.
                return "redirect:/auth/login?passwordChanged=true";
            }
            redirect.addFlashAttribute("flashSuccess",
                    "Đã đổi mật khẩu và thu hồi mọi phiên đăng nhập của người dùng");
            return "redirect:/admin/users/" + id;
        } catch (BadRequestException exception) {
            return form(id, model, exception.getFieldErrors().isEmpty()
                    ? Map.of("global", exception.getMessage()) : exception.getFieldErrors());
        }
    }

    private String form(Long id, Model model, Map<String, String> errors) {
        model.asMap().remove(BindingResult.MODEL_KEY_PREFIX + "form");
        model.addAttribute("form", new UserPasswordForm());
        model.addAttribute("user", userQueries.getDetail(id));
        model.addAttribute("errors", errors);
        model.addAttribute("pageTitle", "Đổi mật khẩu người dùng");
        return "admin/user-password";
    }
}
