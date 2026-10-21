package vn.edu.hcmute.qaute.service.student;

import vn.edu.hcmute.qaute.dto.view.StudentDashboardView;

public interface StudentDashboardService {

    StudentDashboardView getDashboard(Long studentId);
}

