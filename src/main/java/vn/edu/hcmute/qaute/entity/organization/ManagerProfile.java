package vn.edu.hcmute.qaute.entity.organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

/**
 * Hồ sơ tư vấn viên của một tài khoản MANAGER (bảng manager_profiles).
 * Khóa ngoại sang miền khác chỉ lưu bằng Long id, không dùng @ManyToOne.
 */
@Entity
@Table(name = "manager_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerProfile extends AuditableEntity {

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "employee_code", length = 30)
    private String employeeCode;

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "bio", length = 500)
    private String bio;

    /** Số ticket được phụ trách đồng thời tối đa. */
    @Builder.Default
    @Column(name = "max_concurrent_tickets", nullable = false)
    private int maxConcurrentTickets = 10;

    /** Số phiên chat phụ trách đồng thời tối đa. */
    @Builder.Default
    @Column(name = "max_concurrent_chats", nullable = false)
    private int maxConcurrentChats = 3;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "availability_status", nullable = false, length = 10)
    private AvailabilityStatus availabilityStatus = AvailabilityStatus.OFFLINE;

    /** Lần cuối có hoạt động; dùng để tự chuyển OFFLINE khi mất kết nối quá 5 phút. */
    @Column(name = "last_seen_at")
    private LocalDateTime lastSeenAt;
}
