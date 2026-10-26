package vn.edu.hcmute.qaute.dto.view.report;

import java.time.LocalDate;

// Đóng gói dữ liệu ReportRange để hiển thị hoặc trả về API.
public record ReportRange(LocalDate from, LocalDate to) {}
