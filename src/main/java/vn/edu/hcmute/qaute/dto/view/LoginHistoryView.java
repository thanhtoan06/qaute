package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;

// Đóng gói dữ liệu LoginHistory để hiển thị hoặc trả về API.
public record LoginHistoryView(
        LocalDateTime at,
        boolean success,
        String ip,
        String userAgent) {
}
