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

    public EmailOutboxProcessor(EmailOutboxRepository emailOutboxRepository,
                                EmailTemplateRenderer templateRenderer,
                                JavaMailSender mailSender,
                                ObjectMapper objectMapper,
                                JobExecutionTemplate jobExecutionTemplate,
                                @Value("${spring.mail.username:}") String mailUsername) {
        this.emailOutboxRepository = emailOutboxRepository;
        this.templateRenderer = templateRenderer;
        this.mailSender = mailSender;
        this.objectMapper = objectMapper;
        this.jobExecutionTemplate = jobExecutionTemplate;
        this.mailUsername = mailUsername;
    }

    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void processScheduled() {
        jobExecutionTemplate.run("email-outbox", this::processPending);
    }

    protected void processPending() {
        List<EmailOutbox> messages = emailOutboxRepository
                .findTop20ByStatusAndNextAttemptAtLessThanEqualOrderByIdAsc(
                        EmailStatus.PENDING, LocalDateTime.now());
        for (EmailOutbox message : messages) {
            processOne(message);
        }
    }

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
                helper.setFrom(mailUsername);
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

    private String truncateError(String message) {
        String value = message == null || message.isBlank() ? "Lỗi không xác định" : message;
        return value.length() <= 500 ? value : value.substring(0, 500);
    }
}
