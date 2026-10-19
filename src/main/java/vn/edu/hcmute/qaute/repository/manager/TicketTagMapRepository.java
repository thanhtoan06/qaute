package vn.edu.hcmute.qaute.repository.manager;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.manager.TicketTagMap;

public interface TicketTagMapRepository extends JpaRepository<TicketTagMap, Long> {
    List<TicketTagMap> findByTicketId(Long ticketId);
    void deleteByTicketIdAndTagId(Long ticketId, Long tagId);
    boolean existsByTicketIdAndTagId(Long ticketId, Long tagId);
    long countByTicketId(Long ticketId);
}
