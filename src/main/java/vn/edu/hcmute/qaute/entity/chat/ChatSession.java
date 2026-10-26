package vn.edu.hcmute.qaute.entity.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

// Biểu diễn dữ liệu ChatSession được lưu trữ trong hệ thống.
@Entity
@Table(name = "chat_sessions")
@Getter @Setter
public class ChatSession extends AuditableEntity {
    @Column(name = "student_id", nullable = false) private Long studentId;
    @Column(name = "manager_id") private Long managerId;
    @Column(name = "category_id", nullable = false) private Long categoryId;
    @Column(name = "ticket_id") private Long ticketId;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 10) private ChatSessionStatus status;
    @Column(name = "initial_question", nullable = false, length = 500) private String initialQuestion;
    @Column(name = "started_at", nullable = false) private LocalDateTime startedAt;
    @Column(name = "accepted_at") private LocalDateTime acceptedAt;
    @Column(name = "ended_at") private LocalDateTime endedAt;
    @Column(name = "ended_by") private Long endedBy;
    @Column(name = "end_reason", length = 30) private String endReason;
    @Column(name = "last_message_at") private LocalDateTime lastMessageAt;
    @Column private Integer rating;
    @Column(name = "rating_comment", length = 500) private String ratingComment;
    @Version @Column(nullable = false) private Long version;
}
