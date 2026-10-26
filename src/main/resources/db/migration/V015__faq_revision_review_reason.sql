-- Mở rộng ghi chú thay đổi để lưu lý do duyệt FAQ dài hơn.
ALTER TABLE faq_revisions MODIFY change_note VARCHAR(500) NULL;
