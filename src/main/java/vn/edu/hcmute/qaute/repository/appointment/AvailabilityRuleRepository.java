package vn.edu.hcmute.qaute.repository.appointment;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hcmute.qaute.entity.appointment.AvailabilityRule;

// Khai báo truy vấn lưu trữ dữ liệu AvailabilityRule.
public interface AvailabilityRuleRepository extends JpaRepository<AvailabilityRule, Long> {
    // Tìm quy tắc khả dụng của quản lý theo thứ và giờ bắt đầu.
    List<AvailabilityRule> findByManagerIdOrderByDayOfWeekAscStartTimeAsc(Long managerId);

    // Tìm các quy tắc khả dụng đang hoạt động.
    List<AvailabilityRule> findByActiveTrue();

    // Lấy mã các quản lý có quy tắc khả dụng đang hoạt động.
    @Query("select distinct r.managerId from AvailabilityRule r where r.active = true")
    List<Long> findActiveManagerIds();
}
