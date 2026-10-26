package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.validation.ValidMssv;

// Đóng gói dữ liệu đầu vào cho thao tác UserCreate.
@Getter
@Setter
public class UserCreateForm {
    @NotNull(message = "Vui lòng chọn vai trò")
    private RoleCode role;

    @NotBlank(message = "Vui lòng nhập họ tên")
    @Size(min = 2, max = 100, message = "Họ tên phải từ 2 đến 100 ký tự")
    private String fullName;

    @NotBlank(message = "Vui lòng nhập email")
    @Email(message = "Email không hợp lệ")
    @Size(max = 150, message = "Email tối đa 150 ký tự")
    private String email;

    @ValidMssv
    private String mssv;

    @Size(max = 20)
    private String phone;
    private Long facultyId;
    @Size(max = 30)
    private String classCode;
    @Size(max = 10)
    private String cohort;
    @Size(max = 100)
    private String program;
    private Long departmentId;
    @Size(max = 100)
    private String jobTitle;
    @Size(max = 30)
    private String employeeCode;
}
