package vn.edu.hcmute.qaute.service.knowledge;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.entity.knowledge.SearchLog;
import vn.edu.hcmute.qaute.repository.knowledge.SearchLogRepository;

// Xử lý nghiệp vụ SearchLog trong hệ thống.
@Service
@Transactional
public class SearchLogServiceImpl implements SearchLogService {

    private final SearchLogRepository searchLogRepository;

    // Khởi tạo dịch vụ ghi nhật ký tìm kiếm.
    public SearchLogServiceImpl(SearchLogRepository searchLogRepository) {
        this.searchLogRepository = searchLogRepository;
    }

    @Override
    // Lưu nhật ký người dùng nhấp vào kết quả tìm kiếm.
    public void recordClick(String query, Long faqId, String source, Long userId, String ipHash) {
        String qNorm = query != null ? TextUtil.normalizeVi(query) : "";
        if (qNorm.length() > 200) {
            qNorm = qNorm.substring(0, 200);
        }
        SearchLog log = SearchLog.builder()
                .userId(userId)
                .queryNorm(qNorm)
                .clickedFaqId(faqId)
                .source(source != null && !source.isBlank() ? source : "SEARCH")
                .ipHash(ipHash)
                .build();
        searchLogRepository.save(log);
    }
}
