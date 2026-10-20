package vn.edu.hcmute.qaute.controller.student;

import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.view.NotificationView;
import vn.edu.hcmute.qaute.entity.notification.Notification;
import vn.edu.hcmute.qaute.repository.notification.NotificationRepository;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.notification.NotificationService;

@Controller
public class StudentNotificationController {
    private static final int PAGE_SIZE = 15;

    private final NotificationService notifications;
    private final NotificationRepository notificationRepository;

    public StudentNotificationController(NotificationService notifications,
                                         NotificationRepository notificationRepository) {
        this.notifications = notifications;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/student/notifications")
    public String list(@RequestParam(defaultValue = "false") boolean unread,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        Long userId = SecurityUtils.requireUserId();
        Page<NotificationView> notificationPage = notifications.list(userId, unread,
                PageRequest.of(Math.max(0, page), PAGE_SIZE));
        model.addAttribute("page", notificationPage);
        model.addAttribute("unread", unread);
        model.addAttribute("unreadCount", notifications.unreadCount(userId));
        return "student/notifications";
    }

    @PostMapping("/student/notifications/{id}/open")
    public String open(@PathVariable Long id) {
        Long userId = SecurityUtils.requireUserId();
        Notification notification = notificationRepository.findById(id)
                .filter(item -> item.getDeletedAt() == null && Objects.equals(item.getUserId(), userId))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông báo"));
        notifications.markRead(userId, id);
        String link = notification.getLinkUrl() == null ? "" : notification.getLinkUrl().trim();
        return isInternalLink(link) ? "redirect:" + link : "redirect:/student/notifications";
    }

    @PostMapping("/student/notifications/read-all")
    public String readAll(@RequestParam(defaultValue = "false") boolean unread,
                          RedirectAttributes flash) {
        notifications.markAllRead(SecurityUtils.requireUserId());
        flash.addFlashAttribute("flashSuccess", "Đã đánh dấu tất cả thông báo là đã đọc");
        return listRedirect(unread);
    }

    @PostMapping("/student/notifications/{id}/hide")
    public String hide(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean unread,
                       RedirectAttributes flash) {
        notifications.hide(SecurityUtils.requireUserId(), id);
        flash.addFlashAttribute("flashSuccess", "Đã ẩn thông báo");
        return listRedirect(unread);
    }

    private static boolean isInternalLink(String link) {
        return link.startsWith("/") && !link.startsWith("//");
    }

    private static String listRedirect(boolean unread) {
        return unread ? "redirect:/student/notifications?unread=true" : "redirect:/student/notifications";
    }
}
