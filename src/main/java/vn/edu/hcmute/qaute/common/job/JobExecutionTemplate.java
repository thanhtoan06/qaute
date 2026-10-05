package vn.edu.hcmute.qaute.common.job;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JobExecutionTemplate {

    private static final Logger LOG = LoggerFactory.getLogger(JobExecutionTemplate.class);
    private static final long INFO_LOG_THRESHOLD_MILLIS = 500L;

    private final ConcurrentMap<String, AtomicBoolean> runningJobs = new ConcurrentHashMap<>();

    public void run(String jobName, Runnable task) {
        AtomicBoolean running = runningJobs.computeIfAbsent(jobName, key -> new AtomicBoolean());
        if (!running.compareAndSet(false, true)) {
            LOG.info("Job {} đang chạy, bỏ qua lần thực thi chồng lấp", jobName);
            return;
        }

        long start = System.nanoTime();
        try {
            task.run();
            long elapsedMillis = elapsedMillis(start);
            if (elapsedMillis > INFO_LOG_THRESHOLD_MILLIS) {
                LOG.info("Job {} xong sau {} ms", jobName, elapsedMillis);
            } else {
                LOG.debug("Job {} xong sau {} ms", jobName, elapsedMillis);
            }
        } catch (Exception exception) {
            long elapsedMillis = elapsedMillis(start);
            LOG.error("Job {} thất bại sau {} ms", jobName, elapsedMillis, exception);
        } finally {
            running.set(false);
        }
    }

    private long elapsedMillis(long startNanos) {
        return (System.nanoTime() - startNanos) / 1_000_000L;
    }
}
