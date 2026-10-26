package vn.edu.hcmute.qaute.service.ticket;

import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.repository.ticket.TicketAttachmentRepository;
import vn.edu.hcmute.qaute.service.media.MediaAccessPolicy;

// Xử lý nghiệp vụ TicketMediaAccessPolicy trong hệ thống.
@Component
public class TicketMediaAccessPolicy implements MediaAccessPolicy {

    private final TicketAttachmentRepository attachments;
    private final TicketAccessService ticketAccess;

    // Khởi tạo chính sách truy cập tệp đính kèm ticket.
    public TicketMediaAccessPolicy(TicketAttachmentRepository attachments, TicketAccessService ticketAccess) {
        this.attachments = attachments;
        this.ticketAccess = ticketAccess;
    }

    @Override
    // Trả về loại sử dụng dành cho tệp đính kèm ticket.
    public MediaUsageType usage() {
        return MediaUsageType.TICKET_ATTACHMENT;
    }

    @Override
    // Kiểm tra người dùng có quyền truy cập tệp đính kèm ticket.
    public boolean canAccess(MediaAsset asset, Long userId, RoleCode role) {
        if (asset == null || asset.getId() == null) {
            return false;
        }
        return attachments.findByAssetId(asset.getId())
                .map(attachment -> ticketAccess.canView(attachment.getTicketId(), userId, role))
                .orElse(false);
    }
}
