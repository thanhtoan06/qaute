package vn.edu.hcmute.qaute.entity.ticket;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity @Table(name = "ticket_sequences")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TicketSequence {
    @Id private Integer year;
    @Column(name = "last_value", nullable = false) private Integer lastValue;
    @CreationTimestamp @Column(name = "created_at", updatable = false, nullable = false) private LocalDateTime createdAt;
    @UpdateTimestamp @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;

    public TicketSequence(Integer year, Integer lastValue) {
        this.year = year;
        this.lastValue = lastValue;
    }
}
