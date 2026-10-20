package vn.edu.hcmute.qaute.repository.appointment;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.appointment.AvailabilityBlock;

public interface AvailabilityBlockRepository extends JpaRepository<AvailabilityBlock, Long> {
    List<AvailabilityBlock> findByManagerIdAndEndAtAfterOrderByStartAtAsc(Long managerId, LocalDateTime after);
    List<AvailabilityBlock> findByManagerIdAndStartAtLessThanAndEndAtGreaterThan(
            Long managerId, LocalDateTime endAt, LocalDateTime startAt);
}
