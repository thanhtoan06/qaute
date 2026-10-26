-- Lưu phiên chat, người tham gia, trạng thái và thông tin kết thúc.
CREATE TABLE chat_sessions (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    manager_id BIGINT NULL,
    category_id BIGINT NOT NULL,
    ticket_id BIGINT NULL,
    status VARCHAR(10) NOT NULL,
    initial_question VARCHAR(500) NOT NULL,
    started_at DATETIME(6) NOT NULL,
    accepted_at DATETIME(6) NULL,
    ended_at DATETIME(6) NULL,
    ended_by BIGINT NULL,
    end_reason VARCHAR(30) NULL,
    last_message_at DATETIME(6) NULL,
    rating INT NULL,
    rating_comment VARCHAR(500) NULL,
    version BIGINT NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_chat_sessions_student FOREIGN KEY (student_id) REFERENCES users(id),
    CONSTRAINT fk_chat_sessions_manager FOREIGN KEY (manager_id) REFERENCES users(id),
    CONSTRAINT fk_chat_sessions_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT fk_chat_sessions_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id),
    KEY idx_chat_sessions_status_category_started (status, category_id, started_at),
    KEY idx_chat_sessions_student_status (student_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Lưu nội dung tin nhắn, loại nội dung và trạng thái đã đọc.
CREATE TABLE chat_messages (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT NOT NULL,
    sender_id BIGINT NULL,
    sender_type VARCHAR(10) NOT NULL,
    client_message_id VARCHAR(40) NULL,
    content VARCHAR(2000) NOT NULL,
    content_type VARCHAR(12) NOT NULL,
    asset_id BIGINT NULL,
    sent_at DATETIME(6) NOT NULL,
    read_at DATETIME(6) NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    UNIQUE KEY uk_chat_messages_client_id (session_id, client_message_id),
    CONSTRAINT fk_chat_messages_session FOREIGN KEY (session_id) REFERENCES chat_sessions(id),
    KEY idx_chat_messages_session_id (session_id, id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
