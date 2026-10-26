package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;

// Đóng gói dữ liệu Announcement để hiển thị hoặc trả về API.
public record AnnouncementView(Long id, String title, String summary, String bodyHtml,
        String coverUrl, boolean pinned, LocalDateTime publishAt) {}
