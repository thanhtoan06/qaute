package vn.edu.hcmute.qaute.dto.view;

// Đóng gói dữ liệu ManagerStudentListItem để hiển thị hoặc trả về API.
public record ManagerStudentListItem(Long id, String fullName, String mssv, String email,
                                     String facultyName, String classCode) {
}
