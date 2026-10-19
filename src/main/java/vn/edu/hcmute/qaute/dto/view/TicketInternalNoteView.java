package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;

public record TicketInternalNoteView(Long id, String authorName, String contentHtml,
        boolean pinned, boolean mine, LocalDateTime createdAt) { }
