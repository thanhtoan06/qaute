package vn.edu.hcmute.qaute.repository.governance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmute.qaute.entity.governance.BusinessHour;

// Khai báo truy vấn lưu trữ dữ liệu BusinessHour.
public interface BusinessHourRepository extends JpaRepository<BusinessHour, Long> {

    // Lấy giờ làm việc đang hoạt động theo thứ và giờ bắt đầu.
    List<BusinessHour> findByActiveTrueOrderByDayOfWeekAscStartTimeAsc();

    // Lấy toàn bộ giờ làm việc theo thứ và giờ bắt đầu.
    List<BusinessHour> findAllByOrderByDayOfWeekAscStartTimeAsc();
}
