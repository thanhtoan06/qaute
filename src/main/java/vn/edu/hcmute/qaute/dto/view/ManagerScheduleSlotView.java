package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.Modality;
import vn.edu.hcmute.qaute.common.constant.SlotStatus;

// Đóng gói dữ liệu ManagerScheduleSlot để hiển thị hoặc trả về API.
public record ManagerScheduleSlotView(Long id, LocalDateTime startAt, LocalDateTime endAt,
        Modality modality, SlotStatus status, Long appointmentId, String studentName, boolean past) {
}
