package vn.edu.hcmute.qaute.repository.knowledge;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.knowledge.FaqView;

public interface FaqViewRepository extends JpaRepository<FaqView, Long> {
    Optional<FaqView> findByUserIdAndFaqId(Long userId, Long faqId);

    Page<FaqView> findByUserIdOrderByLastViewedAtDesc(Long userId, Pageable pageable);

    List<FaqView> findByUserIdAndFaqIdIn(Long userId, Collection<Long> faqIds);
}
