package vn.edu.hcmute.qaute.controller.admin;

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
import org.springframework.web.bind.annotation.RequestParam;
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
import vn.edu.hcmute.qaute.dto.view.AuthSessionView;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.admin.AdminProfileService;
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho AdminProfile.
@Controller
@RequestMapping("/admin/profile")
public class AdminProfileController {
    private final AdminProfileService profiles;
    private final AuthSessionService sessions;

    // Khởi tạo bộ điều khiển hồ sơ quản trị viên.
    public AdminProfileController(AdminProfileService profiles, AuthSessionService sessions) {
        this.profiles = profiles;
        this.sessions = sessions;
    }

    @GetMapping
    // Hiển thị hồ sơ quản trị viên hiện tại.
    public String profile(@RequestParam(required = false) String section, Model model) {
        Long userId = currentAdminId();
        if ("security".equals(section)) {
            return "redirect:/admin/profile/security";
        }
        populate(model, userId);
        return "admin/profile";
    }

    @PostMapping
    // Kiểm tra và cập nhật hồ sơ quản trị viên.
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
    // Cập nhật ảnh đại diện quản trị viên.
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

    @GetMapping("/security")
    // Hiển thị mật khẩu và các phiên đăng nhập của quản trị viên hiện tại.
    public String security(Model model) {
        model.addAttribute("sessions", sessions.listSessions(currentAdminId(), currentJti()));
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new ChangePasswordForm());
        }
        return "admin/security";
    }

    @PostMapping({"/security/password", "/password"})
    // Đổi mật khẩu từ trang bảo mật tài khoản.
    public String changeSecurityPassword(@Valid @ModelAttribute("form") ChangePasswordForm form,
            BindingResult result, Model model, RedirectAttributes redirect) {
        Long userId = currentAdminId();
        if (form.getNewPassword() != null && !form.getNewPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "Mismatch", "Mật khẩu xác nhận không khớp");
        }
        if (!result.hasErrors()) {
            try {
                profiles.changePassword(userId, form);
                redirect.addFlashAttribute("flashSuccess",
                        "Đã đổi mật khẩu và đăng xuất các thiết bị khác");
                return "redirect:/admin/profile/security";
            } catch (BadRequestException exception) {
                model.addAttribute("errors", errors(result, exception));
            } catch (AppException exception) {
                redirect.addFlashAttribute("flashError", exception.getMessage());
                return "redirect:/admin/profile/security";
            } finally {
                clearPassword(form);
            }
        } else {
            model.addAttribute("errors", FormErrors.of(result));
        }
        clearPassword(form);
        model.asMap().remove(BindingResult.MODEL_KEY_PREFIX + "form");
        model.addAttribute("sessions", sessions.listSessions(userId, currentJti()));
        return "admin/security";
    }

    @PostMapping("/security/sessions/{id}/revoke")
    // Thu hồi phiên đăng nhập được chọn, không cho phép thu hồi phiên hiện tại.
    public String revokeSecuritySession(@PathVariable Long id, RedirectAttributes redirect) {
        Long userId = currentAdminId();
        List<AuthSessionView> activeSessions = sessions.listSessions(userId, currentJti());
        AuthSessionView target = activeSessions.stream()
                .filter(session -> session.id().equals(id))
                .findFirst()
                .orElse(null);
        if (target == null) {
            redirect.addFlashAttribute("flashError", "Không tìm thấy phiên đăng nhập");
        } else if (target.current()) {
            redirect.addFlashAttribute("flashError",
                    "Không thể thu hồi phiên hiện tại bằng nút này");
        } else {
            try {
                sessions.revokeSession(id, userId);
                redirect.addFlashAttribute("flashSuccess", "Đã thu hồi phiên đăng nhập");
            } catch (AppException exception) {
                redirect.addFlashAttribute("flashError", exception.getMessage());
            }
        }
        return "redirect:/admin/profile/security";
    }

    // Nạp hồ sơ và biểu mẫu vào trang.
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
    }

    // Xác minh quyền và lấy mã quản trị viên hiện tại.
    private static Long currentAdminId() {
        if (!SecurityUtils.hasRole(RoleCode.ADMIN)) {
            throw new ForbiddenException("Chỉ quản trị viên được truy cập trang này");
        }
        return SecurityUtils.requireUserId();
    }

    private static String currentJti() {
        return SecurityUtils.currentUser()
                .map(QauteUserDetails::getSessionJti)
                .orElse(null);
    }

    // Kết hợp lỗi kiểm tra với lỗi nghiệp vụ.
    private static Map<String, String> errors(BindingResult result, BadRequestException exception) {
        Map<String, String> errors = new LinkedHashMap<>(FormErrors.of(result));
        errors.putAll(exception.getFieldErrors());
        if (errors.isEmpty()) errors.put("global", exception.getMessage());
        return errors;
    }

    // Xóa các trường mật khẩu khỏi biểu mẫu.
    private static void clearPassword(ChangePasswordForm form) {
        form.setCurrentPassword(null);
        form.setNewPassword(null);
        form.setConfirmPassword(null);
    }
}
