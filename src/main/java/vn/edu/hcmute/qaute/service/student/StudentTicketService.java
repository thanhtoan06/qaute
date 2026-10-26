package vn.edu.hcmute.qaute.service.student;

import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.service.ticket.command.CreateTicketCommand;

// Xử lý nghiệp vụ StudentTicket trong hệ thống.
public interface StudentTicketService {

    // Tạo yêu cầu mới cho sinh viên.
    Ticket createTicket(Long studentId, CreateTicketCommand command);
}
