package vn.edu.hcmute.qaute.service.identity;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.job.JobExecutionTemplate;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.OtpCodeRepository;
import vn.edu.hcmute.qaute.repository.identity.StudentProfileRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@Component
public class UnverifiedAccountCleanupJob {

    private final JobExecutionTemplate jobExecutionTemplate;
    private final SettingsService settingsService;
    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final OtpCodeRepository otpCodeRepository;

    public UnverifiedAccountCleanupJob(JobExecutionTemplate jobExecutionTemplate,
                                       SettingsService settingsService,
                                       UserRepository userRepository,
                                       StudentProfileRepository studentProfileRepository,
                                       OtpCodeRepository otpCodeRepository) {
        this.jobExecutionTemplate = jobExecutionTemplate;
        this.settingsService = settingsService;
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.otpCodeRepository = otpCodeRepository;
    }

    @Scheduled(cron = "0 15 * * * *")
    @Transactional
    public void cleanup() {
        jobExecutionTemplate.run("unverified-account-cleanup", () -> {
            LocalDateTime cutoff = LocalDateTime.now().minusHours(
                    settingsService.getInt(SettingKeys.AUTH_UNVERIFIED_CLEANUP_HOURS));
            List<User> users = userRepository.findByStatusAndCreatedAtBefore(
                    UserStatus.PENDING_VERIFICATION, cutoff);
            users.forEach(user -> {
                otpCodeRepository.deleteByUserId(user.getId());
                studentProfileRepository.findByUserId(user.getId())
                        .ifPresent(studentProfileRepository::delete);
                userRepository.delete(user);
            });
        });
    }
}
