-- Lưu các phiên bản nội dung FAQ để theo dõi lịch sử chỉnh sửa.
CREATE TABLE faq_revisions (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    faq_id      BIGINT       NOT NULL,
    version_no  INT          NOT NULL,
    title       VARCHAR(200) NOT NULL,
    summary     VARCHAR(500) NULL,
    body_html   MEDIUMTEXT   NOT NULL,
    changed_by  BIGINT       NULL,
    change_note VARCHAR(255) NULL,
    created_at  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_faq_revisions_faq_version (faq_id, version_no),
    CONSTRAINT fk_faq_revisions_faq FOREIGN KEY (faq_id) REFERENCES faqs (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
