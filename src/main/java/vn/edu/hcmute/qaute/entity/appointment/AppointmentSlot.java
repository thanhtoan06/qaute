package vn.edu.hcmute.qaute.entity.appointment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.Modality;
import vn.edu.hcmute.qaute.common.constant.SlotStatus;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

// Biểu diễn dữ liệu AppointmentSlot được lưu trữ trong hệ thống.
@Entity
@Table(
        name = "appointment_slots",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_appointment_slots_manager_start",
                columnNames = {"manager_id", "start_at"}))
@Getter
@Setter
public class AppointmentSlot extends AuditableEntity {

    @Column(name = "manager_id", nullable = false)
    private Long managerId;

    @Column(name = "availability_rule_id")
    private Long availabilityRuleId;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Modality modality;

    @Column(length = 200)
    private String location;

    @Column(name = "meeting_link", length = 300)
    private String meetingLink;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SlotStatus status;

    @Version
    @Column(nullable = false)
    private Long version;
}
