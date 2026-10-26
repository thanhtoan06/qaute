package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;

// Đóng gói dữ liệu AdminFaqListItem để hiển thị hoặc trả về API.
public record AdminFaqListItem(
        Long id, String title, String slug, String categoryPath, FaqStatus status,
        boolean featured, String authorName, long viewCount, int helpfulCount,
        int notHelpfulCount, LocalDateTime updatedAt, LocalDateTime publishAt,
        Long sourceTicketId) {
}
