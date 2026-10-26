package vn.edu.hcmute.qaute.dto.view;

import vn.edu.hcmute.qaute.common.constant.Modality;

// Đóng gói dữ liệu BookAppointmentCommand để hiển thị hoặc trả về API.
public record BookAppointmentCommand(Long slotId, Long categoryId, String topic, String note,
        Modality modality, Long ticketId) {
}
