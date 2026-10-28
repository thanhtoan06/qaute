package vn.edu.hcmute.qaute.service.appointment;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.common.job.JobExecutionTemplate;

// Xử lý nghiệp vụ SlotMaintenanceJob trong hệ thống.
@Component
public class SlotMaintenanceJob {
    private final JobExecutionTemplate jobs;
    private final SlotGenerationService slots;

    // Khởi tạo tác vụ bảo trì khung giờ hẹn.
    public SlotMaintenanceJob(JobExecutionTemplate jobs, SlotGenerationService slots) {
        this.jobs = jobs;
        this.slots = slots;
    }

    // Tạo khung giờ mới và hết hạn các khung giờ đã qua.
    @Scheduled(cron = "0 10 0 * * *", zone = "Asia/Ho_Chi_Minh")
    public void maintain() {
        jobs.run("appointment-slots", () -> {
            slots.generateAll();
            slots.expirePast();
        });
    }
}
