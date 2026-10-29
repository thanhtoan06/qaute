package vn.edu.hcmute.qaute.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// Đóng gói dữ liệu đầu vào cho thao tác Login.
@Getter
@Setter
public class LoginForm {

    @NotBlank(message = "Vui lòng nhập email, MSSV hoặc họ tên")
    @Size(max = 150, message = "Thông tin đăng nhập không hợp lệ")
    private String identifier;

    @NotBlank(message = "Vui lòng nhập mật khẩu")
    private String password;

    private boolean remember;
}
