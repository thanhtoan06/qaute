package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import java.util.List;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;

// Đóng gói dữ liệu AdminFaqReview để hiển thị hoặc trả về API.
public record AdminFaqReviewView(
        Long id, String title, String slug, String summary, String bodyHtml,
        String categoryPath, String authorName, FaqStatus status, boolean featured,
        LocalDateTime publishAt, LocalDateTime publishedAt, List<String> tags,
        List<RevisionNote> revisions) {
    public record RevisionNote(int versionNo, String changeNote, Long changedBy,
                               LocalDateTime createdAt) {
    }
}
