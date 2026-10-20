package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.common.constant.Modality;

public record ManagerAppointmentView(Long id, Long slotId, Long studentId, StudentSummary student,
        Long categoryId, String categoryPath, Long ticketId, String ticketCode,
        String topic, String note, Modality modality, String location, String meetingLink,
        LocalDateTime startAt, LocalDateTime endAt, AppointmentStatus status,
        String outcomeSummary, String studentVisibleSummary, String cancelReason,
        boolean canComplete, boolean canNoShow, boolean canCancel) {
}
