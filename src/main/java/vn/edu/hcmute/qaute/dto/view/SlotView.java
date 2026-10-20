package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.Modality;

public record SlotView(Long slotId, Long managerId, String managerName, LocalDateTime startAt,
        LocalDateTime endAt, Modality modality, String location) {
}
