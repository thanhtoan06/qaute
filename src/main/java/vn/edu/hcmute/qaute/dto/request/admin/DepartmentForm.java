package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Form thêm/sửa phòng ban (ADM-05). id null nghĩa là tạo mới.
 */
@Getter
@Setter
@NoArgsConstructor
public class DepartmentForm {

    private Long id;

    @NotBlank(message = "Mã phòng ban không được để trống")
    @Pattern(regexp = "^[A-Z0-9_]{2,30}$",
            message = "Mã phòng ban 2–30 ký tự, chỉ gồm chữ hoa, số và gạch dưới")
    private String code;

    @NotBlank(message = "Tên phòng ban không được để trống")
    @Size(min = 2, max = 150, message = "Tên phòng ban 2–150 ký tự")
    private String name;

    @Size(max = 500, message = "Mô tả tối đa 500 ký tự")
    private String description;

    @Pattern(regexp = "^$|^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$",
            message = "Email liên hệ không đúng định dạng")
    @Size(max = 150, message = "Email liên hệ tối đa 150 ký tự")
    private String email;

    @Size(max = 30, message = "Số điện thoại tối đa 30 ký tự")
    private String phone;

    @Size(max = 200, message = "Địa điểm tối đa 200 ký tự")
    private String location;

    @Min(value = 0, message = "Thứ tự hiển thị phải từ 0 trở lên")
    private int sortOrder;

    private boolean active = true;
}
