-- Lưu đánh giá hữu ích và nhận xét của người dùng cho từng FAQ.
CREATE TABLE faq_feedback (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    faq_id      BIGINT       NOT NULL,
    user_id     BIGINT       NOT NULL,
    helpful     TINYINT(1)   NOT NULL,
    reason_code VARCHAR(30)  NULL,
    comment     VARCHAR(500) NULL,
    created_at  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_faq_feedback_faq_user (faq_id, user_id),
    KEY idx_faq_feedback_user (user_id),
    CONSTRAINT fk_faq_feedback_faq FOREIGN KEY (faq_id) REFERENCES faqs (id) ON DELETE CASCADE,
    CONSTRAINT fk_faq_feedback_user FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Lưu các FAQ được người dùng đánh dấu để xem lại.
CREATE TABLE faq_bookmarks (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    user_id     BIGINT      NOT NULL,
    faq_id      BIGINT      NOT NULL,
    created_at  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_faq_bookmarks_user_faq (user_id, faq_id),
    KEY idx_faq_bookmarks_user_created (user_id, id),
    CONSTRAINT fk_faq_bookmarks_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_faq_bookmarks_faq FOREIGN KEY (faq_id) REFERENCES faqs (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Theo dõi lượt xem FAQ gần nhất và tổng số lần xem theo người dùng.
CREATE TABLE faq_views (
    id             BIGINT      NOT NULL AUTO_INCREMENT,
    user_id        BIGINT      NOT NULL,
    faq_id         BIGINT      NOT NULL,
    last_viewed_at DATETIME(6) NOT NULL,
    view_count     INT         NOT NULL DEFAULT 1,
    created_at     DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at     DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_faq_views_user_faq (user_id, faq_id),
    KEY idx_faq_views_user_last_viewed (user_id, last_viewed_at),
    CONSTRAINT fk_faq_views_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_faq_views_faq FOREIGN KEY (faq_id) REFERENCES faqs (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
