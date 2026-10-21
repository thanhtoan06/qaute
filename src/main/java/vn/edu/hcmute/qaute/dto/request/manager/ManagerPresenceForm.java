package vn.edu.hcmute.qaute.dto.request.manager;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;

@Getter
@Setter
public class ManagerPresenceForm {
    @NotNull(message = "Chọn trạng thái trực hợp lệ")
    private AvailabilityStatus status;
    @Size(max = 2048)
    private String redirect;
}
