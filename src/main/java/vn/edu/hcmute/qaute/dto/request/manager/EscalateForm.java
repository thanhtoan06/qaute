package vn.edu.hcmute.qaute.dto.request.manager;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EscalateForm {
    @NotNull(message = "Vui lòng chọn chuyên mục đích")
    @Positive(message = "Chuyên mục đích không hợp lệ")
    private Long toCategoryId;

    @Positive(message = "Tư vấn viên đích không hợp lệ")
    private Long toManagerId;

    @NotBlank(message = "Vui lòng nhập lý do chuyển cấp")
    @Size(min = 10, max = 500, message = "Lý do chuyển cấp phải từ 10 đến 500 ký tự")
    private String reason;
}
