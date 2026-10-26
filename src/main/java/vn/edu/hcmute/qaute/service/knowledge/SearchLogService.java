package vn.edu.hcmute.qaute.service.knowledge;

// Xử lý nghiệp vụ SearchLog trong hệ thống.
public interface SearchLogService {

    // Ghi nhận lượt nhấp vào kết quả tìm kiếm.
    void recordClick(String query, Long faqId, String source, Long userId, String ipHash);
}
