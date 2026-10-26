package vn.edu.hcmute.qaute.dto.view.report;

import java.util.List;

// Đóng gói dữ liệu CsatStats để hiển thị hoặc trả về API.
public record CsatStats(
        long count,
        double avg,
        double satisfiedPercent,
        long[] distribution,
        List<NamedCount> topReasons) {}
