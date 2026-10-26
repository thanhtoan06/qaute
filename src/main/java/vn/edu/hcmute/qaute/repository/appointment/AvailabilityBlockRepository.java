package vn.edu.hcmute.qaute.repository.appointment;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.appointment.AvailabilityBlock;

// Khai báo truy vấn lưu trữ dữ liệu AvailabilityBlock.
public interface AvailabilityBlockRepository extends JpaRepository<AvailabilityBlock, Long> {
    // Tìm các khoảng bận còn hiệu lực của quản lý.
    List<AvailabilityBlock> findByManagerIdAndEndAtAfterOrderByStartAtAsc(Long managerId, LocalDateTime after);

    // Tìm các khoảng bận giao với khoảng thời gian chỉ định.
    List<AvailabilityBlock> findByManagerIdAndStartAtLessThanAndEndAtGreaterThan(
            Long managerId, LocalDateTime endAt, LocalDateTime startAt);
}
