package vn.edu.hcmute.qaute.dto.view.report;

public record AppointmentReport(long booked, long completed, double noShowPercent, double cancelPercent, double utilizationPercent) {}
