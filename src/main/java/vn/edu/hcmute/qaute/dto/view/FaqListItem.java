package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import java.util.List;

public record FaqListItem(
        Long id,
        String title,
        String slug,
        String summary,
        Long categoryId,
        String categoryName,
        long viewCount,
        int helpfulCount,
        int notHelpfulCount,
        LocalDateTime updatedAt,
        List<String> tags
) {
}

