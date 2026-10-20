package vn.edu.hcmute.qaute.service.governance;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AnnouncementAudience;
import vn.edu.hcmute.qaute.common.constant.AnnouncementStatus;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.job.JobExecutionTemplate;
import vn.edu.hcmute.qaute.entity.governance.Announcement;
import vn.edu.hcmute.qaute.repository.governance.AnnouncementRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.notification.NotificationService;

@Component
public class AnnouncementJob {
    private final JobExecutionTemplate jobs;
    private final Publisher publisher;

    public AnnouncementJob(JobExecutionTemplate jobs, Publisher publisher) {
        this.jobs = jobs;
        this.publisher = publisher;
    }

    @Scheduled(fixedDelay = 60000)
    public void run() {
        jobs.run("announcements", publisher::process);
    }

    @Component
    public static class Publisher {
        private final AnnouncementRepository announcements;
        private final UserRepository users;
        private final NotificationService notifications;

        public Publisher(AnnouncementRepository announcements, UserRepository users,
                         NotificationService notifications) {
            this.announcements = announcements;
            this.users = users;
            this.notifications = notifications;
        }

        @Transactional
        public void process() {
            LocalDateTime now = LocalDateTime.now();
            for (Announcement a : announcements.findByStatusAndPublishAtLessThanEqual(AnnouncementStatus.DRAFT, now)) {
                if (!a.isAutoPublish()) continue;
                if (a.getExpiresAt() != null && !a.getExpiresAt().isAfter(now)) {
                    a.setStatus(AnnouncementStatus.EXPIRED);
                    continue;
                }
                a.setStatus(AnnouncementStatus.PUBLISHED);
                a.setAutoPublish(false);
            }
            announcements.flush();
            for (Announcement a : announcements.findByStatusAndPublishAtLessThanEqual(AnnouncementStatus.PUBLISHED, now)) {
                if (a.getExpiresAt() != null && !a.getExpiresAt().isAfter(now)) {
                    a.setStatus(AnnouncementStatus.EXPIRED);
                } else if (a.isNotifyOnPublish() && !a.isNotified()) {
                    var recipients = new LinkedHashSet<Long>();
                    if (a.getAudience() == AnnouncementAudience.ALL || a.getAudience() == AnnouncementAudience.STUDENT)
                        recipients.addAll(users.findIdsByRoleCodeAndStatus(RoleCode.STUDENT, UserStatus.ACTIVE));
                    if (a.getAudience() == AnnouncementAudience.ALL || a.getAudience() == AnnouncementAudience.MANAGER)
                        recipients.addAll(users.findIdsByRoleCodeAndStatus(RoleCode.MANAGER, UserStatus.ACTIVE));
                    if (a.getAudience() == AnnouncementAudience.ALL)
                        recipients.addAll(users.findIdsByRoleCodeAndStatus(RoleCode.ADMIN, UserStatus.ACTIVE));
                    notifications.notifyUsers(recipients, NotificationType.ANNOUNCEMENT_PUBLISHED,
                            a.getTitle(), a.getSummary(), "/announcements/" + a.getId(), "announcement", a.getId());
                    a.setNotified(true);
                }
            }
            for (Announcement a : announcements.findByStatusAndExpiresAtLessThanEqual(AnnouncementStatus.PUBLISHED, now)) {
                a.setStatus(AnnouncementStatus.EXPIRED);
            }
        }
    }
}
