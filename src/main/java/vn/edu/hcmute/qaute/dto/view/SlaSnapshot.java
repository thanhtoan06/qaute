package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;

// Đóng gói dữ liệu SlaSnapshot để hiển thị hoặc trả về API.
public record SlaSnapshot(int firstResponseMinutes, int resolutionMinutes,
                          LocalDateTime firstResponseDueAt, LocalDateTime resolutionDueAt) {
}
