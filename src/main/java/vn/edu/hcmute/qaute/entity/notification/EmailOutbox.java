package vn.edu.hcmute.qaute.entity.notification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.EmailStatus;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

@Entity
@Table(name = "email_outbox")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailOutbox extends AuditableEntity {

    @Column(name = "to_email", nullable = false, length = 150)
    private String toEmail;

    @Column(name = "subject", nullable = false, length = 255)
    private String subject;

    @Column(name = "template", nullable = false, length = 60)
    private String template;

    @Column(name = "payload_json", nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private EmailStatus status;

    @Column(name = "attempts", nullable = false)
    private int attempts;

    @Column(name = "next_attempt_at", nullable = false)
    private LocalDateTime nextAttemptAt;

    @Column(name = "last_error", length = 500)
    private String lastError;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;
}
