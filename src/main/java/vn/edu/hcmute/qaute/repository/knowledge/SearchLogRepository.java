package vn.edu.hcmute.qaute.repository.knowledge;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.entity.knowledge.SearchLog;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {
    @Query("SELECT l.queryNorm, COUNT(l.id) FROM SearchLog l "
            + "WHERE l.resultCount = 0 AND l.createdAt >= :since AND l.queryNorm <> '' "
            + "GROUP BY l.queryNorm ORDER BY COUNT(l.id) DESC, l.queryNorm ASC")
    List<Object[]> findTopNoResultQueries(@Param("since") LocalDateTime since, Pageable pageable);
}

