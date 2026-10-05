package vn.edu.hcmute.qaute.repository.identity;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.identity.LoginHistory;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

    List<LoginHistory> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
