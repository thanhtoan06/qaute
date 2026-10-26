package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

// Đóng gói dữ liệu đầu vào cho thao tác TagMerge.
@Getter
@Setter
public class TagMergeForm {
    @NotNull(message = "Chọn thẻ đích")
    @Positive
    private Long targetId;
}
