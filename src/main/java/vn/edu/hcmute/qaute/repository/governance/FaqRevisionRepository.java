package vn.edu.hcmute.qaute.repository.governance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.governance.FaqRevision;

public interface FaqRevisionRepository extends JpaRepository<FaqRevision, Long> {
    List<FaqRevision> findByFaqIdOrderByVersionNoDesc(Long faqId);
    Page<FaqRevision> findByFaqIdOrderByVersionNoDesc(Long faqId, Pageable pageable);

    Optional<FaqRevision> findByFaqIdAndVersionNo(Long faqId, Integer versionNo);
}
