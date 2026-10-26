package vn.edu.hcmute.qaute.dto.view.report;

// Đóng gói dữ liệu AppointmentReport để hiển thị hoặc trả về API.
public record AppointmentReport(
        long booked,
        long completed,
        double noShowPercent,
        double cancelPercent,
        double utilizationPercent) {}
