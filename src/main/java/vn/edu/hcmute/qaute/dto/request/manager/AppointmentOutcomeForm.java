package vn.edu.hcmute.qaute.dto.request.manager;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// Đóng gói dữ liệu đầu vào cho thao tác AppointmentOutcome.
@Getter
@Setter
public class AppointmentOutcomeForm {
    @NotBlank
    @Size(min = 10, max = 2000)
    private String outcomeSummary;

    @Size(max = 1000)
    private String studentVisibleSummary;
}
