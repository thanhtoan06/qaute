package vn.edu.hcmute.qaute.dto.view;

// Đóng gói dữ liệu FaqSuggestion để hiển thị hoặc trả về API.
public record FaqSuggestion(
        Long id,
        String title,
        String slug,
        String categoryName,
        String summary,
        String bodyHtml
) {
}

