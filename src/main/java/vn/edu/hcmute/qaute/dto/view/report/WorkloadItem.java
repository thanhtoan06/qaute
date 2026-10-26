package vn.edu.hcmute.qaute.dto.view.report;

// Đóng gói dữ liệu WorkloadItem để hiển thị hoặc trả về API.
public record WorkloadItem(
        Long managerId,
        String managerName,
        long open,
        int capacity,
        long resolvedInRange) {}
