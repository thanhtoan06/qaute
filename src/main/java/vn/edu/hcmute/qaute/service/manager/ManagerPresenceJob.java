package vn.edu.hcmute.qaute.service.manager;

import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.common.job.JobExecutionTemplate;

// Xử lý nghiệp vụ ManagerPresenceJob trong hệ thống.
@Component
public class ManagerPresenceJob {
    private final JobExecutionTemplate jobs;
    private final ManagerProfileService profiles;

    // Khởi tạo tác vụ cập nhật trạng thái trực của tư vấn viên.
    public ManagerPresenceJob(JobExecutionTemplate jobs, ManagerProfileService profiles) {
        this.jobs = jobs;
        this.profiles = profiles;
    }

    // Đưa tư vấn viên không hoạt động về trạng thái ngoại tuyến.
    @Scheduled(fixedDelay = 60000)
    public void expireInactive() {
        jobs.run("manager-presence", () ->
                profiles.expireStalePresence(LocalDateTime.now().minusMinutes(5)));
    }
}
