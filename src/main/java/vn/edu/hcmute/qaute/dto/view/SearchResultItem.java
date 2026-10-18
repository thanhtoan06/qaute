package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;

public record SearchResultItem(
        Long id,
        String title,
        String slug,
        String summary,
        String categoryName,
        long viewCount,
        int helpfulCount,
        LocalDateTime updatedAt,
        String titleHtml,
        String snippetHtml,
        double score) {
}

