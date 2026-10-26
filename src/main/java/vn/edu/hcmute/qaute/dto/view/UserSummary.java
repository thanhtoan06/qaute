package vn.edu.hcmute.qaute.dto.view;

import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;

// Đóng gói dữ liệu UserSummary để hiển thị hoặc trả về API.
public record UserSummary(
        Long id,
        String fullName,
        String email,
        String mssv,
        String avatarUrl,
        RoleCode role,
        UserStatus status) {
}
