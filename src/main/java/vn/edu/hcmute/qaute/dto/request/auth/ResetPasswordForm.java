package vn.edu.hcmute.qaute.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.validation.StrongPassword;

// Đóng gói dữ liệu đầu vào cho thao tác ResetPassword.
@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordForm {

    @NotBlank(message = "Phiên đặt lại không hợp lệ")
    private String token;

    @StrongPassword
    private String newPassword;

    @NotBlank(message = "Vui lòng nhập lại mật khẩu")
    private String confirmPassword;
}
