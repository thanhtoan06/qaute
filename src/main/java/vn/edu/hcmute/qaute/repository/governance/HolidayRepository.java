package vn.edu.hcmute.qaute.repository.governance;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmute.qaute.entity.governance.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    boolean existsByDate(LocalDate date);

    List<Holiday> findAllByOrderByDateAsc();
}
