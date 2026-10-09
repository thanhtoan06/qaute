package vn.edu.hcmute.qaute.repository.governance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmute.qaute.entity.governance.BusinessHour;

public interface BusinessHourRepository extends JpaRepository<BusinessHour, Long> {

    List<BusinessHour> findByActiveTrueOrderByDayOfWeekAscStartTimeAsc();

    List<BusinessHour> findAllByOrderByDayOfWeekAscStartTimeAsc();
}
