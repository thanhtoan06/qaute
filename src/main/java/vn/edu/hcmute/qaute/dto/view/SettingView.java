package vn.edu.hcmute.qaute.dto.view;

public record SettingView(
        String key,
        String value,
        String valueType,
        Long minValue,
        Long maxValue,
        String groupName,
        String description) {
}
