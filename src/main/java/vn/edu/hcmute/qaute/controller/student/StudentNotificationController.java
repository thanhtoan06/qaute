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

// Tiếp nhận và điều phối các yêu cầu giao diện thông báo cho người dùng.
@Controller
public class StudentNotificationController {
    private static final int PAGE_SIZE = 15;

    private final NotificationService notifications;
    private final NotificationRepository notificationRepository;

    // Khởi tạo bộ điều khiển thông báo và kho dữ liệu thông báo.
    public StudentNotificationController(NotificationService notifications,
                                         NotificationRepository notificationRepository) {
        this.notifications = notifications;
        this.notificationRepository = notificationRepository;
    }

    // Hiển thị danh sách thông báo theo bộ lọc chưa đọc và trang.
    @GetMapping("/{role:student|manager|admin}/notifications")
    public String list(@PathVariable String role,
                       @RequestParam(defaultValue = "false") boolean unread,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        Long userId = SecurityUtils.requireUserId();
        Page<NotificationView> notificationPage = notifications.list(userId, unread,
                PageRequest.of(Math.max(0, page), PAGE_SIZE));
        String baseUrl = "/" + role + "/notifications";
        model.addAttribute("page", notificationPage);
        model.addAttribute("unread", unread);
        model.addAttribute("unreadCount", notifications.unreadCount(userId));
        model.addAttribute("notificationBaseUrl", baseUrl);
        model.addAttribute("notificationPreferencesUrl", "/" + role + "/notifications/preferences");
        model.addAttribute("notificationDashboardUrl", "/" + role + "/dashboard");
        if ("manager".equals(role)) {
            return "manager/notifications";
        }
        if ("admin".equals(role)) {
            return "admin/notifications";
        }
        return "student/notifications";
    }

    @PostMapping("/{role:student|manager|admin}/notifications/{id}/open")
    // Đánh dấu thông báo đã đọc và mở liên kết nội bộ nếu hợp lệ.
    public String open(@PathVariable String role, @PathVariable Long id) {
        Long userId = SecurityUtils.requireUserId();
        Notification notification = notificationRepository.findById(id)
                .filter(item -> item.getDeletedAt() == null && Objects.equals(item.getUserId(), userId))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông báo"));
        notifications.markRead(userId, id);
        String link = notification.getLinkUrl() == null ? "" : notification.getLinkUrl().trim();
        return isInternalLink(link) ? "redirect:" + link : "redirect:/" + role + "/notifications";
    }

    @PostMapping("/{role:student|manager|admin}/notifications/read-all")
    // Đánh dấu toàn bộ thông báo của sinh viên là đã đọc.
    public String readAll(@PathVariable String role,
                          @RequestParam(defaultValue = "false") boolean unread,
                          RedirectAttributes flash) {
        notifications.markAllRead(SecurityUtils.requireUserId());
        flash.addFlashAttribute("flashSuccess", "Đã đánh dấu tất cả thông báo là đã đọc");
        return listRedirect(role, unread);
    }

    @PostMapping("/{role:student|manager|admin}/notifications/{id}/hide")
    // Ẩn thông báo và quay lại danh sách tương ứng.
    public String hide(@PathVariable String role, @PathVariable Long id,
                       @RequestParam(defaultValue = "false") boolean unread,
                       RedirectAttributes flash) {
        notifications.hide(SecurityUtils.requireUserId(), id);
        flash.addFlashAttribute("flashSuccess", "Đã ẩn thông báo");
        return listRedirect(role, unread);
    }

    // Kiểm tra liên kết có phải đường dẫn nội bộ hay không.
    private static boolean isInternalLink(String link) {
        return link.startsWith("/") && !link.startsWith("//");
    }

    // Tạo đường dẫn trở lại danh sách thông báo theo bộ lọc.
    private static String listRedirect(String role, boolean unread) {
        return unread ? "redirect:/" + role + "/notifications?unread=true"
                : "redirect:/" + role + "/notifications";
    }
}
