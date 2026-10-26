package vn.edu.hcmute.qaute.entity.appointment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import vn.edu.hcmute.qaute.common.constant.Modality;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

// Biểu diễn dữ liệu AvailabilityRule được lưu trữ trong hệ thống.
@Entity
@Table(name = "availability_rules")
@Getter
@Setter
public class AvailabilityRule extends AuditableEntity {

    @Column(name = "manager_id", nullable = false)
    private Long managerId;

    @JdbcTypeCode(SqlTypes.TINYINT)
    @Column(name = "day_of_week", nullable = false)
    private int dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "slot_minutes", nullable = false)
    private int slotMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Modality modality;

    @Column(length = 200)
    private String location;

    @Column(name = "meeting_link", length = 300)
    private String meetingLink;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(nullable = false)
    private boolean active = true;
}
