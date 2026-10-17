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

@Component
public class FaqViewCounter {

    private final Map<String, Instant> viewCache = new ConcurrentHashMap<>();
    private final FaqRepository faqRepository;
    private final SettingsService settingsService;

    public FaqViewCounter(FaqRepository faqRepository, SettingsService settingsService) {
        this.faqRepository = faqRepository;
        this.settingsService = settingsService;
    }

    @Transactional
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
    public void cleanup() {
        int dedupeMinutes = settingsService.getInt(SettingKeys.FAQ_VIEW_DEDUPE_MINUTES);
        if (dedupeMinutes <= 0) {
            dedupeMinutes = 15;
        }
        Instant cutoff = Instant.now().minus(Duration.ofMinutes(dedupeMinutes));
        viewCache.entrySet().removeIf(entry -> entry.getValue().isBefore(cutoff));
    }
}

