package vn.edu.hcmute.qaute.controller.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.dto.view.NotificationView;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.notification.NotificationService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho NotificationApi.
@RestController
@RequestMapping("/api/notifications")
public class NotificationApiController {

    private final NotificationService notificationService;

    // Khởi tạo bộ điều khiển thông báo.
    public NotificationApiController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    // Lấy danh sách thông báo theo trang và trạng thái đã đọc.
    public ApiResponse<Page<NotificationView>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean unreadOnly) {
        Long userId = SecurityUtils.requireUserId();
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.min(Math.max(1, size), 50));
        return ApiResponse.ok(notificationService.list(userId, unreadOnly, pageable));
    }

    @GetMapping("/unread-count")
    // Lấy số lượng thông báo chưa đọc.
    public ApiResponse<Long> unreadCount() {
        Long userId = SecurityUtils.requireUserId();
        return ApiResponse.ok(notificationService.unreadCount(userId));
    }

    @PostMapping("/{id}/read")
    // Đánh dấu một thông báo là đã đọc.
    public ApiResponse<Void> markRead(@PathVariable Long id) {
        Long userId = SecurityUtils.requireUserId();
        notificationService.markRead(userId, id);
        return ApiResponse.ok(null);
    }

    @PostMapping("/read-all")
    // Đánh dấu tất cả thông báo là đã đọc.
    public ApiResponse<Void> markAllRead() {
        Long userId = SecurityUtils.requireUserId();
        notificationService.markAllRead(userId);
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/{id}")
    // Ẩn một thông báo khỏi danh sách của người dùng.
    public ApiResponse<Void> hide(@PathVariable Long id) {
        Long userId = SecurityUtils.requireUserId();
        notificationService.hide(userId, id);
        return ApiResponse.ok(null);
    }
}
