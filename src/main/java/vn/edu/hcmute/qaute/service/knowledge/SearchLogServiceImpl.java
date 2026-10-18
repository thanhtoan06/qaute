package vn.edu.hcmute.qaute.service.knowledge;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.entity.knowledge.SearchLog;
import vn.edu.hcmute.qaute.repository.knowledge.SearchLogRepository;

@Service
@Transactional
public class SearchLogServiceImpl implements SearchLogService {

    private final SearchLogRepository searchLogRepository;

    public SearchLogServiceImpl(SearchLogRepository searchLogRepository) {
        this.searchLogRepository = searchLogRepository;
    }

    @Override
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

