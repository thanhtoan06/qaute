package vn.edu.hcmute.qaute.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.validation.StrongPassword;
import vn.edu.hcmute.qaute.common.validation.ValidMssv;

@Getter
@Setter
public class RegisterForm {

    @NotBlank(message = "Vui lòng nhập họ và tên")
    @Size(min = 2, max = 100, message = "Họ và tên phải từ 2 đến 100 ký tự")
    @Pattern(regexp = "^[\\p{L}][\\p{L}\\s.\\-']*$",
            message = "Họ và tên chỉ được chứa chữ cái, khoảng trắng, dấu chấm, gạch ngang hoặc nháy đơn")
    private String fullName;

    @NotBlank(message = "Vui lòng nhập email")
    @Email(message = "Email không hợp lệ")
    @Size(max = 150, message = "Email không được vượt quá 150 ký tự")
    private String email;

    @ValidMssv
    private String mssv;

    @StrongPassword
    private String password;

    private String confirmPassword;

    private boolean acceptTerms;
}
