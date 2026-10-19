package vn.edu.hcmute.qaute.service.student;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.service.ticket.TicketWorkflowService;
import vn.edu.hcmute.qaute.service.ticket.command.CreateTicketCommand;

@Service
@Transactional
public class StudentTicketServiceImpl implements StudentTicketService {

    private final TicketWorkflowService ticketWorkflowService;

    public StudentTicketServiceImpl(TicketWorkflowService ticketWorkflowService) {
        this.ticketWorkflowService = ticketWorkflowService;
    }

    @Override
    public Ticket createTicket(Long studentId, CreateTicketCommand command) {
        return ticketWorkflowService.createTicket(studentId, command);
    }
}

