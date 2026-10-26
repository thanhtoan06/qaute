package vn.edu.hcmute.qaute.controller.student;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.dto.request.student.PreferenceInput;
import vn.edu.hcmute.qaute.dto.view.PreferenceView;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.notification.NotificationService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho StudentNotificationPreference.
@Controller
public class StudentNotificationPreferenceController {
    private final NotificationService notifications;

    // Khởi tạo bộ điều khiển tùy chọn thông báo.
    public StudentNotificationPreferenceController(NotificationService notifications) {
        this.notifications = notifications;
    }

    @GetMapping({
            "/{role:student|manager|admin}/notifications/preferences",
            "/{role:student|manager|admin}/notification-preferences"
    })
    // Hiển thị tùy chọn nhận thông báo của vai trò hiện tại.
    public String preferences(@PathVariable String role, Model model) {
        RoleCode roleCode = requireMatchingRole(role);
        model.addAttribute("preferences", notifications.getPreferences(SecurityUtils.requireUserId(), roleCode));
        model.addAttribute("notificationRole", role);
        model.addAttribute("notificationPreferencesUrl", "/" + role + "/notifications/preferences");
        return "student/notification-preferences";
    }

    @PostMapping("/{role:student|manager|admin}/notifications/preferences")
    // Lưu các kênh nhận thông báo đã chọn.
    public String save(@PathVariable String role, @RequestParam Map<String, String> values,
                       RedirectAttributes flash) {
        RoleCode roleCode = requireMatchingRole(role);
        Long userId = SecurityUtils.requireUserId();
        List<PreferenceView> current = notifications.getPreferences(userId, roleCode);
        Map<NotificationType, PreferenceInput> input = new EnumMap<>(NotificationType.class);
        for (PreferenceView preference : current) {
            String key = preference.type().name();
            input.put(preference.type(), new PreferenceInput(
                    values.containsKey("inApp." + key), values.containsKey("email." + key)));
        }
        notifications.savePreferences(userId, roleCode, input);
        flash.addFlashAttribute("flashSuccess", "Đã lưu tùy chọn thông báo");
        return "redirect:/" + role + "/notifications/preferences";
    }

    private static RoleCode requireMatchingRole(String role) {
        RoleCode requestedRole = RoleCode.valueOf(role.toUpperCase(Locale.ROOT));
        QauteUserDetails currentUser = SecurityUtils.requireUser();
        if (currentUser.getRoleCode() != requestedRole) {
            throw new ForbiddenException("Không có quyền truy cập tùy chọn thông báo của vai trò khác");
        }
        return requestedRole;
    }
}
