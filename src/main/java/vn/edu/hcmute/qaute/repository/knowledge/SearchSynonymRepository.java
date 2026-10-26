package vn.edu.hcmute.qaute.repository.knowledge;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.knowledge.SearchSynonym;

// Khai báo truy vấn lưu trữ dữ liệu SearchSynonym.
public interface SearchSynonymRepository extends JpaRepository<SearchSynonym, Long> {

    // Lấy các từ đồng nghĩa đang hoạt động.
    List<SearchSynonym> findByActiveTrue();

    // Lấy tất cả từ đồng nghĩa theo thuật ngữ và từ đồng nghĩa.
    List<SearchSynonym> findAllByOrderByTermAscSynonymAsc();

    // Tìm từ đồng nghĩa theo thuật ngữ và giá trị từ đồng nghĩa.
    Optional<SearchSynonym> findByTermAndSynonym(String term, String synonym);
}
