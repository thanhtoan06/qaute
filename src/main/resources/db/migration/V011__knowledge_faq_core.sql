-- Lưu nhãn chuẩn hóa để phân loại và tìm kiếm bài FAQ.
CREATE TABLE faq_tags (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(60)  NOT NULL,
    name_norm   VARCHAR(60)  NOT NULL,
    slug        VARCHAR(80)  NOT NULL,
    created_at  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_faq_tags_name (name),
    UNIQUE KEY uk_faq_tags_slug (slug)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Lưu nội dung, trạng thái xuất bản và chỉ số tương tác của FAQ.
CREATE TABLE faqs (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    category_id       BIGINT       NOT NULL,
    author_id         BIGINT       NULL,
    reviewer_id       BIGINT       NULL,
    source_ticket_id  BIGINT       NULL,
    title             VARCHAR(200) NOT NULL,
    slug              VARCHAR(220) NOT NULL,
    summary           VARCHAR(500) NULL,
    body_html         MEDIUMTEXT   NOT NULL,
    body_text         MEDIUMTEXT   NOT NULL,
    title_norm        VARCHAR(255) NOT NULL,
    body_norm         MEDIUMTEXT   NOT NULL,
    status            VARCHAR(20)  NOT NULL,
    featured          TINYINT(1)   NOT NULL DEFAULT 0,
    publish_at        DATETIME(6)  NULL,
    published_at      DATETIME(6)  NULL,
    view_count        BIGINT       NOT NULL DEFAULT 0,
    helpful_count     INT          NOT NULL DEFAULT 0,
    not_helpful_count INT          NOT NULL DEFAULT 0,
    version_no        INT          NOT NULL DEFAULT 1,
    version           BIGINT       NOT NULL DEFAULT 0,
    created_at        DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at        DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_faqs_slug (slug),
    INDEX idx_faqs_status_publish (status, publish_at),
    INDEX idx_faqs_category_status (category_id, status),
    FULLTEXT KEY ft_faq_title (title_norm) WITH PARSER ngram,
    FULLTEXT KEY ft_faq_body (body_norm) WITH PARSER ngram,
    CONSTRAINT fk_faqs_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_faqs_author FOREIGN KEY (author_id) REFERENCES users (id),
    CONSTRAINT fk_faqs_reviewer FOREIGN KEY (reviewer_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Liên kết nhiều-nhiều giữa bài FAQ và các nhãn.
CREATE TABLE faq_tag_map (
    faq_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (faq_id, tag_id),
    CONSTRAINT fk_faq_tag_map_faq FOREIGN KEY (faq_id) REFERENCES faqs (id) ON DELETE CASCADE,
    CONSTRAINT fk_faq_tag_map_tag FOREIGN KEY (tag_id) REFERENCES faq_tags (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
