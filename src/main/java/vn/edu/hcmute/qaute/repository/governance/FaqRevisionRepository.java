package vn.edu.hcmute.qaute.repository.governance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.governance.FaqRevision;

// Khai báo truy vấn lưu trữ dữ liệu FaqRevision.
public interface FaqRevisionRepository extends JpaRepository<FaqRevision, Long> {
    // Lấy lịch sử phiên bản câu hỏi theo thứ tự mới nhất.
    List<FaqRevision> findByFaqIdOrderByVersionNoDesc(Long faqId);

    // Lấy lịch sử phiên bản câu hỏi theo trang.
    Page<FaqRevision> findByFaqIdOrderByVersionNoDesc(Long faqId, Pageable pageable);

    // Tìm phiên bản câu hỏi theo mã câu hỏi và số phiên bản.
    Optional<FaqRevision> findByFaqIdAndVersionNo(Long faqId, Integer versionNo);
}
