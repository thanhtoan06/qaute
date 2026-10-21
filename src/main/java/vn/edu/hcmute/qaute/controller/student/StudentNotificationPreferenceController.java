package vn.edu.hcmute.qaute.controller.student;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.dto.request.student.PreferenceInput;
import vn.edu.hcmute.qaute.dto.view.PreferenceView;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.notification.NotificationService;

@Controller
public class StudentNotificationPreferenceController {
    private final NotificationService notifications;

    public StudentNotificationPreferenceController(NotificationService notifications) {
        this.notifications = notifications;
    }

    @GetMapping({"/student/notifications/preferences", "/student/notification-preferences"})
    public String preferences(Model model) {
        model.addAttribute("preferences", notifications.getPreferences(SecurityUtils.requireUserId()));
        return "student/notification-preferences";
    }

    @PostMapping("/student/notifications/preferences")
    public String save(@RequestParam Map<String, String> values, RedirectAttributes flash) {
        Long userId = SecurityUtils.requireUserId();
        List<PreferenceView> current = notifications.getPreferences(userId);
        Map<NotificationType, PreferenceInput> input = new EnumMap<>(NotificationType.class);
        for (PreferenceView preference : current) {
            String key = preference.type().name();
            input.put(preference.type(), new PreferenceInput(
                    values.containsKey("inApp." + key), values.containsKey("email." + key)));
        }
        notifications.savePreferences(userId, input);
        flash.addFlashAttribute("flashSuccess", "Đã lưu tùy chọn thông báo");
        return "redirect:/student/notifications/preferences";
    }
}
