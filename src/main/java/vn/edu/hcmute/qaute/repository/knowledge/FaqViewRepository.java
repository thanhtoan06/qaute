package vn.edu.hcmute.qaute.repository.knowledge;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.knowledge.FaqView;

// Khai báo truy vấn lưu trữ dữ liệu FaqView.
public interface FaqViewRepository extends JpaRepository<FaqView, Long> {
    // Tìm lịch sử xem của người dùng cho một câu hỏi.
    Optional<FaqView> findByUserIdAndFaqId(Long userId, Long faqId);

    // Lấy lịch sử xem theo thời điểm xem gần nhất.
    Page<FaqView> findByUserIdOrderByLastViewedAtDesc(Long userId, Pageable pageable);

    // Tìm lịch sử xem của người dùng cho các câu hỏi đã cho.
    List<FaqView> findByUserIdAndFaqIdIn(Long userId, Collection<Long> faqIds);
}
