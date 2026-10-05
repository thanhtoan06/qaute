package vn.edu.hcmute.qaute.service.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import vn.edu.hcmute.qaute.common.constant.EmailStatus;
import vn.edu.hcmute.qaute.entity.notification.EmailOutbox;
import vn.edu.hcmute.qaute.repository.notification.EmailOutboxRepository;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailOutboxRepository emailOutboxRepository;
    private final ObjectMapper objectMapper;

    public EmailServiceImpl(EmailOutboxRepository emailOutboxRepository, ObjectMapper objectMapper) {
        this.emailOutboxRepository = emailOutboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void sendTemplated(String toEmail, String subject, String templateName,
                              Map<String, String> model) {
        final String payload;
        try {
            payload = objectMapper.writeValueAsString(model == null ? Map.of() : model);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Không thể tạo dữ liệu email", exception);
        }
        emailOutboxRepository.save(EmailOutbox.builder()
                .toEmail(toEmail)
                .subject(subject)
                .template(templateName)
                .payloadJson(payload)
                .status(EmailStatus.PENDING)
                .attempts(0)
                .nextAttemptAt(LocalDateTime.now())
                .build());
    }
}
