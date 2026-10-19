package vn.edu.hcmute.qaute.entity.manager;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

@Entity @Table(name = "ticket_tag_map", uniqueConstraints =
        @UniqueConstraint(name = "uk_ticket_tag_map_ticket_tag", columnNames = {"ticket_id", "tag_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TicketTagMap extends AuditableEntity {
    @Column(name = "ticket_id", nullable = false) private Long ticketId;
    @Column(name = "tag_id", nullable = false) private Long tagId;
}
