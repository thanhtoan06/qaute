-- Bước T2-05: nhật ký hệ thống (audit). Chỉ thêm bản ghi, không sửa; dọn theo retention.audit.days.
-- Không đặt khóa ngoại cho actor_id để việc ghi audit không bao giờ bị chặn bởi dữ liệu người dùng.

CREATE TABLE audit_logs (
    id             BIGINT        NOT NULL AUTO_INCREMENT,
    actor_id       BIGINT        NULL,
    actor_role     VARCHAR(10)   NULL,
    action         VARCHAR(60)   NOT NULL,
    entity_type    VARCHAR(40)   NULL,
    entity_id      BIGINT        NULL,
    summary_before VARCHAR(1000) NULL,
    summary_after  VARCHAR(1000) NULL,
    ip             VARCHAR(64)   NULL,
    user_agent     VARCHAR(255)  NULL,
    trace_id       VARCHAR(16)   NULL,
    created_at     DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at     DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    KEY idx_audit_logs_actor_created (actor_id, created_at),
    KEY idx_audit_logs_action_created (action, created_at),
    KEY idx_audit_logs_entity (entity_type, entity_id),
    KEY idx_audit_logs_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
