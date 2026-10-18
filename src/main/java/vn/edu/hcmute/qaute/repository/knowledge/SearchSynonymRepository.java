package vn.edu.hcmute.qaute.repository.knowledge;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.knowledge.SearchSynonym;

public interface SearchSynonymRepository extends JpaRepository<SearchSynonym, Long> {

    List<SearchSynonym> findByActiveTrue();

    List<SearchSynonym> findAllByOrderByTermAscSynonymAsc();

    Optional<SearchSynonym> findByTermAndSynonym(String term, String synonym);
}

