package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import java.util.List;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;

/** Complete account information shown to an administrator. Profile fields are null when inapplicable. */
public record AdminUserDetailView(
        Long id,
        String fullName,
        String email,
        String mssv,
        String phone,
        RoleCode role,
        UserStatus status,
        LocalDateTime lastLoginAt,
        LocalDateTime createdAt,
        String facultyName,
        String classCode,
        String cohort,
        String program,
        String departmentName,
        String jobTitle,
        List<String> categoryNames,
        int relatedTicketCount) {

    // Chuẩn hóa danh sách chuyên mục trước khi tạo DTO.
    public AdminUserDetailView {
        categoryNames = categoryNames == null ? List.of() : List.copyOf(categoryNames);
    }
}
