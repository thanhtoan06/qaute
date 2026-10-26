package vn.edu.hcmute.qaute.repository.knowledge;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.knowledge.FaqBookmark;

// Khai báo truy vấn lưu trữ dữ liệu FaqBookmark.
public interface FaqBookmarkRepository extends JpaRepository<FaqBookmark, Long> {
    // Tìm dấu trang của người dùng cho một câu hỏi.
    Optional<FaqBookmark> findByUserIdAndFaqId(Long userId, Long faqId);

    // Lấy danh sách dấu trang theo thứ tự mới nhất.
    Page<FaqBookmark> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    // Tìm các dấu trang thuộc danh sách câu hỏi đã cho.
    List<FaqBookmark> findByUserIdAndFaqIdIn(Long userId, Collection<Long> faqIds);
}
