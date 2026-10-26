package vn.edu.hcmute.qaute.entity.ticket;

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
import vn.edu.hcmute.qaute.common.constant.ActorType;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

// Biểu diễn dữ liệu TicketStatusHistory được lưu trữ trong hệ thống.
@Entity @Table(name = "ticket_status_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TicketStatusHistory extends AuditableEntity {
    @Column(name = "ticket_id", nullable = false) private Long ticketId;
    @Enumerated(EnumType.STRING) @Column(name = "from_status", length = 20) private TicketStatus fromStatus;
    @Enumerated(EnumType.STRING) @Column(name = "to_status", nullable = false, length = 20) private TicketStatus toStatus;
    @Column(name = "actor_id") private Long actorId;
    @Enumerated(EnumType.STRING) @Column(name = "actor_type", nullable = false, length = 10) private ActorType actorType;
    @Column(length = 500) private String reason;
}
