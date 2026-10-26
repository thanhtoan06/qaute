package vn.edu.hcmute.qaute.service.ticket;

import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;

// Xử lý nghiệp vụ TicketAccess trong hệ thống.
public interface TicketAccessService {

    // Kiểm tra người dùng có quyền xem ticket hay không.
    boolean canView(Long ticketId, Long userId, RoleCode role);

    // Lấy ticket nếu người dùng có quyền xem.
    Ticket requireViewable(Long ticketId, Long userId, RoleCode role);
}
