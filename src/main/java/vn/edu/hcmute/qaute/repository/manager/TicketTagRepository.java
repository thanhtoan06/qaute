package vn.edu.hcmute.qaute.repository.manager;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.manager.TicketTag;

public interface TicketTagRepository extends JpaRepository<TicketTag, Long> {
    Optional<TicketTag> findByNameIgnoreCase(String name);
    List<TicketTag> findAllByOrderByNameAsc();
}
