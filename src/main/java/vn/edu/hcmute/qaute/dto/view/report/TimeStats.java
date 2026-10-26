package vn.edu.hcmute.qaute.dto.view.report;

// Đóng gói dữ liệu TimeStats để hiển thị hoặc trả về API.
public record TimeStats(long samples, double avgMinutes, double medianMinutes, double p90Minutes) {}
