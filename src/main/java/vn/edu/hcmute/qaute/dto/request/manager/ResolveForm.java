package vn.edu.hcmute.qaute.dto.request.manager;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// Đóng gói dữ liệu đầu vào cho thao tác Resolve.
@Getter
@Setter
public class ResolveForm {
    @NotBlank(message = "Vui lòng nhập tóm tắt giải pháp")
    @Size(min = 10, max = 2000, message = "Tóm tắt giải pháp phải từ 10 đến 2000 ký tự")
    private String resolutionNote;
}
