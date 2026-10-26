package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.RoleCode;

// Đóng gói dữ liệu đầu vào cho thao tác UserRoleChange.
@Getter
@Setter
public class UserRoleChangeForm {
    @NotNull
    private RoleCode role;

    @NotBlank
    @Size(min = 10, max = 500)
    private String reason;
}
