package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.common.constant.Modality;

public record AdminAppointmentListItem(
        Long id,
        String studentName,
        String studentEmail,
        String managerName,
        String managerEmail,
        String topic,
        String categoryPath,
        Modality modality,
        AppointmentStatus status,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String location) {
}
