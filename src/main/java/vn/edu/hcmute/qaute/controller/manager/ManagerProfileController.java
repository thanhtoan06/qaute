package vn.edu.hcmute.qaute.controller.manager;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.manager.ManagerAvatarForm;
import vn.edu.hcmute.qaute.dto.request.manager.ManagerPresenceForm;
import vn.edu.hcmute.qaute.dto.request.manager.ManagerProfileForm;
import vn.edu.hcmute.qaute.dto.request.student.ChangePasswordForm;
import vn.edu.hcmute.qaute.dto.view.AuthSessionView;
import vn.edu.hcmute.qaute.dto.view.ManagerProfileView;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;
import vn.edu.hcmute.qaute.service.identity.PasswordService;
import vn.edu.hcmute.qaute.service.manager.ManagerProfileService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho ManagerProfile.
@Controller
@RequestMapping("/manager/profile")
public class ManagerProfileController {
    private final ManagerProfileService profiles;
    private final PasswordService passwords;
    private final AuthSessionService sessions;

    // Khởi tạo bộ điều khiển hồ sơ quản lý.
    public ManagerProfileController(ManagerProfileService profiles, PasswordService passwords,
            AuthSessionService sessions) {
        this.profiles = profiles;
        this.passwords = passwords;
        this.sessions = sessions;
    }

    @GetMapping
    // Hiển thị hồ sơ và biểu mẫu cập nhật thông tin.
    public String profile(Model model) {
        ManagerProfileView profile = profiles.get(SecurityUtils.requireUserId());
        model.addAttribute("profile", profile);
        if (!model.containsAttribute("form")) {
            ManagerProfileForm form = new ManagerProfileForm();
            form.setFullName(profile.fullName());
            form.setPhone(profile.phone());
            form.setJobTitle(profile.jobTitle());
            form.setBio(profile.bio());
            model.addAttribute("form", form);
        }
        return "manager/profile";
    }

    @PostMapping
    // Cập nhật thông tin hồ sơ quản lý.
    public String update(@Valid @ModelAttribute("form") ManagerProfileForm form,
            BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("errors", FormErrors.of(result));
            model.addAttribute("profile", profiles.get(SecurityUtils.requireUserId()));
            return "manager/profile";
        }
        try {
            profiles.update(SecurityUtils.requireUserId(), form);
            redirect.addFlashAttribute("flashSuccess", "Đã cập nhật hồ sơ");
            return "redirect:/manager/profile";
        } catch (BadRequestException exception) {
            Map<String, String> errors = new LinkedHashMap<>(FormErrors.of(result));
            errors.putAll(exception.getFieldErrors());
            if (errors.isEmpty()) errors.put("global", exception.getMessage());
            model.addAttribute("errors", errors);
            model.addAttribute("profile", profiles.get(SecurityUtils.requireUserId()));
            return "manager/profile";
        } catch (AppException exception) {
            redirect.addFlashAttribute("flashError", exception.getMessage());
            return "redirect:/manager/profile";
        }
    }

    @PostMapping("/avatar")
    // Cập nhật ảnh đại diện của quản lý.
    public String avatar(@Valid @ModelAttribute ManagerAvatarForm form, BindingResult errors,
                         RedirectAttributes redirect) {
        if (errors.hasErrors()) {
            redirect.addFlashAttribute("flashError", "Chọn ảnh đại diện hợp lệ");
            return "redirect:/manager/profile";
        }
        try {
            profiles.updateAvatar(SecurityUtils.requireUserId(), form.getAssetId());
            redirect.addFlashAttribute("flashSuccess", "Đã cập nhật ảnh đại diện");
        } catch (AppException exception) {
            redirect.addFlashAttribute("flashError", exception.getMessage());
        }
        return "redirect:/manager/profile";
    }

    @PostMapping("/status")
    // Cập nhật trạng thái trực của quản lý.
    public String status(@Valid @ModelAttribute ManagerPresenceForm form, BindingResult errors,
                         RedirectAttributes flash) {
        if (errors.hasErrors()) {
            flash.addFlashAttribute("flashError", "Trạng thái trực không hợp lệ");
            return "redirect:/manager/profile";
        }
        try {
            profiles.setStatus(SecurityUtils.requireUserId(), form.getStatus());
            flash.addFlashAttribute("flashSuccess", "Đã cập nhật trạng thái trực");
        } catch (AppException exception) {
            flash.addFlashAttribute("flashError", exception.getMessage());
        }
        return ManagerTicketActionController.redirectTo(form.getRedirect());
    }

    @GetMapping("/security")
    // Hiển thị các phiên đăng nhập và biểu mẫu bảo mật.
    public String security(Model model) {
        model.addAttribute("sessions", sessions.listSessions(SecurityUtils.requireUserId(), currentJti()));
        if (!model.containsAttribute("form")) model.addAttribute("form", new ChangePasswordForm());
        return "manager/security";
    }

    @PostMapping("/security/password")
    // Đổi mật khẩu và hiển thị lỗi xác thực nếu có.
    public String changePassword(@Valid @ModelAttribute("form") ChangePasswordForm form,
            BindingResult result, Model model, RedirectAttributes redirect) {
        if (form.getNewPassword() != null && form.getConfirmPassword() != null
                && !form.getNewPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "Mismatch", "Mật khẩu xác nhận không khớp");
        }
        if (result.hasErrors()) {
            model.addAttribute("errors", FormErrors.of(result));
            model.addAttribute("sessions", sessions.listSessions(SecurityUtils.requireUserId(), currentJti()));
            return "manager/security";
        }
        try {
            passwords.changePassword(SecurityUtils.requireUserId(),
                    form.getCurrentPassword(), form.getNewPassword());
            redirect.addFlashAttribute("flashSuccess", "Đã đổi mật khẩu và đăng xuất các thiết bị khác");
            return "redirect:/manager/profile/security";
        } catch (BadRequestException exception) {
            Map<String, String> errors = new LinkedHashMap<>(FormErrors.of(result));
            errors.putAll(exception.getFieldErrors());
            if (errors.isEmpty()) errors.put("global", exception.getMessage());
            model.addAttribute("errors", errors);
            model.addAttribute("sessions", sessions.listSessions(SecurityUtils.requireUserId(), currentJti()));
            return "manager/security";
        } catch (AppException exception) {
            redirect.addFlashAttribute("flashError", exception.getMessage());
            return "redirect:/manager/profile/security";
        }
    }

    @PostMapping("/sessions/{id}/revoke")
    // Thu hồi phiên đăng nhập được chọn.
    public String revokeSession(@PathVariable Long id, RedirectAttributes redirect) {
        Long userId = SecurityUtils.requireUserId();
        List<AuthSessionView> active = sessions.listSessions(userId, currentJti());
        AuthSessionView target = active.stream().filter(session -> session.id().equals(id))
                .findFirst().orElse(null);
        if (target == null) {
            redirect.addFlashAttribute("flashError", "Không tìm thấy phiên đăng nhập");
        } else if (target.current()) {
            redirect.addFlashAttribute("flashError", "Không thể thu hồi phiên hiện tại bằng nút này");
        } else {
            try {
                sessions.revokeSession(id, userId);
                redirect.addFlashAttribute("flashSuccess", "Đã thu hồi phiên đăng nhập");
            } catch (AppException exception) {
                redirect.addFlashAttribute("flashError", exception.getMessage());
            }
        }
        return "redirect:/manager/profile/security";
    }

    // Lấy mã phiên đăng nhập hiện tại của người dùng.
    private static String currentJti() {
        return SecurityUtils.currentUser().map(QauteUserDetails::getSessionJti).orElse(null);
    }
}
