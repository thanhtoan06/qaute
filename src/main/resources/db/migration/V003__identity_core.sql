CREATE TABLE roles (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    code       VARCHAR(20) NOT NULL,
    name       VARCHAR(50) NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_roles_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO roles (code, name)
VALUES
    ('STUDENT', 'Sinh viên'),
    ('MANAGER', 'Tư vấn viên'),
    ('ADMIN', 'Quản trị viên');

CREATE TABLE faculties (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    code       VARCHAR(30)  NULL,
    name       VARCHAR(150) NOT NULL,
    active     TINYINT(1)   NOT NULL DEFAULT 1,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_faculties_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO faculties (code, name)
VALUES
    ('CNTT', 'Công nghệ Thông tin'),
    ('CK', 'Cơ khí Chế tạo máy'),
    ('DDT', 'Điện – Điện tử'),
    ('XD', 'Xây dựng'),
    ('KT', 'Kinh tế'),
    ('NN', 'Ngoại ngữ'),
    ('CNHH', 'Công nghệ Hóa học và Thực phẩm'),
    ('IN', 'In và Truyền thông'),
    ('TT', 'Thời trang và Du lịch'),
    ('KHUD', 'Khoa học Ứng dụng');

CREATE TABLE users (
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    email               VARCHAR(150) NOT NULL,
    mssv                VARCHAR(20)  NULL,
    password_hash       VARCHAR(100) NULL,
    full_name           VARCHAR(100) NOT NULL,
    phone               VARCHAR(20)  NULL,
    avatar_asset_id     BIGINT       NULL,
    role_id             BIGINT       NOT NULL,
    status              VARCHAR(25)  NOT NULL,
    failed_login_count  INT          NOT NULL DEFAULT 0,
    locked_until        DATETIME(6)  NULL,
    email_verified_at   DATETIME(6)  NULL,
    last_login_at       DATETIME(6)  NULL,
    password_changed_at DATETIME(6)  NULL,
    created_by          BIGINT       NULL,
    created_at          DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_email (email),
    UNIQUE KEY uk_users_mssv (mssv),
    KEY idx_users_role_status (role_id, status),
    KEY idx_users_status_created (status, created_at),
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT fk_users_avatar_asset FOREIGN KEY (avatar_asset_id) REFERENCES media_assets (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE student_profiles (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    user_id    BIGINT      NOT NULL,
    faculty_id BIGINT      NULL,
    class_code VARCHAR(30) NULL,
    cohort     VARCHAR(10) NULL,
    program    VARCHAR(100) NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_student_profiles_user (user_id),
    CONSTRAINT fk_student_profiles_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_student_profiles_faculty FOREIGN KEY (faculty_id) REFERENCES faculties (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
