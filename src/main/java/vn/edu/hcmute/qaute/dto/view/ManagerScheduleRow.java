package vn.edu.hcmute.qaute.dto.view;

import java.util.List;

// Đóng gói dữ liệu ManagerScheduleRow để hiển thị hoặc trả về API.
public record ManagerScheduleRow(int hour, List<List<ManagerScheduleSlotView>> cells) {
}
