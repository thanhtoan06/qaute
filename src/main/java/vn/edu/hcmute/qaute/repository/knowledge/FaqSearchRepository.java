package vn.edu.hcmute.qaute.repository.knowledge;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.entity.knowledge.Faq;

public interface FaqSearchRepository extends JpaRepository<Faq, Long> {

    @Query(value = """
            SELECT f.id AS id,
                   f.title AS title,
                   f.slug AS slug,
                   f.summary AS summary,
                   f.body_text AS bodyText,
                   c.name AS categoryName,
                   f.view_count AS viewCount,
                   f.helpful_count AS helpfulCount,
                   f.updated_at AS updatedAt,
                   (5 * (CASE WHEN f.title_norm LIKE CONCAT('%', :q, '%') THEN 1 ELSE 0 END)
                    + 3 * (CASE WHEN EXISTS (
                        SELECT 1 FROM faq_tag_map ftm
                        JOIN faq_tags ft ON ft.id = ftm.tag_id
                        WHERE ftm.faq_id = f.id AND ft.name_norm LIKE CONCAT('%', :tagQuery, '%')
                      ) THEN 1 ELSE 0 END)
                    + 1 * (CASE WHEN f.body_norm LIKE CONCAT('%', :q, '%') THEN 1 ELSE 0 END)
                    + 0.5 * LOG10(1 + f.view_count)
                    + 1 / (1 + GREATEST(0, DATEDIFF(NOW(), f.updated_at)) / 365.0)) AS score
            FROM faqs f
            JOIN categories c ON c.id = f.category_id
            WHERE f.status = 'PUBLISHED'
              AND (f.publish_at IS NULL OR f.publish_at <= NOW())
              AND (
                  f.title_norm LIKE CONCAT('%', :q, '%')
                  OR f.body_norm LIKE CONCAT('%', :q, '%')
                  OR EXISTS (
                      SELECT 1 FROM faq_tag_map ftm2
                      JOIN faq_tags ft2 ON ft2.id = ftm2.tag_id
                      WHERE ftm2.faq_id = f.id AND ft2.name_norm LIKE CONCAT('%', :tagQuery, '%')
                  )
              )
              AND (:tagSlug IS NULL OR :tagSlug = '' OR EXISTS (
                  SELECT 1 FROM faq_tag_map ftm3
                  JOIN faq_tags ft3 ON ft3.id = ftm3.tag_id
                  WHERE ftm3.faq_id = f.id AND ft3.slug = :tagSlug
              ))
            ORDER BY
              CASE WHEN :sort = 'LATEST' THEN f.published_at END DESC,
              CASE WHEN :sort = 'MOST_VIEWED' THEN f.view_count END DESC,
              CASE WHEN :sort = 'MOST_HELPFUL' THEN f.helpful_count END DESC,
              CASE WHEN :sort = 'RELEVANCE' OR :sort NOT IN ('LATEST', 'MOST_VIEWED', 'MOST_HELPFUL') THEN
                  (5 * (CASE WHEN f.title_norm LIKE CONCAT('%', :q, '%') THEN 1 ELSE 0 END)
                   + 3 * (CASE WHEN EXISTS (
                       SELECT 1 FROM faq_tag_map ftm
                       JOIN faq_tags ft ON ft.id = ftm.tag_id
                       WHERE ftm.faq_id = f.id AND ft.name_norm LIKE CONCAT('%', :tagQuery, '%')
                     ) THEN 1 ELSE 0 END)
                   + 1 * (CASE WHEN f.body_norm LIKE CONCAT('%', :q, '%') THEN 1 ELSE 0 END)
                   + 0.5 * LOG10(1 + f.view_count)
                   + 1 / (1 + GREATEST(0, DATEDIFF(NOW(), f.updated_at)) / 365.0))
              END DESC,
              f.id DESC
            """,
            countQuery = """
            SELECT COUNT(DISTINCT f.id)
            FROM faqs f
            WHERE f.status = 'PUBLISHED'
              AND (f.publish_at IS NULL OR f.publish_at <= NOW())
              AND (
                  f.title_norm LIKE CONCAT('%', :q, '%')
                  OR f.body_norm LIKE CONCAT('%', :q, '%')
                  OR EXISTS (
                      SELECT 1 FROM faq_tag_map ftm
                      JOIN faq_tags ft ON ft.id = ftm.tag_id
                      WHERE ftm.faq_id = f.id AND ft.name_norm LIKE CONCAT('%', :tagQuery, '%')
                  )
              )
              AND (:tagSlug IS NULL OR :tagSlug = '' OR EXISTS (
                  SELECT 1 FROM faq_tag_map ftm3
                  JOIN faq_tags ft3 ON ft3.id = ftm3.tag_id
                  WHERE ftm3.faq_id = f.id AND ft3.slug = :tagSlug
              ))
            """,
            nativeQuery = true)
    Page<FaqSearchProjection> searchWithoutCategories(
            @Param("q") String q,
            @Param("tagQuery") String tagQuery,
            @Param("tagSlug") String tagSlug,
            @Param("sort") String sort,
            Pageable pageable);

