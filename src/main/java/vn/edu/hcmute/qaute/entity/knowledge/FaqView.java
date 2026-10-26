package vn.edu.hcmute.qaute.entity.knowledge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

// Biểu diễn dữ liệu FaqView được lưu trữ trong hệ thống.
@Entity
@Table(name = "faq_views")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqView extends AuditableEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "faq_id", nullable = false)
    private Long faqId;

    @Column(name = "last_viewed_at", nullable = false)
    private LocalDateTime lastViewedAt;

    @Builder.Default
    @Column(name = "view_count", nullable = false)
    private int viewCount = 1;
}
