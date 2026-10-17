package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import java.util.List;

public record FaqDetailView(
        Long id,
        String title,
        String slug,
        String summary,
        String bodyHtml,
        Long categoryId,
        String categoryPath,
        long viewCount,
        int helpfulCount,
        int notHelpfulCount,
        LocalDateTime updatedAt,
        List<TagView> tags
) {
}

