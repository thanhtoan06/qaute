package vn.edu.hcmute.qaute.dto.request.admin;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.common.constant.Modality;

@Getter
@Setter
public class AdminAppointmentFilter {
    private String q;
    private AppointmentStatus status;
    private Modality modality;
    private Long managerId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;
}
