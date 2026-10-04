-- Bước T2-01: dữ liệu tổ chức. Phòng ban (departments) và chuyên mục (categories, tối đa 2 cấp).
-- Cây: Phòng ban → Chuyên mục (depth = 1) → Chủ đề (depth = 2).

CREATE TABLE departments (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    code        VARCHAR(30)  NOT NULL,
    name        VARCHAR(150) NOT NULL,
    description VARCHAR(500) NULL,
    email       VARCHAR(150) NULL,
    phone       VARCHAR(30)  NULL,
    location    VARCHAR(200) NULL,
    sort_order  INT          NOT NULL DEFAULT 0,
    active      TINYINT(1)   NOT NULL DEFAULT 1,
    created_at  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_departments_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE categories (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    department_id BIGINT       NOT NULL,
    parent_id     BIGINT       NULL,
    depth         TINYINT      NOT NULL,
    code          VARCHAR(40)  NOT NULL,
    name          VARCHAR(150) NOT NULL,
    description   VARCHAR(500) NULL,
    icon          VARCHAR(40)  NULL,
    sort_order    INT          NOT NULL DEFAULT 0,
    active        TINYINT(1)   NOT NULL DEFAULT 1,
    created_at    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_categories_code (code),
    KEY idx_categories_dept_parent_active (department_id, parent_id, active),
    CONSTRAINT fk_categories_department FOREIGN KEY (department_id) REFERENCES departments (id),
    CONSTRAINT fk_categories_parent FOREIGN KEY (parent_id) REFERENCES categories (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
