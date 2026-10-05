package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;

public record LoginHistoryView(
        LocalDateTime at,
        boolean success,
        String ip,
        String userAgent) {
}
