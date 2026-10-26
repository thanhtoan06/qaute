package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;

// Đóng gói dữ liệu FaqRevision để hiển thị hoặc trả về API.
public record FaqRevisionView(int versionNo, String title, String summary, String bodyHtml,
                              Long changedBy, String changedByName, String changeNote,
                              LocalDateTime createdAt) {
}
