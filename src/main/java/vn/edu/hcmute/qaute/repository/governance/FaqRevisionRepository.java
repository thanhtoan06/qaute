package vn.edu.hcmute.qaute.repository.governance;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.governance.FaqRevision;

public interface FaqRevisionRepository extends JpaRepository<FaqRevision, Long> {
    List<FaqRevision> findByFaqIdOrderByVersionNoDesc(Long faqId);

    Optional<FaqRevision> findByFaqIdAndVersionNo(Long faqId, Integer versionNo);
}
