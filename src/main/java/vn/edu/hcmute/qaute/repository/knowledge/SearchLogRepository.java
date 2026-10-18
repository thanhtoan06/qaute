package vn.edu.hcmute.qaute.repository.knowledge;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.knowledge.SearchLog;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {
}

