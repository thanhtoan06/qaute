package vn.edu.hcmute.qaute.dto.view;

import vn.edu.hcmute.qaute.common.constant.NotificationType;

public record PreferenceView(NotificationType type, String label, boolean inApp, boolean email,
                             boolean emailLocked) {
}
