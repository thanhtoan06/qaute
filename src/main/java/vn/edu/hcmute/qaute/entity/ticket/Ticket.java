package vn.edu.hcmute.qaute.entity.ticket;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.SlaStatus;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

// Biểu diễn dữ liệu Ticket được lưu trữ trong hệ thống.
@Entity
@Table(name = "tickets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Ticket extends AuditableEntity {
    @Column(nullable = false, unique = true, length = 20) private String code;
    @Column(name = "student_id", nullable = false) private Long studentId;
    @Column(name = "category_id", nullable = false) private Long categoryId;
    @Column(nullable = false, length = 150) private String title;
    @Column(name = "description_html", nullable = false, columnDefinition = "MEDIUMTEXT") private String descriptionHtml;
    @Column(name = "description_text", nullable = false, columnDefinition = "MEDIUMTEXT") private String descriptionText;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 10) private TicketPriority priority;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private TicketStatus status;
    @Column(name = "urgent_requested", nullable = false) private boolean urgentRequested;
    @Column(name = "urgent_reason", length = 500) private String urgentReason;
    @Column(name = "allow_public_faq", nullable = false) private boolean allowPublicFaq;
    @Column(name = "current_assignee_id") private Long currentAssigneeId;
    @Column(name = "first_assigned_at") private LocalDateTime firstAssignedAt;
    @Column(name = "first_response_at") private LocalDateTime firstResponseAt;
    @Column(name = "resolved_at") private LocalDateTime resolvedAt;
    @Column(name = "closed_at") private LocalDateTime closedAt;
    @Column(name = "close_reason", length = 255) private String closeReason;
    @Column(name = "resolution_note", length = 2000) private String resolutionNote;
    @Column(name = "reopen_count", nullable = false) private int reopenCount;
    @Column(name = "waiting_reminder_count", nullable = false) private int waitingReminderCount;
    @Column(name = "sla_first_response_minutes") private Integer slaFirstResponseMinutes;
    @Column(name = "sla_resolution_minutes") private Integer slaResolutionMinutes;
    @Column(name = "sla_first_response_due_at") private LocalDateTime slaFirstResponseDueAt;
    @Column(name = "sla_resolution_due_at") private LocalDateTime slaResolutionDueAt;
    @Column(name = "sla_paused_at") private LocalDateTime slaPausedAt;
    @Column(name = "sla_paused_seconds", nullable = false) private long slaPausedSeconds;
    @Enumerated(EnumType.STRING) @Column(name = "sla_status", length = 10) private SlaStatus slaStatus;
    @Column(name = "last_activity_at", nullable = false) private LocalDateTime lastActivityAt;
    @Column(name = "submit_token", unique = true, length = 64) private String submitToken;
    @Version @Column(nullable = false) private Long version;
}
