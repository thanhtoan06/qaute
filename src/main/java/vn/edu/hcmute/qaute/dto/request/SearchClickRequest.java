package vn.edu.hcmute.qaute.dto.request;

// Đóng gói dữ liệu đầu vào cho thao tác SearchClick.
public record SearchClickRequest(
        String query,
        Long faqId,
        String source
) {
}

