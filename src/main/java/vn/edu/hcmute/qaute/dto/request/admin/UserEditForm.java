package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.validation.ValidMssv;

// Đóng gói dữ liệu đầu vào cho thao tác UserEdit.
@Getter
@Setter
public class UserEditForm {
    @NotBlank(message = "Vui lòng nhập họ tên")
    @Size(min = 2, max = 100, message = "Họ tên phải từ 2 đến 100 ký tự")
    private String fullName;
    @Size(max = 20)
    private String phone;
    @ValidMssv
    private String mssv;
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
