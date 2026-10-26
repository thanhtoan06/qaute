-- Cấp dãy số riêng theo năm để sinh mã ticket.
CREATE TABLE ticket_sequences (
    year INT NOT NULL,
    last_sequence_value INT NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Lưu ticket, người xử lý, trạng thái và các mốc thời gian SLA.
CREATE TABLE tickets (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(20) NOT NULL,
    student_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description_html MEDIUMTEXT NOT NULL,
    description_text MEDIUMTEXT NOT NULL,
    priority VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL,
    urgent_requested TINYINT(1) NOT NULL DEFAULT 0,
    urgent_reason VARCHAR(500) NULL,
    allow_public_faq TINYINT(1) NOT NULL DEFAULT 0,
    current_assignee_id BIGINT NULL,
    first_assigned_at DATETIME(6) NULL,
    first_response_at DATETIME(6) NULL,
    resolved_at DATETIME(6) NULL,
    closed_at DATETIME(6) NULL,
    close_reason VARCHAR(255) NULL,
    resolution_note VARCHAR(2000) NULL,
    reopen_count INT NOT NULL DEFAULT 0,
    waiting_reminder_count INT NOT NULL DEFAULT 0,
    sla_first_response_minutes INT NULL,
    sla_resolution_minutes INT NULL,
    sla_first_response_due_at DATETIME(6) NULL,
    sla_resolution_due_at DATETIME(6) NULL,
    sla_paused_at DATETIME(6) NULL,
    sla_paused_seconds BIGINT NOT NULL DEFAULT 0,
    sla_status VARCHAR(10) NULL,
    last_activity_at DATETIME(6) NOT NULL,
    submit_token VARCHAR(64) NULL,
    version BIGINT NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_tickets_code (code),
    UNIQUE KEY uk_tickets_submit_token (submit_token),
    KEY idx_tickets_work_queue (status, category_id, current_assignee_id, created_at),
    KEY idx_tickets_student_status (student_id, status),
    CONSTRAINT fk_tickets_student FOREIGN KEY (student_id) REFERENCES users (id),
    CONSTRAINT fk_tickets_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_tickets_assignee FOREIGN KEY (current_assignee_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Lưu nội dung trao đổi giữa người gửi và ticket.
CREATE TABLE ticket_messages (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ticket_id BIGINT NOT NULL,
    sender_id BIGINT NULL,
    sender_type VARCHAR(10) NOT NULL,
    content_html MEDIUMTEXT NOT NULL,
    content_text MEDIUMTEXT NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    KEY idx_ticket_messages_ticket_created (ticket_id, created_at),
    CONSTRAINT fk_ticket_messages_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Liên kết tệp đính kèm với ticket và tin nhắn tương ứng.
CREATE TABLE ticket_attachments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ticket_id BIGINT NOT NULL,
    message_id BIGINT NULL,
    asset_id BIGINT NOT NULL,
    uploader_id BIGINT NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_ticket_attachments_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id) ON DELETE CASCADE,
    CONSTRAINT fk_ticket_attachments_asset FOREIGN KEY (asset_id) REFERENCES media_assets (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Ghi lại lịch sử chuyển trạng thái và tác nhân thực hiện.
CREATE TABLE ticket_status_history (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ticket_id BIGINT NOT NULL,
    from_status VARCHAR(20) NULL,
    to_status VARCHAR(20) NOT NULL,
    actor_id BIGINT NULL,
    actor_type VARCHAR(10) NOT NULL,
    reason VARCHAR(500) NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    KEY idx_ticket_status_history_ticket_created (ticket_id, created_at),
    CONSTRAINT fk_ticket_status_history_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Theo dõi các lần phân công và thời gian phụ trách ticket.
CREATE TABLE ticket_assignments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ticket_id BIGINT NOT NULL,
    from_manager_id BIGINT NULL,
    to_manager_id BIGINT NULL,
    assigned_by BIGINT NULL,
    type VARCHAR(12) NOT NULL,
    reason VARCHAR(500) NULL,
    assigned_at DATETIME(6) NOT NULL,
    ended_at DATETIME(6) NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_ticket_assignments_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Lưu thông tin chuyển cấp hoặc chuyển chuyên mục của ticket.
CREATE TABLE ticket_escalations (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ticket_id BIGINT NOT NULL,
    from_manager_id BIGINT NULL,
    from_category_id BIGINT NULL,
    to_category_id BIGINT NULL,
    to_manager_id BIGINT NULL,
    reason VARCHAR(500) NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_ticket_escalations_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Lưu đánh giá của sinh viên cho ticket đã xử lý.
CREATE TABLE ticket_reviews (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ticket_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    rating INT NOT NULL,
    reason_codes VARCHAR(255) NULL,
    comment VARCHAR(500) NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_ticket_reviews_ticket (ticket_id),
    CONSTRAINT fk_ticket_reviews_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
