package vn.edu.hcmute.qaute.dto.view.report;

import java.util.Set;
public record ReportScope(Long managerId, Set<Long> categoryIds) {}
