package vn.edu.hcmute.qaute.dto.view.report;

// Đóng gói dữ liệu DepartmentCompare để hiển thị hoặc trả về API.
public record DepartmentCompare(
        String departmentName,
        long tickets,
        double avgFirstResponseMinutes,
        double slaPercent,
        double csat) {}
