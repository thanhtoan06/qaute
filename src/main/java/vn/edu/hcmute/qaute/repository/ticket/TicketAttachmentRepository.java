package vn.edu.hcmute.qaute.repository.ticket;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.ticket.TicketAttachment;

public interface TicketAttachmentRepository extends JpaRepository<TicketAttachment, Long> {
    List<TicketAttachment> findByTicketId(Long ticketId);
    Optional<TicketAttachment> findByAssetId(Long assetId);
}
