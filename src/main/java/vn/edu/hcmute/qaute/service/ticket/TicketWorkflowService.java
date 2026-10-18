package vn.edu.hcmute.qaute.service.ticket;

import java.util.List;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.service.ticket.command.CreateTicketCommand;

public interface TicketWorkflowService {
    Ticket createTicket(Long studentId, CreateTicketCommand cmd);
    Ticket addStudentReply(Long ticketId, Long studentId, String contentHtml, List<Long> assetIds);
    Ticket addAdvisorReply(Long ticketId, Long managerId, String contentHtml, List<Long> assetIds);
    Ticket claim(Long ticketId, Long managerId);
    Ticket assign(Long ticketId, Long toManagerId, Long actorUserId, String reason);
    Ticket returnToQueue(Long ticketId, Long actorUserId, String reason);
    Ticket requestMoreInfo(Long ticketId, Long managerId, String messageHtml, List<Long> assetIds);
    Ticket resolve(Long ticketId, Long managerId, String resolutionNote);
    Ticket confirmClose(Long ticketId, Long studentId);
    Ticket reopen(Long ticketId, Long studentId, String reason);
    Ticket cancel(Long ticketId, Long studentId, String reason);
    Ticket changePriority(Long ticketId, Long actorUserId, TicketPriority priority, String reason);
    Ticket changeCategory(Long ticketId, Long actorUserId, Long newCategoryId, String reason);
    Ticket escalate(Long ticketId, Long managerId, Long toCategoryId, Long toManagerId, String reason);
    Ticket forceClose(Long ticketId, Long adminId, String reason);
    int autoCloseExpired();
    void autoAssignIfEnabled(Ticket ticket);
}
