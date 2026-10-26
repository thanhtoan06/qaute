package vn.edu.hcmute.qaute.service.knowledge;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.repository.knowledge.FaqRepository;
import vn.edu.hcmute.qaute.service.system.SettingsService;

// Xử lý nghiệp vụ FaqViewCounter trong hệ thống.
@Component
public class FaqViewCounter {

    private final Map<String, Instant> viewCache = new ConcurrentHashMap<>();
    private final FaqRepository faqRepository;
    private final SettingsService settingsService;

    // Khởi tạo bộ đếm lượt xem và các dịch vụ phụ thuộc.
    public FaqViewCounter(FaqRepository faqRepository, SettingsService settingsService) {
        this.faqRepository = faqRepository;
        this.settingsService = settingsService;
    }

    @Transactional
    // Ghi nhận lượt xem nếu người xem chưa vượt giới hạn trùng lặp.
    public void recordView(Long faqId, String viewerKey) {
        if (faqId == null || viewerKey == null || viewerKey.isBlank()) {
            return;
        }

        String key = faqId + ":" + viewerKey.trim();
        Instant now = Instant.now();
        int dedupeMinutes = settingsService.getInt(SettingKeys.FAQ_VIEW_DEDUPE_MINUTES);
        if (dedupeMinutes <= 0) {
            dedupeMinutes = 15;
        }

        Instant lastView = viewCache.get(key);
        if (lastView == null || Duration.between(lastView, now).toMinutes() >= dedupeMinutes) {
            viewCache.put(key, now);
            faqRepository.incrementViewCount(faqId);
        }
    }

    @Scheduled(fixedDelay = 600000)
    // Loại bỏ các khóa lượt xem đã hết thời gian lưu.
    public void cleanup() {
        int dedupeMinutes = settingsService.getInt(SettingKeys.FAQ_VIEW_DEDUPE_MINUTES);
        if (dedupeMinutes <= 0) {
            dedupeMinutes = 15;
        }
        Instant cutoff = Instant.now().minus(Duration.ofMinutes(dedupeMinutes));
        viewCache.entrySet().removeIf(entry -> entry.getValue().isBefore(cutoff));
    }
}
