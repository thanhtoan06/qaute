package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;

public record SlaSnapshot(int firstResponseMinutes, int resolutionMinutes,
                          LocalDateTime firstResponseDueAt, LocalDateTime resolutionDueAt) {
}
