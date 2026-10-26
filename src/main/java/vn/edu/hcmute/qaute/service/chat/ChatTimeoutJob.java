package vn.edu.hcmute.qaute.service.chat;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.job.JobExecutionTemplate;
import vn.edu.hcmute.qaute.repository.chat.ChatSessionRepository;
import vn.edu.hcmute.qaute.service.notification.NotificationService;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.system.SettingsService;

// Xử lý nghiệp vụ ChatTimeoutJob trong hệ thống.
@Component
public class ChatTimeoutJob {
    private final JobExecutionTemplate jobs;
    private final TransactionTemplate transactions;
    private final ChatSessionRepository sessions;
    private final SettingsService settings;
    private final ChatEventPublisher publisher;
    private final NotificationService notifications;
    private final ApplicationEventPublisher events;
    private final AuditService audit;

    // Khởi tạo tác vụ hết hạn với dịch vụ xử lý và các kho dữ liệu liên quan.
    public ChatTimeoutJob(JobExecutionTemplate jobs, TransactionTemplate transactions,
            ChatSessionRepository sessions, SettingsService settings, ChatEventPublisher publisher,
            NotificationService notifications, ApplicationEventPublisher events, AuditService audit) {
        this.jobs = jobs;
        this.transactions = transactions;
        this.sessions = sessions;
        this.settings = settings;
        this.publisher = publisher;
        this.notifications = notifications;
        this.events = events;
        this.audit = audit;
    }

    @Scheduled(fixedDelay = 60_000)
    // Chạy xử lý các phiên chat chờ hoặc không hoạt động quá hạn.
    public void run() {
        jobs.run("chat-timeout", this::process);
    }

    // Tìm và xử lý các phiên chờ hoặc hoạt động đã vượt thời hạn cấu hình.
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

    // Đánh dấu phiên chờ quá hạn và thông báo cho sinh viên.
    private void expireWaiting(Long sessionId, LocalDateTime cutoff) {
        var session = sessions.findById(sessionId).orElse(null);
        if (session == null || sessions.expireWaiting(sessionId, cutoff, LocalDateTime.now()) == 0) {
            return;
        }
        audit.log(AuditActions.CHAT_SESSION_EXPIRED, "ChatSession", sessionId,
                "status=WAITING", "status=EXPIRED; reason=WAITING_TIMEOUT");
        publisher.sendToSession(sessionId, Map.of("type", "SESSION_EXPIRED"));
        notifications.notifyUser(session.getStudentId(), NotificationType.CHAT_MISSED,
                "Chưa có tư vấn viên tiếp nhận", "Phiên chat chưa được tiếp nhận. Bạn có thể tạo yêu cầu tư vấn.",
                "/student/chat", "CHAT_SESSION", sessionId);
        events.publishEvent(new ChatQueueChangedEvent(session.getCategoryId()));
    }

    // Kết thúc phiên chat đã không hoạt động quá thời hạn.
    private void endIdle(Long sessionId, LocalDateTime cutoff) {
        if (sessions.endIdle(sessionId, cutoff, LocalDateTime.now()) > 0) {
            audit.log(AuditActions.CHAT_SESSION_ENDED, "ChatSession", sessionId,
                    "status=ACTIVE", "status=ENDED; reason=IDLE_TIMEOUT");
            publisher.sendToSession(sessionId, Map.of("type", "SESSION_ENDED"));
        }
    }
}
