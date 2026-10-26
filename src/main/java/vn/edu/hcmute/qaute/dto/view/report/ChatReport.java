package vn.edu.hcmute.qaute.dto.view.report;

import java.util.List;

// Đóng gói dữ liệu ChatReport để hiển thị hoặc trả về API.
public record ChatReport(
        long total,
        double avgWaitSeconds,
        double avgDurationMinutes,
        double abandonedPercent,
        List<NamedCount> perManager) {}
