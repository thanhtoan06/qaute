package vn.edu.hcmute.qaute.controller.student;

import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.student.ChangePasswordForm;
import vn.edu.hcmute.qaute.dto.view.AuthSessionView;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;
import vn.edu.hcmute.qaute.service.identity.PasswordService;

@Controller
public class StudentSecurityController {

    private final PasswordService passwordService;
    private final AuthSessionService authSessionService;

    public StudentSecurityController(PasswordService passwordService,
                                   AuthSessionService authSessionService) {
        this.passwordService = passwordService;
        this.authSessionService = authSessionService;
    }

    @GetMapping({"/student/profile/security", "/student/security"})
    public String security(Model model) {
        Long userId = SecurityUtils.requireUserId();
        String currentJti = SecurityUtils.currentUser()
                .map(QauteUserDetails::getSessionJti)
                .orElse(null);

        List<AuthSessionView> sessions = authSessionService.listSessions(userId, currentJti);
        model.addAttribute("sessions", sessions);

        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new ChangePasswordForm());
        }
        return "student/security";
    }

    @PostMapping("/student/profile/security/password")
    public String changePassword(@Valid @ModelAttribute("form") ChangePasswordForm form,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        Long userId = SecurityUtils.requireUserId();
        String currentJti = SecurityUtils.currentUser()
                .map(QauteUserDetails::getSessionJti)
                .orElse(null);

        if (form.getNewPassword() != null && form.getConfirmPassword() != null
                && !form.getNewPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "Mismatch", "Mật khẩu xác nhận không khớp");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", FormErrors.of(bindingResult));
            model.addAttribute("sessions", authSessionService.listSessions(userId, currentJti));
            return "student/security";
        }

        try {
            passwordService.changePassword(userId, form.getCurrentPassword(), form.getNewPassword());
            redirectAttributes.addFlashAttribute("flashSuccess",
                    "Đã đổi mật khẩu, các thiết bị khác đã bị đăng xuất");
            return "redirect:/student/profile/security";
        } catch (BadRequestException e) {
            Map<String, String> errors = new LinkedHashMap<>(FormErrors.of(bindingResult));
            errors.putAll(e.getFieldErrors());
            if (e.getFieldErrors().isEmpty() && e.getMessage() != null) {
                errors.putIfAbsent("global", e.getMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("sessions", authSessionService.listSessions(userId, currentJti));
            return "student/security";
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("flashError", e.getMessage());
            return "redirect:/student/profile/security";
        }
    }

    @PostMapping("/student/profile/sessions/{id}/revoke")
    public String revokeSession(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        Long userId = SecurityUtils.requireUserId();
        String currentJti = SecurityUtils.currentUser()
                .map(QauteUserDetails::getSessionJti)
                .orElse(null);

        List<AuthSessionView> sessions = authSessionService.listSessions(userId, currentJti);
        AuthSessionView target = sessions.stream()
                .filter(s -> s.id().equals(id))
                .findFirst()
                .orElse(null);

        if (target == null) {
            redirectAttributes.addFlashAttribute("flashError", "Không tìm thấy phiên đăng nhập");
            return "redirect:/student/profile/security";
        }

        if (target.current()) {
            redirectAttributes.addFlashAttribute("flashError",
                    "Không cho thu hồi phiên hiện tại bằng nút này");
            return "redirect:/student/profile/security";
        }

        try {
            authSessionService.revokeSession(id, userId);
            redirectAttributes.addFlashAttribute("flashSuccess", "Đã thu hồi phiên đăng nhập thiết bị");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/student/profile/security";
    }

    @PostMapping("/student/profile/sessions/revoke-others")
    public String revokeOtherSessions(RedirectAttributes redirectAttributes) {
        Long userId = SecurityUtils.requireUserId();
        String currentJti = SecurityUtils.currentUser()
                .map(QauteUserDetails::getSessionJti)
                .orElse(null);

        try {
            authSessionService.revokeAllExcept(userId, currentJti, "user_revoked_others");
            redirectAttributes.addFlashAttribute("flashSuccess", "Đã đăng xuất tất cả các thiết bị khác");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/student/profile/security";
    }
}

