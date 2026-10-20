package vn.edu.hcmute.qaute.repository.knowledge;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.entity.knowledge.FaqFeedback;

public interface FaqFeedbackRepository extends JpaRepository<FaqFeedback, Long> {
    Optional<FaqFeedback> findByFaqIdAndUserId(Long faqId, Long userId);

    List<FaqFeedback> findByUserIdAndFaqIdIn(Long userId, Collection<Long> faqIds);

    @Query("SELECT COUNT(f.id) FROM FaqFeedback f WHERE f.faqId = :faqId AND f.helpful = :helpful")
    long countByFaqIdAndHelpful(@Param("faqId") Long faqId, @Param("helpful") boolean helpful);
}
