package vn.edu.hcmute.qaute.dto.view.report;

// Đóng gói dữ liệu BacklogStats để hiển thị hoặc trả về API.
public record BacklogStats(long open, long under24h, long d1to3, long over3d) {}
