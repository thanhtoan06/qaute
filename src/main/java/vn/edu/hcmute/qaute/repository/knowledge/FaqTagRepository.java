package vn.edu.hcmute.qaute.repository.knowledge;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.entity.knowledge.FaqTag;

public interface FaqTagRepository extends JpaRepository<FaqTag, Long> {

    Optional<FaqTag> findBySlug(String slug);

    List<FaqTag> findAllByOrderByNameAsc();

    @Query("SELECT t.name, t.slug, COUNT(f.id) FROM Faq f JOIN f.tags t "
            + "WHERE f.status = vn.edu.hcmute.qaute.common.constant.FaqStatus.PUBLISHED "
            + "AND (f.publishAt IS NULL OR f.publishAt <= :now) "
            + "GROUP BY t.id, t.name, t.slug "
            + "ORDER BY COUNT(f.id) DESC")
    List<Object[]> findPopularTags(@Param("now") LocalDateTime now, Pageable pageable);
}

