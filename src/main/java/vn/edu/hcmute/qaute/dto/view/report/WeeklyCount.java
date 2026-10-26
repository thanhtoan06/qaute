package vn.edu.hcmute.qaute.dto.view.report;

import java.time.LocalDate;

// Đóng gói dữ liệu WeeklyCount để hiển thị hoặc trả về API.
public record WeeklyCount(LocalDate weekStart, long count) {}
