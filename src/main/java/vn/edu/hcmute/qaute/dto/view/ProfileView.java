package vn.edu.hcmute.qaute.dto.view;

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

