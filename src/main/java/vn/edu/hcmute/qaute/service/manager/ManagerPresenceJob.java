package vn.edu.hcmute.qaute.service.manager;

import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.common.job.JobExecutionTemplate;

@Component
public class ManagerPresenceJob {
    private final JobExecutionTemplate jobs;
    private final ManagerProfileService profiles;

    public ManagerPresenceJob(JobExecutionTemplate jobs, ManagerProfileService profiles) {
        this.jobs = jobs;
        this.profiles = profiles;
    }

    @Scheduled(fixedDelay = 60000)
    public void expireInactive() {
        jobs.run("manager-presence", () ->
                profiles.expireStalePresence(LocalDateTime.now().minusMinutes(5)));
    }
}
