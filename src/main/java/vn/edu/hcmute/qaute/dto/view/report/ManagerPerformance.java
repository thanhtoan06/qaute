package vn.edu.hcmute.qaute.dto.view.report;

// Đóng gói dữ liệu ManagerPerformance để hiển thị hoặc trả về API.
public record ManagerPerformance(
        Long managerId,
        String managerName,
        long resolved,
        double avgFirstResponseMinutes,
        double csat,
        long chats) {}
