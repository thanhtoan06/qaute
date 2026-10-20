package vn.edu.hcmute.qaute.service.chat;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.job.JobExecutionTemplate;
import vn.edu.hcmute.qaute.repository.chat.ChatSessionRepository;
import vn.edu.hcmute.qaute.service.notification.NotificationService;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@Component
public class ChatTimeoutJob {
    private final JobExecutionTemplate jobs;
    private final TransactionTemplate transactions;
    private final ChatSessionRepository sessions;
    private final SettingsService settings;
    private final ChatEventPublisher publisher;
    private final NotificationService notifications;
    private final ApplicationEventPublisher events;

    public ChatTimeoutJob(JobExecutionTemplate jobs, TransactionTemplate transactions,
            ChatSessionRepository sessions, SettingsService settings, ChatEventPublisher publisher,
            NotificationService notifications, ApplicationEventPublisher events) {
        this.jobs = jobs;
        this.transactions = transactions;
        this.sessions = sessions;
        this.settings = settings;
        this.publisher = publisher;
        this.notifications = notifications;
        this.events = events;
    }

    @Scheduled(fixedDelay = 60_000)
    public void run() {
        jobs.run("chat-timeout", this::process);
    }

    private void process() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime waitingCutoff = now.minusMinutes(settings.getInt(SettingKeys.CHAT_WAITING_TIMEOUT_MINUTES));
        for (var session : sessions.findByStatusAndLastMessageAtBeforeOrderByLastMessageAtAsc(
                ChatSessionStatus.WAITING, waitingCutoff)) {
            transactions.executeWithoutResult(ignored -> expireWaiting(session.getId(), waitingCutoff));
        }
        LocalDateTime idleCutoff = now.minusMinutes(settings.getInt(SettingKeys.CHAT_IDLE_TIMEOUT_MINUTES));
        for (var session : sessions.findByStatusAndLastMessageAtBeforeOrderByLastMessageAtAsc(
                ChatSessionStatus.ACTIVE, idleCutoff)) {
            transactions.executeWithoutResult(ignored -> endIdle(session.getId(), idleCutoff));
        }
    }

    private void expireWaiting(Long sessionId, LocalDateTime cutoff) {
        var session = sessions.findById(sessionId).orElse(null);
        if (session == null || sessions.expireWaiting(sessionId, cutoff, LocalDateTime.now()) == 0) {
            return;
        }
        publisher.sendToSession(sessionId, Map.of("type", "SESSION_EXPIRED"));
        notifications.notifyUser(session.getStudentId(), NotificationType.CHAT_MISSED,
                "Chưa có tư vấn viên tiếp nhận", "Phiên chat chưa được tiếp nhận. Bạn có thể tạo yêu cầu tư vấn.",
                "/student/chat", "CHAT_SESSION", sessionId);
        events.publishEvent(new ChatQueueChangedEvent(session.getCategoryId()));
    }

    private void endIdle(Long sessionId, LocalDateTime cutoff) {
        if (sessions.endIdle(sessionId, cutoff, LocalDateTime.now()) > 0) {
            publisher.sendToSession(sessionId, Map.of("type", "SESSION_ENDED"));
        }
    }
}
