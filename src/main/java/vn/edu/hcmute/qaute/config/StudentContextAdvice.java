package vn.edu.hcmute.qaute.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.identity.StudentProfileService;

@ControllerAdvice(basePackages = "vn.edu.hcmute.qaute.controller.student")
public class StudentContextAdvice {

    private final StudentProfileService profiles;

    public StudentContextAdvice(StudentProfileService profiles) {
        this.profiles = profiles;
    }

    @ModelAttribute("studentAvatarUrl")
    public String studentAvatarUrl() {
        return SecurityUtils.currentUser()
                .map(user -> profiles.getProfile(user.getId()).avatarUrl())
                .orElse(null);
    }
}
