package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

// Đóng gói dữ liệu đầu vào cho thao tác AdminAvatar.
@Getter
@Setter
public class AdminAvatarForm {
    @NotNull(message = "Vui lòng chọn ảnh đại diện")
    @Positive(message = "Ảnh đại diện không hợp lệ")
    private Long assetId;
}
