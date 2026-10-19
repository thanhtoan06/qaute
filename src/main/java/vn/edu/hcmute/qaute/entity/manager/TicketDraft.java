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

@Entity @Table(name = "ticket_drafts", uniqueConstraints =
        @UniqueConstraint(name = "uk_ticket_drafts_ticket_manager", columnNames = {"ticket_id", "manager_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TicketDraft extends AuditableEntity {
    @Column(name = "ticket_id", nullable = false) private Long ticketId;
    @Column(name = "manager_id", nullable = false) private Long managerId;
    @Column(name = "content_html", nullable = false, columnDefinition = "MEDIUMTEXT") private String contentHtml;
}
