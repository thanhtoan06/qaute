package vn.edu.hcmute.qaute.dto.view.report;

import java.time.LocalDate;

// Đóng gói dữ liệu DailyCount để hiển thị hoặc trả về API.
public record DailyCount(LocalDate day, long count) {}
