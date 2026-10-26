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

// Biểu diễn dữ liệu TicketReview được lưu trữ trong hệ thống.
@Entity @Table(name = "ticket_reviews")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TicketReview extends AuditableEntity {
    @Column(name = "ticket_id", nullable = false, unique = true) private Long ticketId;
    @Column(name = "student_id", nullable = false) private Long studentId;
    @Column(nullable = false) private int rating;
    @Column(name = "reason_codes", length = 255) private String reasonCodes;
    @Column(length = 500) private String comment;
}
