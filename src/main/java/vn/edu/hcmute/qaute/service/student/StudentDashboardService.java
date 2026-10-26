package vn.edu.hcmute.qaute.service.student;

import vn.edu.hcmute.qaute.dto.view.StudentDashboardView;

// Xử lý nghiệp vụ StudentDashboard trong hệ thống.
public interface StudentDashboardService {

    // Lấy dữ liệu tổng quan dành cho sinh viên.
    StudentDashboardView getDashboard(Long studentId);
}
