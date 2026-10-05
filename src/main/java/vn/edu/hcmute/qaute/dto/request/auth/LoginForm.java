package vn.edu.hcmute.qaute.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {

    @NotBlank(message = "Vui lòng nhập email hoặc MSSV")
    @Size(max = 150, message = "Email hoặc MSSV không hợp lệ")
    private String identifier;

    @NotBlank(message = "Vui lòng nhập mật khẩu")
    private String password;

    private boolean remember;
}
