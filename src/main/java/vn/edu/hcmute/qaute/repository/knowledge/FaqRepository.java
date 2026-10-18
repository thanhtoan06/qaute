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

public interface FaqRepository extends JpaRepository<Faq, Long>, JpaSpecificationExecutor<Faq> {

    Optional<Faq> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    @Query("SELECT f.categoryId, COUNT(f.id) FROM Faq f "
            + "WHERE f.status = vn.edu.hcmute.qaute.common.constant.FaqStatus.PUBLISHED "
            + "AND (f.publishAt IS NULL OR f.publishAt <= :now) "
            + "GROUP BY f.categoryId")
    List<Object[]> countPublishedByCategory(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE Faq f SET f.viewCount = f.viewCount + 1 WHERE f.id = :id")
    void incrementViewCount(@Param("id") Long id);

    @Query(value = """
            SELECT f.* FROM faqs f
            WHERE f.status = 'PUBLISHED'
              AND (f.publish_at IS NULL OR f.publish_at <= :now)
              AND (
                  f.title_norm LIKE :prefix
                  OR f.title_norm LIKE :contains
              )
            ORDER BY
              CASE
                WHEN f.title_norm LIKE :prefix THEN 1
                WHEN f.title_norm LIKE :contains THEN 2
                ELSE 3
              END,
              f.view_count DESC,
              f.updated_at DESC
            """, nativeQuery = true)
    List<Faq> suggestFaqs(@Param("prefix") String prefix,
                          @Param("contains") String contains,
                          @Param("now") LocalDateTime now,
                          org.springframework.data.domain.Pageable pageable);
}

