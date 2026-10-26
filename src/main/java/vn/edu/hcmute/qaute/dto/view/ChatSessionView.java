package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import java.util.List;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;

// Đóng gói dữ liệu ChatSession để hiển thị hoặc trả về API.
public record ChatSessionView(Long sessionId, ChatSessionStatus status, Long categoryId,
        String categoryPath, String question, Long managerId, String managerName,
        Integer queuePosition, boolean workingNow, String message,
        List<FaqSuggestion> suggestions, LocalDateTime startedAt) {
}
