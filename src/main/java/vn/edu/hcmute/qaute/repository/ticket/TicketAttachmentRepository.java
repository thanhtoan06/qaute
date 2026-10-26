package vn.edu.hcmute.qaute.repository.ticket;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.ticket.TicketAttachment;

// Khai báo truy vấn lưu trữ dữ liệu TicketAttachment.
public interface TicketAttachmentRepository extends JpaRepository<TicketAttachment, Long> {
    // Lấy danh sách tệp đính kèm của vé.
    List<TicketAttachment> findByTicketId(Long ticketId);

    // Tìm tệp đính kèm theo mã tài nguyên.
    Optional<TicketAttachment> findByAssetId(Long assetId);
}
