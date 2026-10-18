package vn.edu.hcmute.qaute.entity.ticket;

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
import vn.edu.hcmute.qaute.common.constant.AssignmentType;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

@Entity @Table(name = "ticket_assignments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TicketAssignment extends AuditableEntity {
    @Column(name = "ticket_id", nullable = false) private Long ticketId;
    @Column(name = "from_manager_id") private Long fromManagerId;
    @Column(name = "to_manager_id") private Long toManagerId;
    @Column(name = "assigned_by") private Long assignedBy;
    @Enumerated(EnumType.STRING) @Column(name = "type", nullable = false, length = 12) private AssignmentType type;
    @Column(length = 500) private String reason;
    @Column(name = "assigned_at", nullable = false) private LocalDateTime assignedAt;
    @Column(name = "ended_at") private LocalDateTime endedAt;
}
