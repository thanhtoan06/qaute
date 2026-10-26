package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;

// Đóng gói dữ liệu TicketDraft để hiển thị hoặc trả về API.
public record TicketDraftView(String contentHtml, LocalDateTime updatedAt) { }
