package vn.edu.hcmute.qaute.dto.view.report;

public record ManagerPerformance(Long managerId, String managerName, long resolved, double avgFirstResponseMinutes, double csat, long chats) {}
