-- Lưu các câu hỏi thường gặp hiển thị trên trang chủ dành cho khách (guest)
CREATE TABLE guest_home_faqs (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    category_name     VARCHAR(100) NOT NULL COMMENT 'Tên danh mục hiển thị (Học vụ, Học phí, v.v.)',
    question          VARCHAR(300) NOT NULL COMMENT 'Câu hỏi',
    answer            TEXT         NOT NULL COMMENT 'Câu trả lời (có thể có HTML)',
    display_order     INT          NOT NULL DEFAULT 0 COMMENT 'Thứ tự hiển thị',
    is_active         TINYINT(1)   NOT NULL DEFAULT 1 COMMENT 'Có hiển thị hay không',
    created_at        DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at        DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    INDEX idx_guest_home_faqs_order (display_order),
    INDEX idx_guest_home_faqs_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Câu hỏi thường gặp trên trang chủ guest';

UPDATE system_settings
SET setting_value = '30',
    max_value = 30,
    description = 'Số yêu cầu khẩn cấp tối đa đang mở'
WHERE setting_key = 'ticket.urgent.request.limit';
