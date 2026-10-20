package vn.edu.hcmute.qaute.entity.appointment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

@Entity
@Table(name = "availability_blocks")
@Getter @Setter
public class AvailabilityBlock extends AuditableEntity {
    @Column(name = "manager_id", nullable = false) private Long managerId;
    @Column(name = "start_at", nullable = false) private LocalDateTime startAt;
    @Column(name = "end_at", nullable = false) private LocalDateTime endAt;
    @Column(length = 200) private String reason;
}
