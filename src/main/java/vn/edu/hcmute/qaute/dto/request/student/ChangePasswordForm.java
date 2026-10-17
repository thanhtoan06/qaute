package vn.edu.hcmute.qaute.dto.request.student;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.validation.StrongPassword;

@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordForm {

    @NotBlank(message = "Vui lòng nhập mật khẩu hiện tại")
    private String currentPassword;

    @StrongPassword
    private String newPassword;

    @NotBlank(message = "Vui lòng nhập lại mật khẩu mới")
    private String confirmPassword;
}

