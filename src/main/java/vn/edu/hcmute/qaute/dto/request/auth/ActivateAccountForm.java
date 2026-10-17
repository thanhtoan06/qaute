package vn.edu.hcmute.qaute.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.validation.StrongPassword;

@Getter
@Setter
@NoArgsConstructor
public class ActivateAccountForm {

    @NotBlank(message = "Token không được để trống")
    private String token;

    @StrongPassword
    private String newPassword;

    @NotBlank(message = "Vui lòng nhập lại mật khẩu")
    private String confirmPassword;
}

