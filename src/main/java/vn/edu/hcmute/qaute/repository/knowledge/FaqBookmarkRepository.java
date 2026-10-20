package vn.edu.hcmute.qaute.repository.knowledge;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.knowledge.FaqBookmark;

public interface FaqBookmarkRepository extends JpaRepository<FaqBookmark, Long> {
    Optional<FaqBookmark> findByUserIdAndFaqId(Long userId, Long faqId);

    Page<FaqBookmark> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    List<FaqBookmark> findByUserIdAndFaqIdIn(Long userId, Collection<Long> faqIds);
}
