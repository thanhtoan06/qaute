-- Bước T2-06: cấu hình SLA. Chính sách theo chuyên mục x mức ưu tiên, giờ làm việc, ngày nghỉ.
-- sla_policies.category_id NULL = mặc định toàn hệ thống; giờ tính theo cột DATETIME(6) của ticket.

CREATE TABLE sla_policies (
    id                      BIGINT      NOT NULL AUTO_INCREMENT,
    category_id             BIGINT      NULL,
    priority                VARCHAR(10) NOT NULL,
    first_response_minutes  INT         NOT NULL,
    resolution_minutes      INT         NOT NULL,
    active                  TINYINT(1)  NOT NULL DEFAULT 1,
    created_at              DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at              DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_sla_policies_category_priority (category_id, priority),
    CONSTRAINT fk_sla_policies_category FOREIGN KEY (category_id) REFERENCES categories (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE business_hours (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    day_of_week TINYINT     NOT NULL COMMENT '1=Thứ Hai … 7=Chủ nhật (java.time.DayOfWeek)',
    start_time  TIME        NOT NULL,
    end_time    TIME        NOT NULL,
    active      TINYINT(1)  NOT NULL DEFAULT 1,
    created_at  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    KEY idx_business_hours_day (day_of_week)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE holidays (
    id           BIGINT        NOT NULL AUTO_INCREMENT,
    holiday_date DATE          NOT NULL,
    name         VARCHAR(150)  NOT NULL,
    created_at   DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at   DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_holidays_date (holiday_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Chính sách mặc định toàn hệ thống (category_id NULL), đơn vị phút làm việc.
INSERT INTO sla_policies (category_id, priority, first_response_minutes, resolution_minutes, active) VALUES
    (NULL, 'LOW',     960, 5760, 1),
    (NULL, 'NORMAL',  480, 2880, 1),
    (NULL, 'HIGH',    240, 1440, 1),
    (NULL, 'URGENT',  120,  480, 1);

-- Giờ làm việc mặc định: Thứ Hai–Thứ Sáu, sáng 07:30–11:30, chiều 13:00–17:00.
INSERT INTO business_hours (day_of_week, start_time, end_time, active) VALUES
    (1, '07:30:00', '11:30:00', 1),
    (1, '13:00:00', '17:00:00', 1),
    (2, '07:30:00', '11:30:00', 1),
    (2, '13:00:00', '17:00:00', 1),
    (3, '07:30:00', '11:30:00', 1),
    (3, '13:00:00', '17:00:00', 1),
    (4, '07:30:00', '11:30:00', 1),
    (4, '13:00:00', '17:00:00', 1),
    (5, '07:30:00', '11:30:00', 1),
    (5, '13:00:00', '17:00:00', 1);
