package vn.edu.hcmute.qaute.entity.appointment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.common.constant.Modality;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

@Entity
@Table(name = "appointments")
@Getter @Setter
public class Appointment extends AuditableEntity {
    @Column(name = "slot_id", nullable = false) private Long slotId;
    @Column(name = "student_id", nullable = false) private Long studentId;
    @Column(name = "manager_id", nullable = false) private Long managerId;
    @Column(name = "category_id") private Long categoryId;
    @Column(name = "ticket_id") private Long ticketId;
    @Column(nullable = false, length = 200) private String topic;
    @Column(length = 500) private String note;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 10) private Modality modality;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 12) private AppointmentStatus status;
    @Column(name = "cancelled_by") private Long cancelledBy;
    @Column(name = "cancel_reason", length = 500) private String cancelReason;
    @Column(name = "outcome_summary", length = 2000) private String outcomeSummary;
    @Column(name = "student_visible_summary", length = 1000) private String studentVisibleSummary;
    @Column(name = "reminder_24h_sent", nullable = false) private boolean reminder24hSent;
    @Column(name = "reminder_1h_sent", nullable = false) private boolean reminder1hSent;
    @Column(name = "active_slot_id", insertable = false, updatable = false) private Long activeSlotId;
    @Version @Column(nullable = false) private Long version;
}
