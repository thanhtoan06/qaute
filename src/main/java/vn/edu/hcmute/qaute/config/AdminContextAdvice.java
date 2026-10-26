package vn.edu.hcmute.qaute.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.admin.AdminProfileService;

// Đưa ảnh đại diện quản trị viên vào ngữ cảnh các trang quản trị.
@ControllerAdvice(basePackages = "vn.edu.hcmute.qaute.controller.admin")
public class AdminContextAdvice {
    private final AdminProfileService profiles;

    public AdminContextAdvice(AdminProfileService profiles) {
        this.profiles = profiles;
    }

    @ModelAttribute("workspaceAvatarUrl")
    public String workspaceAvatarUrl() {
        return SecurityUtils.currentUser()
                .map(user -> profiles.get(user.getId()).avatarUrl())
                .orElse(null);
    }
}
