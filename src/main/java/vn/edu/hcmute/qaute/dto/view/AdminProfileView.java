package vn.edu.hcmute.qaute.dto.view;

// Đóng gói dữ liệu AdminProfile để hiển thị hoặc trả về API.
public record AdminProfileView(String fullName, String email, String phone, String avatarUrl) {
}
