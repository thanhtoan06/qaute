package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import java.util.List;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;

public record ManagerChatSessionView(Long sessionId, ChatSessionStatus status, Long categoryId,
        String categoryPath, String question, StudentSummary student, LocalDateTime lastMessageAt,
        Long ticketId, List<RecentTicket> recentTickets) {
    public record RecentTicket(Long id, String code, String title) {}
}
