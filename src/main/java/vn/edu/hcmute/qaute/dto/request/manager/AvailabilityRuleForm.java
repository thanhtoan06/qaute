package vn.edu.hcmute.qaute.dto.request.manager;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;
import vn.edu.hcmute.qaute.common.constant.Modality;

@Getter @Setter
public class AvailabilityRuleForm {
    @NotNull @Min(1) @Max(7) private Integer dayOfWeek;
    @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) private LocalTime startTime;
    @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) private LocalTime endTime;
    @NotNull private Integer slotMinutes;
    @NotNull private Modality modality;
    @Size(max = 200) private String location;
    @URL(protocol = "https") @Pattern(regexp = "^$|^https://.*$", message = "Liên kết phải bắt đầu bằng https://")
    @Size(max = 300) private String meetingLink;
    @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) private LocalDate validFrom;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) private LocalDate validTo;
}
