package vn.edu.hcmute.qaute.controller.auth;

import jakarta.validation.Valid;
import java.util.Objects;
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

// Tiếp nhận và điều phối các yêu cầu giao diện cho ActivateAccount.
@Controller
public class ActivateAccountController {

    private final AccountActivationService accountActivationService;

    // Khởi tạo bộ điều khiển kích hoạt tài khoản.
    public ActivateAccountController(AccountActivationService accountActivationService) {
        this.accountActivationService = accountActivationService;
    }

    @GetMapping("/auth/activate")
    // Hiển thị trang kích hoạt tài khoản.
    public String activatePage(@RequestParam(defaultValue = "") String token, Model model) {
        if (token.isBlank() || !accountActivationService.isTokenValid(token)) {
            model.addAttribute("tokenValid", false);
            model.addAttribute("errorMessage", "Liên kết đã hết hạn, hãy nhờ quản trị gửi lại lời mời");
            return "auth/activate";
        }

        model.addAttribute("tokenValid", true);
        if (!model.containsAttribute("form")) {
            ActivateAccountForm form = new ActivateAccountForm();
            form.setToken(token);
            model.addAttribute("form", form);
        }
        return "auth/activate";
    }

    @PostMapping("/auth/activate")
    // Kiểm tra thông tin và kích hoạt tài khoản.
    public String activate(@Valid @ModelAttribute("form") ActivateAccountForm form,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (!Objects.equals(form.getNewPassword(), form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Mật khẩu nhập lại không khớp");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("tokenValid", true);
            model.addAttribute("errors", FormErrors.of(bindingResult));
            return "auth/activate";
        }

        try {
            accountActivationService.activate(form.getToken(), form.getNewPassword());
            redirectAttributes.addFlashAttribute("flashSuccess", "Kích hoạt thành công");
            return "redirect:/auth/login";
        } catch (AppException exception) {
            model.addAttribute("tokenValid", false);
            model.addAttribute("errorMessage", exception.getMessage());
            return "auth/activate";
        }
    }
}
