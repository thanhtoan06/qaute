package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.validation.StrongPassword;

@Getter
@Setter
@NoArgsConstructor
public class UserPasswordForm {
    @StrongPassword
    private String newPassword;

    @NotBlank(message = "Vui lòng nhập lại mật khẩu mới")
    @Size(max = 64, message = "Mật khẩu xác nhận tối đa 64 ký tự")
    private String confirmPassword;
}
