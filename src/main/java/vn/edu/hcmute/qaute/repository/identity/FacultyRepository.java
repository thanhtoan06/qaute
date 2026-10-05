package vn.edu.hcmute.qaute.repository.identity;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.identity.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    List<Faculty> findByActiveTrueOrderByNameAsc();
}
