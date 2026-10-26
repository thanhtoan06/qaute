package vn.edu.hcmute.qaute.repository.knowledge;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.entity.knowledge.FaqFeedback;

// Khai báo truy vấn lưu trữ dữ liệu FaqFeedback.
public interface FaqFeedbackRepository extends JpaRepository<FaqFeedback, Long> {
    // Tìm phản hồi của người dùng cho một câu hỏi.
    Optional<FaqFeedback> findByFaqIdAndUserId(Long faqId, Long userId);

    // Tìm phản hồi của người dùng cho các câu hỏi đã cho.
    List<FaqFeedback> findByUserIdAndFaqIdIn(Long userId, Collection<Long> faqIds);

    // Đếm số phản hồi theo câu hỏi và trạng thái hữu ích.
    @Query("SELECT COUNT(f.id) FROM FaqFeedback f WHERE f.faqId = :faqId AND f.helpful = :helpful")
    long countByFaqIdAndHelpful(@Param("faqId") Long faqId, @Param("helpful") boolean helpful);
}
