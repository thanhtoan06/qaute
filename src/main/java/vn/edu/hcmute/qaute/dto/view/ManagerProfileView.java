package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;

// Đóng gói dữ liệu ManagerProfile để hiển thị hoặc trả về API.
public record ManagerProfileView(String fullName, String email, String phone, String avatarUrl,
        String employeeCode, String jobTitle, String departmentName, String bio,
        AvailabilityStatus availabilityStatus, LocalDateTime lastSeenAt) { }
