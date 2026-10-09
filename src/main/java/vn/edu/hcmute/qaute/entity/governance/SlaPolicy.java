package vn.edu.hcmute.qaute.entity.governance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

/**
 * Chính sách SLA theo chuyên mục và mức ưu tiên (bảng sla_policies).
 * categoryId = null là chính sách mặc định toàn hệ thống; thời hạn tính bằng phút làm việc.
 */
@Entity
@Table(name = "sla_policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlaPolicy extends AuditableEntity {

    /** Chuyên mục áp dụng; null nghĩa là mặc định toàn hệ thống. */
    @Column(name = "category_id")
    private Long categoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 10)
    private TicketPriority priority;

    /** Hạn phản hồi đầu tiên, tính bằng phút làm việc. */
    @Column(name = "first_response_minutes", nullable = false)
    private int firstResponseMinutes;

    /** Hạn giải quyết, tính bằng phút làm việc. */
    @Column(name = "resolution_minutes", nullable = false)
    private int resolutionMinutes;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;
}
