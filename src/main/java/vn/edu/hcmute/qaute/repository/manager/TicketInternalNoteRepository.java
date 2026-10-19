package vn.edu.hcmute.qaute.repository.manager;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.manager.TicketInternalNote;

public interface TicketInternalNoteRepository extends JpaRepository<TicketInternalNote, Long> {
    List<TicketInternalNote> findByTicketIdOrderByPinnedDescCreatedAtDesc(Long ticketId);
}
