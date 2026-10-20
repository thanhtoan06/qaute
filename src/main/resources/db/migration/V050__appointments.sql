CREATE TABLE availability_rules (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    manager_id BIGINT NOT NULL,
    day_of_week TINYINT NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    slot_minutes INT NOT NULL,
    modality VARCHAR(10) NOT NULL,
    location VARCHAR(200) NULL,
    meeting_link VARCHAR(300) NULL,
    valid_from DATE NOT NULL,
    valid_to DATE NULL,
    active TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    KEY idx_availability_rules_manager_active (manager_id, active),
    CONSTRAINT fk_availability_rules_manager FOREIGN KEY (manager_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE availability_blocks (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    manager_id BIGINT NOT NULL,
    start_at DATETIME(6) NOT NULL,
    end_at DATETIME(6) NOT NULL,
    reason VARCHAR(200) NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    KEY idx_availability_blocks_manager_end (manager_id, end_at),
    CONSTRAINT fk_availability_blocks_manager FOREIGN KEY (manager_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE appointment_slots (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    manager_id BIGINT NOT NULL,
    start_at DATETIME(6) NOT NULL,
    end_at DATETIME(6) NOT NULL,
    modality VARCHAR(10) NOT NULL,
    location VARCHAR(200) NULL,
    meeting_link VARCHAR(300) NULL,
    status VARCHAR(10) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    UNIQUE KEY uk_appointment_slots_manager_start (manager_id, start_at),
    KEY idx_appointment_slots_status_start (status, start_at),
    CONSTRAINT fk_appointment_slots_manager FOREIGN KEY (manager_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE appointments (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    slot_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    manager_id BIGINT NOT NULL,
    category_id BIGINT NULL,
    ticket_id BIGINT NULL,
    topic VARCHAR(200) NOT NULL,
    note VARCHAR(500) NULL,
    modality VARCHAR(10) NOT NULL,
    status VARCHAR(12) NOT NULL,
    cancelled_by BIGINT NULL,
    cancel_reason VARCHAR(500) NULL,
    outcome_summary VARCHAR(2000) NULL,
    student_visible_summary VARCHAR(1000) NULL,
    reminder_24h_sent TINYINT(1) NOT NULL DEFAULT 0,
    reminder_1h_sent TINYINT(1) NOT NULL DEFAULT 0,
    active_slot_id BIGINT GENERATED ALWAYS AS (CASE WHEN status = 'CONFIRMED' THEN slot_id ELSE NULL END) STORED,
    version BIGINT NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    UNIQUE KEY uk_appt_active_slot (active_slot_id),
    KEY idx_appointments_student_status (student_id, status),
    KEY idx_appointments_manager_status (manager_id, status),
    CONSTRAINT fk_appointments_slot FOREIGN KEY (slot_id) REFERENCES appointment_slots(id),
    CONSTRAINT fk_appointments_student FOREIGN KEY (student_id) REFERENCES users(id),
    CONSTRAINT fk_appointments_manager FOREIGN KEY (manager_id) REFERENCES users(id),
    CONSTRAINT fk_appointments_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT fk_appointments_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id),
    CONSTRAINT fk_appointments_cancelled_by FOREIGN KEY (cancelled_by) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
