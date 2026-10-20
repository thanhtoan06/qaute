package vn.edu.hcmute.qaute.dto.view;

import vn.edu.hcmute.qaute.common.constant.Modality;

public record BookAppointmentCommand(Long slotId, Long categoryId, String topic, String note,
        Modality modality, Long ticketId) {
}
