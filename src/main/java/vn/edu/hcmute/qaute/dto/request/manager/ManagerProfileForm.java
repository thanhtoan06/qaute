package vn.edu.hcmute.qaute.dto.request.manager;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ManagerProfileForm {
    @NotBlank(message = "Vui lòng nhập họ và tên")
    @Size(min = 2, max = 100, message = "Họ và tên phải từ 2 đến 100 ký tự")
    private String fullName;

    @Pattern(regexp = "^$|^(0|\\+84)\\d{9}$",
            message = "Số điện thoại phải có dạng 0xxxxxxxxx hoặc +84xxxxxxxxx")
    private String phone;

    @Size(max = 100, message = "Chức danh tối đa 100 ký tự")
    private String jobTitle;

    @Size(max = 500, message = "Giới thiệu tối đa 500 ký tự")
    private String bio;
}
