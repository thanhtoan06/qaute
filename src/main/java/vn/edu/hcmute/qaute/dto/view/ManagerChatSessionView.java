package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
public record ManagerChatSessionView(ChatSessionView session, String studentName,
        LocalDateTime lastMessageAt, Long ticketId) {}
