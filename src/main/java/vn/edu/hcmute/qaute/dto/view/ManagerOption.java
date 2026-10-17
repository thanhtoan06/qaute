package vn.edu.hcmute.qaute.dto.view;

import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;

/**
 * Một tư vấn viên phù hợp để giao ticket hoặc đặt lịch hẹn.
 * Thiếu ManagerProfile thì trạng thái trực coi như OFFLINE.
 */
public record ManagerOption(
        Long id,
        String fullName,
        String jobTitle,
        String avatarUrl,
        AvailabilityStatus status) {
}
