package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.common.constant.Modality;

public record AppointmentView(Long id, Long slotId, Long managerId, String managerName,
        Long categoryId, String categoryPath, Long ticketId, String ticketCode, String topic,
        String note, Modality modality, String location, String meetingLink,
        LocalDateTime startAt, LocalDateTime endAt, AppointmentStatus status,
        String cancelReason, String studentVisibleSummary, boolean canCancel, boolean canReschedule) {
}
