package vn.edu.hcmute.qaute.dto.request.manager;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignForm {
    @NotNull(message = "Vui lòng chọn tư vấn viên")
    @Positive(message = "Tư vấn viên không hợp lệ")
    private Long toManagerId;

    @NotBlank(message = "Vui lòng nhập lý do")
    @Size(min = 10, max = 500, message = "Lý do phải từ 10 đến 500 ký tự")
    private String reason;
}
