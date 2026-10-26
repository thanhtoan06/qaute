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

// Biểu diễn dữ liệu TicketInternalNote được lưu trữ trong hệ thống.
@Entity @Table(name = "ticket_internal_notes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TicketInternalNote extends AuditableEntity {
    @Column(name = "ticket_id", nullable = false) private Long ticketId;
    @Column(name = "author_id", nullable = false) private Long authorId;
    @Column(name = "content_html", nullable = false, columnDefinition = "MEDIUMTEXT") private String contentHtml;
    @Column(nullable = false) private boolean pinned;
}
