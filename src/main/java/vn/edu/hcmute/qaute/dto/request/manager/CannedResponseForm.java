package vn.edu.hcmute.qaute.dto.request.manager;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.CannedResponseScope;

// Đóng gói dữ liệu đầu vào cho thao tác CannedResponse.
@Getter
@Setter
public class CannedResponseForm {
    private Long id;

    @NotBlank(message = "Vui lòng nhập tiêu đề")
    @Size(min = 2, max = 120, message = "Tiêu đề phải từ 2 đến 120 ký tự")
    private String title;

    @NotBlank(message = "Vui lòng nhập nội dung mẫu")
    @Size(max = 10000, message = "Nội dung mẫu tối đa 10000 ký tự")
    private String bodyHtml;

    @NotNull(message = "Vui lòng chọn phạm vi mẫu")
    private CannedResponseScope scope = CannedResponseScope.PERSONAL;

    private Long categoryId;
    private boolean active = true;
}
