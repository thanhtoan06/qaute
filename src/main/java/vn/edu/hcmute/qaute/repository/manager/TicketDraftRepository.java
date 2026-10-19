package vn.edu.hcmute.qaute.repository.manager;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.manager.TicketDraft;

public interface TicketDraftRepository extends JpaRepository<TicketDraft, Long> {
    Optional<TicketDraft> findByTicketIdAndManagerId(Long ticketId, Long managerId);
    void deleteByTicketIdAndManagerId(Long ticketId, Long managerId);
}
