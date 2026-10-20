package vn.edu.hcmute.qaute.service.appointment;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.common.job.JobExecutionTemplate;

@Component
public class SlotMaintenanceJob {
    private final JobExecutionTemplate jobs;
    private final SlotGenerationService slots;

    public SlotMaintenanceJob(JobExecutionTemplate jobs, SlotGenerationService slots) {
        this.jobs = jobs;
        this.slots = slots;
    }

    @Scheduled(cron = "0 10 0 * * *")
    public void maintain() {
        jobs.run("appointment-slots", () -> {
            slots.generateAll();
            slots.expirePast();
        });
    }
}
