package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;

public record ChatConversationHistoryItem(Long id, LocalDateTime startedAt, LocalDateTime endedAt,
        String studentName, String managerName, String categoryName, String question, ChatSessionStatus status) {
}
