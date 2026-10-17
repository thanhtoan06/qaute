package vn.edu.hcmute.qaute.controller.auth;

import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.auth.ActivateAccountForm;
import vn.edu.hcmute.qaute.service.identity.AccountActivationService;

@Controller
public class ActivateAccountController {
    private final AccountActivationService activation;

    public ActivateAccountController(AccountActivationService activation) {
        this.activation = activation;
    }

    @GetMapping("/auth/activate")
    public String page(@RequestParam(defaultValue = "") String token, Model model) {
        ActivateAccountForm form = new ActivateAccountForm();
        form.setToken(token);
        model.addAttribute("form", form);
        model.addAttribute("validToken", activation.isTokenValid(token));
        return "auth/activate";
    }

    @PostMapping("/auth/activate")
    public String activate(@Valid @ModelAttribute("form") ActivateAccountForm form,
                           BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("errors", FormErrors.of(result));
            model.addAttribute("validToken", activation.isTokenValid(form.getToken()));
            return "auth/activate";
        }
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("errors", Map.of("confirmPassword", "Mật khẩu nhập lại không khớp"));
            model.addAttribute("validToken", activation.isTokenValid(form.getToken()));
            return "auth/activate";
        }
        try {
            activation.activate(form.getToken(), form.getNewPassword());
            redirect.addFlashAttribute("flashSuccess", "Kích hoạt thành công, hãy đăng nhập");
            return "redirect:/auth/login";
        } catch (AppException e) {
            model.addAttribute("errors", Map.of("global", e.getMessage()));
            model.addAttribute("validToken", false);
            return "auth/activate";
        }
    }
}
