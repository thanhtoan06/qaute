package vn.edu.hcmute.qaute.service.identity;

// Xử lý nghiệp vụ Invitation trong hệ thống.
public interface InvitationService {

    // Gửi lời mời kích hoạt đến người dùng.
    void sendInvitation(Long userId);
}
