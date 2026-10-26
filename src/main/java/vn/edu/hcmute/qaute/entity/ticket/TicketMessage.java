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
import vn.edu.hcmute.qaute.common.constant.SenderType;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

// Biểu diễn dữ liệu TicketMessage được lưu trữ trong hệ thống.
@Entity @Table(name = "ticket_messages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TicketMessage extends AuditableEntity {
    @Column(name = "ticket_id", nullable = false) private Long ticketId;
    @Column(name = "sender_id") private Long senderId;
    @Enumerated(EnumType.STRING) @Column(name = "sender_type", nullable = false, length = 10) private SenderType senderType;
    @Column(name = "content_html", nullable = false, columnDefinition = "MEDIUMTEXT") private String contentHtml;
    @Column(name = "content_text", nullable = false, columnDefinition = "MEDIUMTEXT") private String contentText;
}
