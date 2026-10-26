package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.Modality;

// Đóng gói dữ liệu Slot để hiển thị hoặc trả về API.
public record SlotView(Long slotId, Long managerId, String managerName, LocalDateTime startAt,
        LocalDateTime endAt, Modality modality, String location) {
}
