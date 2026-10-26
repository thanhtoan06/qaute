package vn.edu.hcmute.qaute.repository.knowledge;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.entity.knowledge.Faq;

// Khai báo truy vấn lưu trữ dữ liệu Faq.
public interface FaqRepository extends JpaRepository<Faq, Long>, JpaSpecificationExecutor<Faq> {

    // Tìm câu hỏi thường gặp theo đường dẫn.
    Optional<Faq> findBySlug(String slug);

    // Kiểm tra đường dẫn đã được sử dụng hay chưa.
    boolean existsBySlug(String slug);

    // Kiểm tra đường dẫn đã được câu hỏi khác sử dụng hay chưa.
    boolean existsBySlugAndIdNot(String slug, Long id);

    // Đếm số câu hỏi đã xuất bản theo danh mục.
    @Query("SELECT f.categoryId, COUNT(f.id) FROM Faq f "
            + "WHERE f.status = vn.edu.hcmute.qaute.common.constant.FaqStatus.PUBLISHED "
            + "AND (f.publishAt IS NULL OR f.publishAt <= :now) "
            + "GROUP BY f.categoryId")
    List<Object[]> countPublishedByCategory(@Param("now") LocalDateTime now);

    // Tăng lượt xem của câu hỏi.
    @Modifying
    @Query("UPDATE Faq f SET f.viewCount = f.viewCount + 1 WHERE f.id = :id")
    void incrementViewCount(@Param("id") Long id);

    // Gợi ý câu hỏi phù hợp với tiền tố hoặc nội dung tìm kiếm.
    @Query(value = """
            SELECT f.* FROM faqs f
            WHERE f.status = 'PUBLISHED'
              AND (f.publish_at IS NULL OR f.publish_at <= :now)
              AND (
                  f.title_norm LIKE :prefix
                  OR f.title_norm LIKE :contains
                  OR f.body_norm LIKE :prefix
                  OR f.body_norm LIKE :contains
              )
            ORDER BY
              CASE
                WHEN f.title_norm LIKE :prefix THEN 1
                WHEN f.title_norm LIKE :contains THEN 2
                WHEN f.body_norm LIKE :prefix THEN 3
                WHEN f.body_norm LIKE :contains THEN 4
                ELSE 5
              END,
              f.view_count DESC,
              f.updated_at DESC
            """, nativeQuery = true)
    List<Faq> suggestFaqs(@Param("prefix") String prefix,
                          @Param("contains") String contains,
                          @Param("now") LocalDateTime now,
                          org.springframework.data.domain.Pageable pageable);
}
