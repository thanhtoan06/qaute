package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// Đóng gói dữ liệu đầu vào cho thao tác AdminProfile.
@Getter
@Setter
public class AdminProfileForm {
    @NotBlank(message = "Vui lòng nhập họ và tên")
    @Size(min = 2, max = 100, message = "Họ và tên phải từ 2 đến 100 ký tự")
    private String fullName;

    @NotBlank(message = "Vui lòng nhập email")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 150, message = "Email tối đa 150 ký tự")
    private String email;

    @Pattern(regexp = "^$|^(0|\\+84)\\d{9}$",
            message = "Số điện thoại phải có dạng 0xxxxxxxxx hoặc +84xxxxxxxxx")
    private String phone;
}
