package vn.edu.hcmute.qaute.service.media;

import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.job.JobExecutionTemplate;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;

// Xử lý nghiệp vụ MediaOrphanCleanupJob trong hệ thống.
@Component
public class MediaOrphanCleanupJob {
    private final JobExecutionTemplate jobs;
    private final MediaAssetRepository assets;
    private final MediaService media;

    // Khởi tạo tác vụ dọn dẹp tệp mồ côi.
    public MediaOrphanCleanupJob(JobExecutionTemplate jobs, MediaAssetRepository assets, MediaService media) {
        this.jobs = jobs;
        this.assets = assets;
        this.media = media;
    }

    @Scheduled(cron = "0 30 3 * * *")
    // Xóa các tài nguyên mồ côi đã tồn tại quá 24 giờ.
    public void removeOrphans() {
        jobs.run("media-orphan-cleanup", () -> assets
                .findByStatusAndCreatedAtBefore(MediaStatus.ORPHAN, LocalDateTime.now().minusHours(24))
                .forEach(asset -> media.delete(asset.getId())));
    }
}
