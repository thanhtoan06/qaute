package vn.edu.hcmute.qaute.controller.admin;

import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.admin.AdminAvatarForm;
import vn.edu.hcmute.qaute.dto.request.admin.AdminProfileForm;
import vn.edu.hcmute.qaute.dto.request.student.ChangePasswordForm;
import vn.edu.hcmute.qaute.dto.view.AdminProfileView;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.admin.AdminProfileService;

@Controller
@RequestMapping("/admin/profile")
public class AdminProfileController {
    private final AdminProfileService profiles;

    public AdminProfileController(AdminProfileService profiles) {
        this.profiles = profiles;
    }

    @GetMapping
    public String profile(Model model) {
        populate(model, currentAdminId());
        return "admin/profile";
    }

    @PostMapping
    public String update(@Valid @ModelAttribute("form") AdminProfileForm form,
            BindingResult result, Model model, RedirectAttributes redirect) {
        Long userId = currentAdminId();
        if (!result.hasErrors()) {
            try {
                profiles.update(userId, form);
                redirect.addFlashAttribute("flashSuccess", "Đã cập nhật hồ sơ quản trị viên");
                return "redirect:/admin/profile";
            } catch (DuplicateException exception) {
                result.rejectValue("email", "Duplicate", exception.getMessage());
            } catch (BadRequestException exception) {
                model.addAttribute("errors", errors(result, exception));
            } catch (AppException exception) {
                redirect.addFlashAttribute("flashError", exception.getMessage());
                return "redirect:/admin/profile";
            }
        }
        if (!model.containsAttribute("errors")) model.addAttribute("errors", FormErrors.of(result));
        populate(model, userId);
        return "admin/profile";
    }

    @PostMapping("/avatar")
    public String avatar(@Valid @ModelAttribute AdminAvatarForm form, BindingResult result,
            RedirectAttributes redirect) {
        Long userId = currentAdminId();
        if (result.hasErrors()) {
            redirect.addFlashAttribute("flashError", "Vui lòng chọn ảnh đại diện hợp lệ");
        } else {
            try {
                profiles.updateAvatar(userId, form.getAssetId());
                redirect.addFlashAttribute("flashSuccess", "Đã cập nhật ảnh đại diện");
            } catch (AppException exception) {
                redirect.addFlashAttribute("flashError", exception.getMessage());
            }
        }
        return "redirect:/admin/profile";
    }

    @PostMapping("/password")
    public String changePassword(@Valid @ModelAttribute("passwordForm") ChangePasswordForm form,
            BindingResult result, Model model, RedirectAttributes redirect) {
        Long userId = currentAdminId();
        if (form.getNewPassword() != null && !form.getNewPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "Mismatch", "Mật khẩu xác nhận không khớp");
        }
        if (!result.hasErrors()) {
            try {
                profiles.changePassword(userId, form);
                redirect.addFlashAttribute("flashSuccess", "Đã đổi mật khẩu và đăng xuất các thiết bị khác");
                return "redirect:/admin/profile";
            } catch (BadRequestException exception) {
                model.addAttribute("passwordErrors", errors(result, exception));
            } catch (AppException exception) {
                redirect.addFlashAttribute("flashError", exception.getMessage());
                return "redirect:/admin/profile";
            } finally {
                clearPassword(form);
            }
        }
        if (!model.containsAttribute("passwordErrors")) {
            model.addAttribute("passwordErrors", FormErrors.of(result));
        }
        clearPassword(form);
        model.asMap().remove(BindingResult.MODEL_KEY_PREFIX + "passwordForm");
        populate(model, userId);
        return "admin/profile";
    }

    private void populate(Model model, Long userId) {
        AdminProfileView profile = profiles.get(userId);
        model.addAttribute("profile", profile);
        if (!model.containsAttribute("form")) {
            AdminProfileForm form = new AdminProfileForm();
            form.setFullName(profile.fullName());
            form.setEmail(profile.email());
            form.setPhone(profile.phone());
            model.addAttribute("form", form);
        }
        if (!model.containsAttribute("passwordForm")) {
            model.addAttribute("passwordForm", new ChangePasswordForm());
        }
    }

    private static Long currentAdminId() {
        if (!SecurityUtils.hasRole(RoleCode.ADMIN)) {
            throw new ForbiddenException("Chỉ quản trị viên được truy cập trang này");
        }
        return SecurityUtils.requireUserId();
    }

    private static Map<String, String> errors(BindingResult result, BadRequestException exception) {
        Map<String, String> errors = new LinkedHashMap<>(FormErrors.of(result));
        errors.putAll(exception.getFieldErrors());
        if (errors.isEmpty()) errors.put("global", exception.getMessage());
        return errors;
    }

    private static void clearPassword(ChangePasswordForm form) {
        form.setCurrentPassword(null);
        form.setNewPassword(null);
        form.setConfirmPassword(null);
    }
}
