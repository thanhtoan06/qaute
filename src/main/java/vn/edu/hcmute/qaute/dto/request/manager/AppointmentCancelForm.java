package vn.edu.hcmute.qaute.dto.request.manager;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// Đóng gói dữ liệu đầu vào cho thao tác AppointmentCancel.
@Getter
@Setter
public class AppointmentCancelForm {
    @NotBlank
    @Size(min = 10, max = 500)
    private String reason;
}
