package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryForm {

    private Long id;

    @NotNull(message = "Vui lòng chọn phòng ban")
    private Long departmentId;

    /** Null = chuyên mục cấp 1; có giá trị = chủ đề cấp 2 thuộc chuyên mục cha. */
    private Long parentId;

    @NotBlank(message = "Vui lòng nhập mã chuyên mục")
    @Pattern(regexp = "^[A-Z0-9_]{2,40}$",
            message = "Mã gồm 2–40 ký tự: chữ in hoa, số hoặc dấu gạch dưới")
    private String code;

    @NotBlank(message = "Vui lòng nhập tên chuyên mục")
    @Size(min = 2, max = 150, message = "Tên chuyên mục từ 2 đến 150 ký tự")
    private String name;

    @Size(max = 500, message = "Mô tả tối đa 500 ký tự")
    private String description;

    /** Tùy chọn. Cột icon dài 40 ký tự nên "bi-" + tối đa 37 ký tự; chuỗi rỗng được chấp nhận. */
    @Pattern(regexp = "^(bi-[a-z0-9-]{1,37})?$",
            message = "Icon phải có dạng bi-xxx (chữ thường, số, dấu gạch ngang), tối đa 40 ký tự")
    private String icon;

    @Min(value = 0, message = "Thứ tự không được âm")
    private int sortOrder;

    private boolean active = true;
}
