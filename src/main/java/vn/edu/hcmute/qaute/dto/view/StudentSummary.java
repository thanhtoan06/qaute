package vn.edu.hcmute.qaute.dto.view;

// Đóng gói dữ liệu StudentSummary để hiển thị hoặc trả về API.
public record StudentSummary(
        UserSummary user,
        String phone,
        String facultyName,
        String classCode,
        String cohort,
        String program) {
}
