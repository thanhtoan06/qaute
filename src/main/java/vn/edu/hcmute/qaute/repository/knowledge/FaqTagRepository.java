package vn.edu.hcmute.qaute.repository.knowledge;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.entity.knowledge.FaqTag;

// Khai báo truy vấn lưu trữ dữ liệu FaqTag.
public interface FaqTagRepository extends JpaRepository<FaqTag, Long> {

    // Tìm thẻ theo đường dẫn.
    Optional<FaqTag> findBySlug(String slug);

    // Kiểm tra tên chuẩn hóa đã tồn tại hay chưa.
    boolean existsByNameNorm(String nameNorm);

    // Kiểm tra tên chuẩn hóa đã thuộc về thẻ khác hay chưa.
    boolean existsByNameNormAndIdNot(String nameNorm, Long id);

    // Lấy tất cả thẻ theo thứ tự tên tăng dần.
    List<FaqTag> findAllByOrderByNameAsc();

    // Đếm số lần sử dụng của từng thẻ.
    @Query(value = "SELECT tag_id, COUNT(*) FROM faq_tag_map GROUP BY tag_id", nativeQuery = true)
    List<Object[]> countAllUsages();

    // Lấy các thẻ phổ biến nhất trong câu hỏi đã xuất bản.
    @Query("SELECT t.name, t.slug, COUNT(f.id) FROM Faq f JOIN f.tags t "
            + "WHERE f.status = vn.edu.hcmute.qaute.common.constant.FaqStatus.PUBLISHED "
            + "AND (f.publishAt IS NULL OR f.publishAt <= :now) "
            + "GROUP BY t.id, t.name, t.slug "
            + "ORDER BY COUNT(f.id) DESC")
    List<Object[]> findPopularTags(@Param("now") LocalDateTime now, Pageable pageable);
}
