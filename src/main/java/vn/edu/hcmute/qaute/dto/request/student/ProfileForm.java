package vn.edu.hcmute.qaute.dto.request.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileForm {

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(min = 2, max = 100, message = "Họ và tên phải từ 2 đến 100 ký tự")
    private String fullName;

    @Pattern(regexp = "^$|^(0|\\+84)\\d{9}$", message = "Số điện thoại không đúng định dạng (0xxxxxxxxx hoặc +84xxxxxxxxx)")
    private String phone;

    private Long facultyId;

    @Size(max = 30, message = "Mã lớp tối đa 30 ký tự")
    private String classCode;

    @Size(max = 10, message = "Khóa học tối đa 10 ký tự")
    private String cohort;

    @Size(max = 100, message = "Chương trình đào tạo tối đa 100 ký tự")
    private String program;
}

