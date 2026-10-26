package vn.edu.hcmute.qaute.dto.view.report;

import java.util.Set;

// Đóng gói dữ liệu ReportScope để hiển thị hoặc trả về API.
public record ReportScope(Long managerId, Set<Long> categoryIds) {}
