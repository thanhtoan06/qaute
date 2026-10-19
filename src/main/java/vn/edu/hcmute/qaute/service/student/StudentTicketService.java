package vn.edu.hcmute.qaute.service.student;

import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.service.ticket.command.CreateTicketCommand;

public interface StudentTicketService {

    Ticket createTicket(Long studentId, CreateTicketCommand command);
}

