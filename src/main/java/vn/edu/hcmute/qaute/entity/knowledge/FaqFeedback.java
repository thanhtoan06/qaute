package vn.edu.hcmute.qaute.entity.knowledge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

@Entity
@Table(name = "faq_feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqFeedback extends AuditableEntity {

    @Column(name = "faq_id", nullable = false)
    private Long faqId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private boolean helpful;

    @Column(name = "reason_code", length = 30)
    private String reasonCode;

    @Column(length = 500)
    private String comment;
}
