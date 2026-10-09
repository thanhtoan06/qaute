package vn.edu.hcmute.qaute.entity.governance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

/**
 * Một khoảng giờ làm việc trong tuần (bảng business_hours).
 * Một ngày có thể có nhiều khoảng (ví dụ sáng và chiều).
 */
@Entity
@Table(name = "business_hours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessHour extends AuditableEntity {

    /** 1 = Thứ Hai … 7 = Chủ nhật, theo java.time.DayOfWeek. */
    @Column(name = "day_of_week", nullable = false, columnDefinition = "TINYINT")
    private int dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;
}
