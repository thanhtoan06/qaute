package vn.edu.hcmute.qaute.dto.view;

import vn.edu.hcmute.qaute.common.constant.NotificationType;

// Đóng gói dữ liệu Preference để hiển thị hoặc trả về API.
public record PreferenceView(NotificationType type, String label, boolean inApp, boolean email,
                             boolean emailLocked) {
}