    @Query(value = """
            SELECT f.id AS id,
                   f.title AS title,
                   f.slug AS slug,
                   f.summary AS summary,
                   f.body_text AS bodyText,
                   c.name AS categoryName,
                   f.view_count AS viewCount,
                   f.helpful_count AS helpfulCount,
                   f.updated_at AS updatedAt,
                   (5 * (CASE WHEN f.title_norm LIKE CONCAT('%', :q, '%') THEN 1 ELSE 0 END)
                    + 3 * (CASE WHEN EXISTS (
                        SELECT 1 FROM faq_tag_map ftm
                        JOIN faq_tags ft ON ft.id = ftm.tag_id
                        WHERE ftm.faq_id = f.id AND ft.name_norm LIKE CONCAT('%', :tagQuery, '%')
                      ) THEN 1 ELSE 0 END)
                    + 1 * (CASE WHEN f.body_norm LIKE CONCAT('%', :q, '%') THEN 1 ELSE 0 END)
                    + 0.5 * LOG10(1 + f.view_count)
                    + 1 / (1 + GREATEST(0, DATEDIFF(NOW(), f.updated_at)) / 365.0)) AS score
            FROM faqs f
            JOIN categories c ON c.id = f.category_id
            WHERE f.status = 'PUBLISHED'
              AND (f.publish_at IS NULL OR f.publish_at <= NOW())
              AND (
                  f.title_norm LIKE CONCAT('%', :q, '%')
                  OR f.body_norm LIKE CONCAT('%', :q, '%')
                  OR EXISTS (
                      SELECT 1 FROM faq_tag_map ftm2
                      JOIN faq_tags ft2 ON ft2.id = ftm2.tag_id
                      WHERE ftm2.faq_id = f.id AND ft2.name_norm LIKE CONCAT('%', :tagQuery, '%')
                  )
              )
              AND f.category_id IN (:categoryIds)
              AND (:tagSlug IS NULL OR :tagSlug = '' OR EXISTS (
                  SELECT 1 FROM faq_tag_map ftm3
                  JOIN faq_tags ft3 ON ft3.id = ftm3.tag_id
                  WHERE ftm3.faq_id = f.id AND ft3.slug = :tagSlug
              ))
            ORDER BY
              CASE WHEN :sort = 'LATEST' THEN f.published_at END DESC,
              CASE WHEN :sort = 'MOST_VIEWED' THEN f.view_count END DESC,
              CASE WHEN :sort = 'MOST_HELPFUL' THEN f.helpful_count END DESC,
              CASE WHEN :sort = 'RELEVANCE' OR :sort NOT IN ('LATEST', 'MOST_VIEWED', 'MOST_HELPFUL') THEN
                  (5 * (CASE WHEN f.title_norm LIKE CONCAT('%', :q, '%') THEN 1 ELSE 0 END)
                   + 3 * (CASE WHEN EXISTS (
                       SELECT 1 FROM faq_tag_map ftm
                       JOIN faq_tags ft ON ft.id = ftm.tag_id
                       WHERE ftm.faq_id = f.id AND ft.name_norm LIKE CONCAT('%', :tagQuery, '%')
                     ) THEN 1 ELSE 0 END)
                   + 1 * (CASE WHEN f.body_norm LIKE CONCAT('%', :q, '%') THEN 1 ELSE 0 END)
                   + 0.5 * LOG10(1 + f.view_count)
                   + 1 / (1 + GREATEST(0, DATEDIFF(NOW(), f.updated_at)) / 365.0))
              END DESC,
              f.id DESC
            """,
            countQuery = """
            SELECT COUNT(DISTINCT f.id)
            FROM faqs f
            WHERE f.status = 'PUBLISHED'
              AND (f.publish_at IS NULL OR f.publish_at <= NOW())
              AND (
                  f.title_norm LIKE CONCAT('%', :q, '%')
                  OR f.body_norm LIKE CONCAT('%', :q, '%')
                  OR EXISTS (
                      SELECT 1 FROM faq_tag_map ftm
                      JOIN faq_tags ft ON ft.id = ftm.tag_id
                      WHERE ftm.faq_id = f.id AND ft.name_norm LIKE CONCAT('%', :tagQuery, '%')
                  )
              )
              AND f.category_id IN (:categoryIds)
              AND (:tagSlug IS NULL OR :tagSlug = '' OR EXISTS (
                  SELECT 1 FROM faq_tag_map ftm3
                  JOIN faq_tags ft3 ON ft3.id = ftm3.tag_id
                  WHERE ftm3.faq_id = f.id AND ft3.slug = :tagSlug
              ))
            """,
            nativeQuery = true)
    Page<FaqSearchProjection> searchWithCategories(
            @Param("q") String q,
            @Param("tagQuery") String tagQuery,
            @Param("categoryIds") List<Long> categoryIds,
            @Param("tagSlug") String tagSlug,
            @Param("sort") String sort,
            Pageable pageable);
}

