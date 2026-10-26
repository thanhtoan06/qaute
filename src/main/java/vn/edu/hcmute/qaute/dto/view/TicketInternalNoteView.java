package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;

// Đóng gói dữ liệu TicketInternalNote để hiển thị hoặc trả về API.
public record TicketInternalNoteView(Long id, String authorName, String contentHtml,
        boolean pinned, boolean mine, LocalDateTime createdAt) { }
