package vn.edu.hcmute.qaute.service.identity;

// Xử lý nghiệp vụ Password trong hệ thống.
public interface PasswordService {

    // Đổi mật khẩu sau khi xác minh mật khẩu hiện tại.
    void changePassword(Long userId, String currentPassword, String newPassword);
}
