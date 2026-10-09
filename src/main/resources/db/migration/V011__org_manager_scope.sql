-- Bước T2-07: phạm vi của Manager. Hồ sơ tư vấn viên và bảng gán chuyên mục phụ trách.
-- Quyền truy cập ticket của Manager = chuyên mục được giao (tính cả cha/con) hoặc ticket được giao đích danh.

CREATE TABLE manager_profiles (
    id                     BIGINT       NOT NULL AUTO_INCREMENT,
    user_id                BIGINT       NOT NULL,
    employee_code          VARCHAR(30)  NULL,
    job_title              VARCHAR(100) NULL,
    department_id          BIGINT       NULL,
    bio                    VARCHAR(500) NULL,
    max_concurrent_tickets INT          NOT NULL DEFAULT 10,
    max_concurrent_chats   INT          NOT NULL DEFAULT 3,
    availability_status    VARCHAR(10)  NOT NULL DEFAULT 'OFFLINE',
    last_seen_at           DATETIME(6)  NULL,
    created_at             DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at             DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_manager_profiles_user (user_id),
    CONSTRAINT fk_manager_profiles_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_manager_profiles_department FOREIGN KEY (department_id) REFERENCES departments (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE manager_categories (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    manager_id  BIGINT      NOT NULL,
    category_id BIGINT      NOT NULL,
    level       VARCHAR(10) NOT NULL,
    created_at  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_manager_categories_manager_category (manager_id, category_id),
    CONSTRAINT fk_manager_categories_manager FOREIGN KEY (manager_id) REFERENCES users (id),
    CONSTRAINT fk_manager_categories_category FOREIGN KEY (category_id) REFERENCES categories (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
