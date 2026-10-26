package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;

// Đóng gói dữ liệu ChatHistoryItem để hiển thị hoặc trả về API.
public record ChatHistoryItem(Long id, LocalDateTime startedAt, LocalDateTime endedAt,
        String managerName, String categoryName, ChatSessionStatus status) {
}
