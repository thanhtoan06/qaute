package vn.edu.hcmute.qaute.entity.manager;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

@Entity @Table(name = "ticket_tags")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TicketTag extends AuditableEntity {
    @Column(nullable = false, unique = true, length = 40) private String name;
}
