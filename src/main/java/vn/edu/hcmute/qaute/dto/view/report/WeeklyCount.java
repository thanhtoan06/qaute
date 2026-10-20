package vn.edu.hcmute.qaute.dto.view.report;

import java.time.LocalDate;

public record WeeklyCount(LocalDate weekStart, long count) {}
