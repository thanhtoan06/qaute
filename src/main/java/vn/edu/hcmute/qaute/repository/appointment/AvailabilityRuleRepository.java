package vn.edu.hcmute.qaute.repository.appointment;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hcmute.qaute.entity.appointment.AvailabilityRule;

public interface AvailabilityRuleRepository extends JpaRepository<AvailabilityRule, Long> {
    List<AvailabilityRule> findByManagerIdOrderByDayOfWeekAscStartTimeAsc(Long managerId);
    List<AvailabilityRule> findByActiveTrue();

    @Query("select distinct r.managerId from AvailabilityRule r where r.active = true")
    List<Long> findActiveManagerIds();
}
