package vn.edu.hcmute.qaute.entity.governance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

/**
 * Một dòng nhật ký hệ thống (bảng audit_logs). Chỉ thêm mới, không sửa.
 * actorId và entityId chỉ lưu id, không dùng @ManyToOne để nhật ký không phụ thuộc dữ liệu gốc.
 */
@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog extends AuditableEntity {

    /** Người thực hiện; null khi hành động do hệ thống hoặc người chưa đăng nhập. */
    @Column(name = "actor_id")
    private Long actorId;

    /** STUDENT / MANAGER / ADMIN; null khi không có người thực hiện. */
    @Column(name = "actor_role", length = 10)
    private String actorRole;

    /** Mã hành động, xem AuditActions. */
    @Column(name = "action", nullable = false, length = 60)
    private String action;

    @Column(name = "entity_type", length = 40)
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "summary_before", length = 1000)
    private String summaryBefore;

    @Column(name = "summary_after", length = 1000)
    private String summaryAfter;

    @Column(name = "ip", length = 64)
    private String ip;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    @Column(name = "trace_id", length = 16)
    private String traceId;
}
