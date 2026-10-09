package vn.edu.hcmute.qaute.service.governance;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.job.JobExecutionTemplate;
import vn.edu.hcmute.qaute.repository.governance.AuditLogRepository;
import vn.edu.hcmute.qaute.service.system.SettingsService;

/**
 * Mỗi ngày lúc 04:00 xóa nhật ký cũ hơn số ngày cấu hình ở khóa retention.audit.days.
 */
@Component
public class AuditRetentionJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditRetentionJob.class);

    private final JobExecutionTemplate jobExecutionTemplate;
    private final SettingsService settingsService;
    private final AuditLogRepository auditLogRepository;

    public AuditRetentionJob(JobExecutionTemplate jobExecutionTemplate,
                             SettingsService settingsService,
                             AuditLogRepository auditLogRepository) {
        this.jobExecutionTemplate = jobExecutionTemplate;
        this.settingsService = settingsService;
        this.auditLogRepository = auditLogRepository;
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void purgeOldAuditLogs() {
        jobExecutionTemplate.run("audit-retention", () -> {
            int retentionDays = settingsService.getInt(SettingKeys.RETENTION_AUDIT_DAYS);
            LocalDateTime threshold = LocalDateTime.now().minusDays(retentionDays);
            long deleted = auditLogRepository.deleteByCreatedAtBefore(threshold);
            if (deleted > 0) {
                LOGGER.info("Đã xóa {} bản ghi audit cũ hơn {} ngày", deleted, retentionDays);
            } else {
                LOGGER.debug("Không có bản ghi audit nào cũ hơn {} ngày", retentionDays);
            }
        });
    }
}
