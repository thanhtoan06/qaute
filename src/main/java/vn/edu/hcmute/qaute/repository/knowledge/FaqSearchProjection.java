package vn.edu.hcmute.qaute.repository.knowledge;

import java.time.LocalDateTime;

// Khai báo truy vấn lưu trữ dữ liệu FaqSearchProjection.
public interface FaqSearchProjection {

    // Lấy mã câu hỏi.
    Long getId();

    // Lấy tiêu đề câu hỏi.
    String getTitle();

    // Lấy đường dẫn câu hỏi.
    String getSlug();

    // Lấy phần tóm tắt.
    String getSummary();

    // Lấy nội dung văn bản.
    String getBodyText();

    // Lấy tên danh mục.
    String getCategoryName();

    // Lấy số lượt xem.
    Long getViewCount();

    // Lấy số lượt đánh giá hữu ích.
    Integer getHelpfulCount();

    // Lấy thời điểm cập nhật.
    LocalDateTime getUpdatedAt();

    // Lấy điểm liên quan của kết quả.
    Double getScore();
}
