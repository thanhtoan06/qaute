CREATE TABLE search_synonyms (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    term       VARCHAR(100) NOT NULL,
    synonym    VARCHAR(100) NOT NULL,
    active     TINYINT(1)  NOT NULL DEFAULT 1,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_search_synonyms_term_synonym (term, synonym)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE search_logs (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    user_id        BIGINT       NULL,
    query_norm     VARCHAR(200) NOT NULL,
    result_count   INT          NULL,
    clicked_faq_id BIGINT       NULL,
    source         VARCHAR(20)  NOT NULL,
    ip_hash        VARCHAR(64)  NULL,
    created_at     DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at     DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    KEY idx_search_logs_created_at (created_at),
    KEY idx_search_logs_query_norm (query_norm)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_faq_tags_name_norm ON faq_tags (name_norm);

INSERT INTO search_synonyms (term, synonym, active, created_at, updated_at) VALUES
('đăng ký môn', 'đăng ký học phần', 1, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6)),
('học phí', 'phí học tập', 1, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6)),
('ktx', 'ký túc xá', 1, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6)),
('rèn luyện', 'điểm rèn luyện', 1, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6));

