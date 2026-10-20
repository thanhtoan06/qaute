package vn.edu.hcmute.qaute.dto.view.report;

import java.util.List;
public record CsatStats(long count, double avg, double satisfiedPercent, long[] distribution, List<NamedCount> topReasons) {}
