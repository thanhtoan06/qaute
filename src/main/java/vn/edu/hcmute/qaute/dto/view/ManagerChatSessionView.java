package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
// Đóng gói dữ liệu ManagerChatSession để hiển thị hoặc trả về API.
public record ManagerChatSessionView(ChatSessionView session, String studentName,
        LocalDateTime lastMessageAt, Long ticketId) {}
