package vn.edu.hcmute.qaute.dto.view.report;

public record WorkloadItem(Long managerId, String managerName, long open, int capacity, long resolvedInRange) {}
