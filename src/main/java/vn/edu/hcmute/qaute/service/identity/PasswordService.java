package vn.edu.hcmute.qaute.service.identity;

public interface PasswordService {

    void changePassword(Long userId, String currentPassword, String newPassword);
}

