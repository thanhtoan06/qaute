package vn.edu.hcmute.qaute.service.email;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.EmailStatus;
import vn.edu.hcmute.qaute.common.job.JobExecutionTemplate;
import vn.edu.hcmute.qaute.entity.notification.EmailOutbox;
import vn.edu.hcmute.qaute.repository.notification.EmailOutboxRepository;

// Xử lý nghiệp vụ EmailOutboxProcessor trong hệ thống.
@Component
public class EmailOutboxProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(EmailOutboxProcessor.class);
    private static final int MAX_ATTEMPTS = 5;

    private final EmailOutboxRepository emailOutboxRepository;
    private final EmailTemplateRenderer templateRenderer;
    private final JavaMailSender mailSender;
    private final ObjectMapper objectMapper;
    private final JobExecutionTemplate jobExecutionTemplate;
    private final String mailUsername;
    private final String mailFromName;

    // Khởi tạo bộ xử lý email chờ cùng kho dữ liệu và dịch vụ gửi email.
    public EmailOutboxProcessor(EmailOutboxRepository emailOutboxRepository,
                                EmailTemplateRenderer templateRenderer,
                                JavaMailSender mailSender,
                                ObjectMapper objectMapper,
                                JobExecutionTemplate jobExecutionTemplate,
                                @Value("${spring.mail.username:}") String mailUsername,
                                @Value("${app.mail.from-name:QAUTE - Tư vấn Sinh viên HCMUTE}") String mailFromName) {
        this.emailOutboxRepository = emailOutboxRepository;
        this.templateRenderer = templateRenderer;
        this.mailSender = mailSender;
        this.objectMapper = objectMapper;
        this.jobExecutionTemplate = jobExecutionTemplate;
        this.mailUsername = mailUsername;
        this.mailFromName = mailFromName;
    }

    // Chạy định kỳ việc xử lý email đang chờ.
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processScheduled() {
        jobExecutionTemplate.run("email-outbox", this::processPending);
    }

    // Lấy và xử lý các email đang đến hạn gửi.
    protected void processPending() {
        List<EmailOutbox> messages = emailOutboxRepository
                .findTop20ByStatusAndNextAttemptAtLessThanEqualOrderByIdAsc(
                        EmailStatus.PENDING, LocalDateTime.now());
        for (EmailOutbox message : messages) {
            processOne(message);
        }
    }

    // Gửi một email và lưu kết quả xử lý.
    private void processOne(EmailOutbox message) {
        try {
            Map<String, String> model = objectMapper.readValue(
                    message.getPayloadJson(), new TypeReference<>() { });
            String html = templateRenderer.render(message.getTemplate(), model);
            if (mailUsername == null || mailUsername.isBlank()) {
                LOG.info("Dev email, subject={}, to={}, nội dung={}",
                        message.getSubject(), message.getToEmail(), html);
            } else {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(
                        mimeMessage, false, "UTF-8");
                if (mailFromName != null && !mailFromName.isBlank()) {
                    helper.setFrom(mailUsername, mailFromName);
                } else {
                    helper.setFrom(mailUsername);
                }
                helper.setTo(message.getToEmail());
                helper.setSubject(message.getSubject());
                helper.setText(html, true);
                mailSender.send(mimeMessage);
            }
            message.setStatus(EmailStatus.SENT);
            message.setSentAt(LocalDateTime.now());
            message.setLastError(null);
        } catch (Exception exception) {
            registerFailure(message, exception);
        }
        emailOutboxRepository.save(message);
    }

    // Cập nhật trạng thái và lịch thử lại khi gửi email thất bại.
    private void registerFailure(EmailOutbox message, Exception exception) {
        int attempts = message.getAttempts() + 1;
        message.setAttempts(attempts);
        message.setLastError(truncateError(exception.getMessage()));
        if (attempts >= MAX_ATTEMPTS) {
            message.setStatus(EmailStatus.FAILED);
            return;
        }
        long delayMinutes = 1L << Math.min(attempts, 30);
        message.setNextAttemptAt(LocalDateTime.now().plusMinutes(delayMinutes));
        LOG.error("Gửi email thất bại, id={}, lần thử {}", message.getId(), attempts, exception);
    }

    // Giới hạn nội dung lỗi lưu trong bản ghi email.
    private String truncateError(String message) {
        String value = message == null || message.isBlank() ? "Lỗi không xác định" : message;
        return value.length() <= 500 ? value : value.substring(0, 500);
    }
}
