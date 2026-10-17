package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;

/** A user row displayed in the administrator's user directory. */
public record AdminUserListItem(
        Long id,
        String fullName,
        String email,
        String mssv,
        RoleCode role,
        UserStatus status,
        LocalDateTime lastLoginAt,
        LocalDateTime createdAt) {
}
