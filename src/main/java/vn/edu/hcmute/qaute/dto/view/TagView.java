package vn.edu.hcmute.qaute.dto.view;

// Đóng gói dữ liệu Tag để hiển thị hoặc trả về API.
public record TagView(
        String name,
        String slug,
        long faqCount
) {
}

