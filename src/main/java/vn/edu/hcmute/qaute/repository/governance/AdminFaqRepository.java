package vn.edu.hcmute.qaute.repository.governance;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hcmute.qaute.entity.knowledge.Faq;

public interface AdminFaqRepository extends JpaRepository<Faq, Long>, JpaSpecificationExecutor<Faq> {
    @Query("select f.status, count(f.id) from Faq f group by f.status")
    List<Object[]> countGroupedByStatus();

    @Query("select distinct f.authorId from Faq f where f.authorId is not null")
    List<Long> findDistinctAuthorIds();
}
