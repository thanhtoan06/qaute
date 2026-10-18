package vn.edu.hcmute.qaute.service.ticket;

import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.repository.ticket.TicketAttachmentRepository;
import vn.edu.hcmute.qaute.service.media.MediaAccessPolicy;

@Component
public class TicketMediaAccessPolicy implements MediaAccessPolicy {

    private final TicketAttachmentRepository attachments;
    private final TicketAccessService ticketAccess;

    public TicketMediaAccessPolicy(TicketAttachmentRepository attachments, TicketAccessService ticketAccess) {
        this.attachments = attachments;
        this.ticketAccess = ticketAccess;
    }

    @Override
    public MediaUsageType usage() {
        return MediaUsageType.TICKET_ATTACHMENT;
    }

    @Override
    public boolean canAccess(MediaAsset asset, Long userId, RoleCode role) {
        if (asset == null || asset.getId() == null) {
            return false;
        }
        return attachments.findByAssetId(asset.getId())
                .map(attachment -> ticketAccess.canView(attachment.getTicketId(), userId, role))
                .orElse(false);
    }
}

