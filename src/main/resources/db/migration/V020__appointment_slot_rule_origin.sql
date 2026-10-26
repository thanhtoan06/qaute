-- Liên kết mỗi khung giờ với quy tắc khả dụng đã sinh ra nó.
ALTER TABLE appointment_slots
    ADD COLUMN availability_rule_id BIGINT NULL,
    ADD KEY idx_appointment_slots_rule (availability_rule_id),
    ADD CONSTRAINT fk_appointment_slots_rule
        FOREIGN KEY (availability_rule_id) REFERENCES availability_rules(id);
