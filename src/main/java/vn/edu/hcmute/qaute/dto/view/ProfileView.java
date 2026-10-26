package vn.edu.hcmute.qaute.dto.view;

// Đóng gói dữ liệu Profile để hiển thị hoặc trả về API.
public record ProfileView(
        String fullName,
        String email,
        String mssv,
        String phone,
        String avatarUrl,
        Long facultyId,
        String facultyName,
        String classCode,
        String cohort,
        String program) {
}

