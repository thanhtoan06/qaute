package vn.edu.hcmute.qaute.dto.view.report;

import java.util.List;
public record ChatReport(long total, double avgWaitSeconds, double avgDurationMinutes, double abandonedPercent, List<NamedCount> perManager) {}
