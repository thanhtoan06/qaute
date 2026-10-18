package vn.edu.hcmute.qaute.service.ticket;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;

@Service
@Transactional(readOnly = true)
public class TicketAccessServiceImpl implements TicketAccessService {

    private final TicketRepository tickets;
    private final ManagerScopeService managerScope;

    public TicketAccessServiceImpl(TicketRepository tickets, ManagerScopeService managerScope) {
        this.tickets = tickets;
        this.managerScope = managerScope;
    }

    @Override
    public boolean canView(Long ticketId, Long userId, RoleCode role) {
        if (ticketId == null || userId == null || role == null) {
            return false;
        }
        return tickets.findById(ticketId)
                .map(ticket -> checkPermission(ticket, userId, role))
                .orElse(false);
    }

    @Override
    public Ticket requireViewable(Long ticketId, Long userId, RoleCode role) {
        if (ticketId == null || userId == null || role == null) {
            throw new NotFoundException("Không tìm thấy ticket");
        }
        Ticket ticket = tickets.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ticket"));
        if (!checkPermission(ticket, userId, role)) {
            throw new NotFoundException("Không tìm thấy ticket");
        }
        return ticket;
    }

    private boolean checkPermission(Ticket ticket, Long userId, RoleCode role) {
        if (role == RoleCode.ADMIN) {
            return true;
        }
        if (role == RoleCode.STUDENT) {
            return userId.equals(ticket.getStudentId());
        }
        if (role == RoleCode.MANAGER) {
            return userId.equals(ticket.getCurrentAssigneeId())
                    || managerScope.isInScope(userId, ticket.getCategoryId());
        }
        return false;
    }
}

