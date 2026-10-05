package vn.edu.hcmute.qaute.controller.auth;

import jakarta.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.exception.RateLimitException;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.auth.RegisterForm;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.identity.RegistrationService;

@Controller
public class RegisterController {

    private final RegistrationService registrationService;

    public RegisterController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/auth/register")
    public String registerPage(Model model) {
        if (SecurityUtils.currentUser().isPresent()) {
            return dashboardRedirect();
        }
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new RegisterForm());
        }
        return "auth/register";
    }

    @PostMapping("/auth/register")
    public String register(@Valid @ModelAttribute("form") RegisterForm form,
                           BindingResult bindingResult, Model model,
                           RedirectAttributes redirectAttributes) {
        if (SecurityUtils.currentUser().isPresent()) {
            return dashboardRedirect();
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", FormErrors.of(bindingResult));
            return "auth/register";
        }
        try {
            String email = registrationService.register(form);
            redirectAttributes.addFlashAttribute("flashSuccess",
                    "Đã gửi mã OTP tới email của bạn");
            return "redirect:/auth/verify-otp?email="
                    + URLEncoder.encode(email, StandardCharsets.UTF_8);
        } catch (BadRequestException exception) {
            return showFormError(model, exception.getFieldErrors());
        } catch (DuplicateException exception) {
            Map<String, String> errors = exception.getField() == null
                    ? Map.of("global", exception.getMessage())
                    : Map.of(exception.getField(), exception.getMessage());
            return showFormError(model, errors);
        } catch (RateLimitException exception) {
            redirectAttributes.addFlashAttribute("flashError", exception.getMessage());
            return "redirect:/auth/register";
        }
    }

    private String showFormError(Model model, Map<String, String> additions) {
        Map<String, String> errors = new LinkedHashMap<>();
        Object existing = model.getAttribute("errors");
        if (existing instanceof Map<?, ?> existingErrors) {
            existingErrors.forEach((key, value) ->
                    errors.put(String.valueOf(key), String.valueOf(value)));
        }
        errors.putAll(additions);
        model.addAttribute("errors", errors);
        return "auth/register";
    }

    private String dashboardRedirect() {
        return SecurityUtils.currentUser()
                .map(QauteUserDetails::getRoleCode)
                .map(this::dashboardPath)
                .orElse("redirect:/");
    }

    private String dashboardPath(RoleCode role) {
        return switch (role) {
            case STUDENT -> "redirect:/student/dashboard";
            case MANAGER -> "redirect:/manager/dashboard";
            case ADMIN -> "redirect:/admin/dashboard";
        };
    }
}
