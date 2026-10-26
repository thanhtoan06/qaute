package vn.edu.hcmute.qaute.service.identity;

import vn.edu.hcmute.qaute.dto.request.student.ProfileForm;
import vn.edu.hcmute.qaute.dto.view.ProfileView;

// Xử lý nghiệp vụ StudentProfile trong hệ thống.
public interface StudentProfileService {

    // Lấy hồ sơ sinh viên của người dùng.
    ProfileView getProfile(Long userId);

    // Cập nhật thông tin hồ sơ sinh viên.
    void updateProfile(Long userId, ProfileForm form);

    // Cập nhật ảnh đại diện của người dùng.
    void updateAvatar(Long userId, Long assetId);
}
