package vn.edu.hcmute.qaute.service.knowledge;

public interface SearchLogService {

    void recordClick(String query, Long faqId, String source, Long userId, String ipHash);
}

