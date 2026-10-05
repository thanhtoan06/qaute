package vn.edu.hcmute.qaute.dto.view;

public record StudentSummary(
        UserSummary user,
        String phone,
        String facultyName,
        String classCode,
        String cohort,
        String program) {
}
