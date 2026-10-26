package vn.edu.hcmute.qaute.repository.governance;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmute.qaute.entity.governance.Holiday;

// Khai báo truy vấn lưu trữ dữ liệu Holiday.
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    // Kiểm tra ngày nghỉ đã tồn tại.
    boolean existsByDate(LocalDate date);

    // Lấy danh sách ngày nghỉ theo thứ tự ngày tăng dần.
    List<Holiday> findAllByOrderByDateAsc();
}
