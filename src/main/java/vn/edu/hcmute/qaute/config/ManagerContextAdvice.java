package vn.edu.hcmute.qaute.config;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.manager.ManagerProfileService;

// Đưa trạng thái và hoạt động gần nhất của tư vấn viên vào ngữ cảnh trang quản lý.
@ControllerAdvice(basePackages = "vn.edu.hcmute.qaute.controller.manager")
public class ManagerContextAdvice {
    private static final Duration TOUCH_INTERVAL = Duration.ofMinutes(1);
    private final ManagerProfileService profiles;
    private final ConcurrentMap<Long, Instant> lastTouched = new ConcurrentHashMap<>();

    // Khởi tạo ngữ cảnh quản lý với dịch vụ hồ sơ.
    public ManagerContextAdvice(ManagerProfileService profiles) {
        this.profiles = profiles;
    }

    // Lấy URL ảnh đại diện hiện tại cho các vị trí nhận diện trong workspace.
    @ModelAttribute("workspaceAvatarUrl")
    public String workspaceAvatarUrl() {
        return SecurityUtils.currentUser()
                .map(user -> profiles.get(user.getId()).avatarUrl())
                .orElse(null);
    }

    // Lấy trạng thái quản lý và cập nhật thời điểm hoạt động.
    @ModelAttribute("managerStatus")
    public AvailabilityStatus managerStatus() {
        Long userId = SecurityUtils.currentUser().map(user -> user.getId()).orElse(null);
        if (userId == null) return AvailabilityStatus.OFFLINE;
        Instant now = Instant.now();
        lastTouched.compute(userId, (id, previous) -> {
            if (previous == null || !now.isBefore(previous.plus(TOUCH_INTERVAL))) {
                profiles.touchLastSeen(id);
                return now;
            }
            return previous;
        });
        return profiles.currentStatus(userId);
    }
}
