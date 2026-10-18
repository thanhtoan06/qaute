package vn.edu.hcmute.qaute.entity.ticket;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

@Entity @Table(name = "ticket_escalations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TicketEscalation extends AuditableEntity {
    @Column(name = "ticket_id", nullable = false) private Long ticketId;
    @Column(name = "from_manager_id") private Long fromManagerId;
    @Column(name = "from_category_id") private Long fromCategoryId;
    @Column(name = "to_category_id") private Long toCategoryId;
    @Column(name = "to_manager_id") private Long toManagerId;
    @Column(nullable = false, length = 500) private String reason;
}
