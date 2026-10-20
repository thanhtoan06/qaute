package vn.edu.hcmute.qaute.dto.view;

import java.util.List;

public record ManagerScheduleRow(int hour, List<List<ManagerScheduleSlotView>> cells) {
}
