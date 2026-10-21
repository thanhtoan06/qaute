package vn.edu.hcmute.qaute.dto.request.manager;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerAvatarForm {
    @NotNull(message = "Chọn ảnh đại diện")
    @Positive
    private Long assetId;
}
