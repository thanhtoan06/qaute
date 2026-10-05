package vn.edu.hcmute.qaute.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final Map<String, ConsumptionProbe> lastProbes = new ConcurrentHashMap<>();

    public boolean tryConsume(String key, int capacity, Duration period) {
        Bucket bucket = buckets.computeIfAbsent(key, ignored -> Bucket.builder()
                .addLimit(Bandwidth.classic(capacity, Refill.intervally(capacity, period)))
                .build());
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        lastProbes.put(key, probe);
        return probe.isConsumed();
    }

    public long secondsToWait(String key) {
        ConsumptionProbe probe = lastProbes.get(key);
        if (probe == null || probe.isConsumed()) {
            return 0;
        }
        return Math.max(1, (probe.getNanosToWaitForRefill() + 999_999_999L) / 1_000_000_000L);
    }

    @Scheduled(fixedDelay = 3_600_000L)
    public void cleanup() {
        buckets.clear();
        lastProbes.clear();
    }
}
