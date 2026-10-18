package vn.edu.hcmute.qaute.service.ticket;

import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;

public interface TicketAccessService {

    boolean canView(Long ticketId, Long userId, RoleCode role);

    Ticket requireViewable(Long ticketId, Long userId, RoleCode role);
}

