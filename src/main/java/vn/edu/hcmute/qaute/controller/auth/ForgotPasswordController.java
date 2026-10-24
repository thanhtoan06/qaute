package vn.edu.hcmute.qaute.controller.auth;

import jakarta.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.RateLimitException;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.auth.ForgotPasswordForm;
import vn.edu.hcmute.qaute.dto.request.auth.ResetOtpForm;
import vn.edu.hcmute.qaute.dto.request.auth.ResetPasswordForm;
import vn.edu.hcmute.qaute.service.identity.PasswordResetService;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@Controller
public class ForgotPasswordController {

    private final PasswordResetService passwordResetService;
    private final SettingsService settingsService;

    public ForgotPasswordController(PasswordResetService passwordResetService,
                                    SettingsService settingsService) {
        this.passwordResetService = passwordResetService;
        this.settingsService = settingsService;
    }

    @GetMapping("/auth/forgot-password")
    public String forgotPasswordPage(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new ForgotPasswordForm());
        }
        return "auth/forgot-password";
    }

    @PostMapping("/auth/forgot-password")
    public String forgotPassword(@Valid @ModelAttribute("form") ForgotPasswordForm form,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", FormErrors.of(bindingResult));
            return "auth/forgot-password";
        }

        String identifier = form.getIdentifier().trim();
        try {
            passwordResetService.start(identifier);
            redirectAttributes.addFlashAttribute("flashSuccess",
                    "Nếu tài khoản tồn tại, mã OTP đã được gửi tới email");
        } catch (RateLimitException exception) {
            redirectAttributes.addFlashAttribute("flashError", exception.getMessage());
        }

        return "redirect:/auth/reset-password?identifier="
                + URLEncoder.encode(identifier, StandardCharsets.UTF_8);
    }

    @GetMapping("/auth/reset-password")
    public String resetPasswordPage(@RequestParam(defaultValue = "") String identifier,
                                    Model model) {
        model.addAttribute("step", "otp");
        model.addAttribute("identifier", identifier);
        model.addAttribute("ttlMinutes", settingsService.getInt(SettingKeys.OTP_TTL_MINUTES));
        model.addAttribute("cooldownSeconds", settingsService.getInt(SettingKeys.OTP_RESEND_COOLDOWN_SECONDS));
        passwordResetService.getMaskedEmail(identifier).ifPresent(masked ->
                model.addAttribute("maskedEmail", masked));

        if (!model.containsAttribute("form")) {
            ResetOtpForm form = new ResetOtpForm();
            form.setIdentifier(identifier);
            model.addAttribute("form", form);
        }
        return "auth/reset-password";
    }

    @PostMapping("/auth/reset-password/verify")
    public String verifyOtp(@Valid @ModelAttribute("form") ResetOtpForm form,
                            BindingResult bindingResult,
                            Model model) {
        model.addAttribute("step", "otp");
        model.addAttribute("identifier", form.getIdentifier());
        model.addAttribute("ttlMinutes", settingsService.getInt(SettingKeys.OTP_TTL_MINUTES));
        model.addAttribute("cooldownSeconds", settingsService.getInt(SettingKeys.OTP_RESEND_COOLDOWN_SECONDS));
        passwordResetService.getMaskedEmail(form.getIdentifier()).ifPresent(masked ->
                model.addAttribute("maskedEmail", masked));

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", FormErrors.of(bindingResult));
            return "auth/reset-password";
        }

        try {
            String rawToken = passwordResetService.verifyOtpAndIssueToken(
                    form.getIdentifier().trim(), form.getOtp());
            model.addAttribute("step", "password");
            ResetPasswordForm resetForm = new ResetPasswordForm();
            resetForm.setToken(rawToken);
            model.addAttribute("passwordForm", resetForm);
            return "auth/reset-password";
        } catch (AppException exception) {
            model.addAttribute("errors", Map.of("otp", exception.getMessage()));
            return "auth/reset-password";
        }
    }

    @PostMapping("/auth/reset-password")
    public String resetPassword(@Valid @ModelAttribute("passwordForm") ResetPasswordForm form,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (!Objects.equals(form.getNewPassword(), form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Mật khẩu nhập lại không khớp");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("step", "password");
            model.addAttribute("errors", FormErrors.of(bindingResult));
            return "auth/reset-password";
        }

        try {
            passwordResetService.resetPassword(form.getToken(), form.getNewPassword());
            redirectAttributes.addFlashAttribute("flashSuccess", "Đã đặt lại mật khẩu, hãy đăng nhập");
            return "redirect:/auth/login";
        } catch (AppException exception) {
            model.addAttribute("step", "password");
            model.addAttribute("errors", Map.of("global", exception.getMessage()));
            return "auth/reset-password";
        }
    }
}
