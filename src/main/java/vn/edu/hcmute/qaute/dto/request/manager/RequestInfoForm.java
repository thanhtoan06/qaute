package vn.edu.hcmute.qaute.dto.request.manager;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

// Đóng gói dữ liệu đầu vào cho thao tác RequestInfo.
@Getter
@Setter
public class RequestInfoForm {
    @NotBlank(message = "Vui lòng nhập thông tin cần bổ sung")
    private String messageHtml;
    private List<Long> assetIds;
}
