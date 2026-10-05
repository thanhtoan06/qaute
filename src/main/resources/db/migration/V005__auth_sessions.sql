CREATE TABLE auth_sessions (
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    user_id            BIGINT       NOT NULL,
    jti                VARCHAR(64)  NOT NULL,
    refresh_token_hash VARCHAR(64)  NOT NULL,
    device_info        VARCHAR(255) NULL,
    ip                 VARCHAR(64)  NULL,
    last_used_at       DATETIME(6)  NULL,
    expires_at         DATETIME(6)  NOT NULL,
    remember           TINYINT(1)   NOT NULL DEFAULT 0,
    revoked_at         DATETIME(6)  NULL,
    revoked_reason     VARCHAR(100) NULL,
    replaced_by_id     BIGINT       NULL,
    created_at         DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at         DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_auth_sessions_jti (jti),
    UNIQUE KEY uk_auth_sessions_refresh_hash (refresh_token_hash),
    KEY idx_auth_sessions_user_revoked (user_id, revoked_at),
    CONSTRAINT fk_auth_sessions_user FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE login_history (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    user_id          BIGINT       NULL,
    identifier_masked VARCHAR(150) NOT NULL,
    success          TINYINT(1)   NOT NULL,
    ip               VARCHAR(64)  NULL,
    user_agent       VARCHAR(255) NULL,
    created_at       DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at       DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    KEY idx_login_history_user_created (user_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
