package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;

public record AnnouncementView(Long id, String title, String summary, String bodyHtml,
        String coverUrl, boolean pinned, LocalDateTime publishAt) {}
