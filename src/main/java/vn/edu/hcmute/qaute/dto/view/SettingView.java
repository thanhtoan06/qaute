package vn.edu.hcmute.qaute.dto.view;

// Đóng gói dữ liệu Setting để hiển thị hoặc trả về API.
public record SettingView(
        String key,
        String value,
        String valueType,
        Long minValue,
        Long maxValue,
        String groupName,
        String description) {
}
