package vn.edu.hcmute.qaute.controller.auth;

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
import vn.edu.hcmute.qaute.common.constant.OtpPurpose;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.RateLimitException;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.auth.VerifyOtpForm;
import vn.edu.hcmute.qaute.service.identity.OtpService;
import vn.edu.hcmute.qaute.service.identity.RegistrationService;

@Controller
public class OtpController {

    private final RegistrationService registrationService;
    private final OtpService otpService;

    public OtpController(RegistrationService registrationService, OtpService otpService) {
        this.registrationService = registrationService;
        this.otpService = otpService;
    }

    @GetMapping("/auth/verify-otp")
    public String verifyPage(@RequestParam(defaultValue = "") String email, Model model) {
        String normalizedEmail = email.trim().toLowerCase();
        VerifyOtpForm form = new VerifyOtpForm();
        form.setEmail(normalizedEmail);
        model.addAttribute("form", form);
        model.addAttribute("email", normalizedEmail);
        model.addAttribute("maskedEmail", maskEmail(normalizedEmail));
        model.addAttribute("secondsUntilResend",
                otpService.secondsUntilResend(normalizedEmail, OtpPurpose.REGISTER_VERIFY));
        model.addAttribute("ttlMinutes", registrationService.otpTtlMinutes());
        return "auth/verify-otp";
    }

    @PostMapping("/auth/verify-otp")
    public String verify(@Valid @ModelAttribute("form") VerifyOtpForm form,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return showErrors(form, model, FormErrors.of(bindingResult));
        }
        try {
            registrationService.verifyRegistration(form.getEmail(), form.getOtp());
            redirectAttributes.addFlashAttribute("flashSuccess",
                    "Xác thực thành công, hãy đăng nhập");
            return "redirect:/auth/login";
        } catch (AppException exception) {
            return showErrors(form, model, Map.of("otp", exception.getMessage()));
        }
    }

    @PostMapping("/auth/resend-otp")
    public String resend(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            registrationService.resendOtp(email);
            redirectAttributes.addFlashAttribute("flashSuccess", "Đã gửi lại mã OTP");
        } catch (RateLimitException exception) {
            redirectAttributes.addFlashAttribute("flashError", exception.getMessage());
        }
        return "redirect:/auth/verify-otp?email="
                + URLEncoder.encode(email.trim(), StandardCharsets.UTF_8);
    }

    private String showErrors(VerifyOtpForm form, Model model, Map<String, String> errors) {
        model.addAttribute("email", form.getEmail());
        model.addAttribute("maskedEmail", maskEmail(form.getEmail()));
        model.addAttribute("secondsUntilResend",
                otpService.secondsUntilResend(form.getEmail(), OtpPurpose.REGISTER_VERIFY));
        model.addAttribute("ttlMinutes", registrationService.otpTtlMinutes());
        model.addAttribute("errors", errors);
        return "auth/verify-otp";
    }

    private String maskEmail(String email) {
        int at = email.indexOf('@');
        if (at <= 0) {
            return email;
        }
        String local = email.substring(0, at);
        return local.substring(0, 1) + "***" + email.substring(at);
    }
}
