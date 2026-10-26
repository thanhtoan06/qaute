package vn.edu.hcmute.qaute.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.identity.StudentProfileService;

// Đưa URL ảnh đại diện của sinh viên hiện tại vào ngữ cảnh trang sinh viên.
@ControllerAdvice(basePackages = "vn.edu.hcmute.qaute.controller.student")
public class StudentContextAdvice {

    private final StudentProfileService profiles;

    // Khởi tạo ngữ cảnh sinh viên với dịch vụ hồ sơ.
    public StudentContextAdvice(StudentProfileService profiles) {
        this.profiles = profiles;
    }

    // Lấy URL ảnh đại diện của sinh viên hiện tại.
    @ModelAttribute("studentAvatarUrl")
    public String studentAvatarUrl() {
        return SecurityUtils.currentUser()
                .map(user -> profiles.getProfile(user.getId()).avatarUrl())
                .orElse(null);
    }
}
