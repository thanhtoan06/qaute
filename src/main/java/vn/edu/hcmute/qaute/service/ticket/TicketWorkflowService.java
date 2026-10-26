package vn.edu.hcmute.qaute.service.ticket;

import java.util.List;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.service.ticket.command.CreateTicketCommand;

// Xử lý nghiệp vụ TicketWorkflow trong hệ thống.
public interface TicketWorkflowService {
    // Tạo ticket mới cho sinh viên.
    Ticket createTicket(Long studentId, CreateTicketCommand cmd);
    // Thêm phản hồi của sinh viên vào ticket.
    Ticket addStudentReply(Long ticketId, Long studentId, String contentHtml, List<Long> assetIds);
    // Thêm phản hồi của tư vấn viên vào ticket.
    Ticket addAdvisorReply(Long ticketId, Long managerId, String contentHtml, List<Long> assetIds);
    // Nhận xử lý ticket đang chờ trong phạm vi tư vấn viên.
    Ticket claim(Long ticketId, Long managerId);
    // Giao ticket cho tư vấn viên được chỉ định.
    Ticket assign(Long ticketId, Long toManagerId, Long actorUserId, String reason);
    // Trả ticket đang xử lý về hàng chờ.
    Ticket returnToQueue(Long ticketId, Long actorUserId, String reason);
    // Yêu cầu sinh viên cung cấp thêm thông tin.
    Ticket requestMoreInfo(Long ticketId, Long managerId, String messageHtml, List<Long> assetIds);
    // Đánh dấu ticket đã được giải quyết.
    Ticket resolve(Long ticketId, Long managerId, String resolutionNote);
    // Xác nhận đóng ticket đã giải quyết.
    Ticket confirmClose(Long ticketId, Long studentId);
    // Mở lại ticket theo yêu cầu của sinh viên.
    Ticket reopen(Long ticketId, Long studentId, String reason);
    // Hủy ticket mới theo yêu cầu của sinh viên.
    Ticket cancel(Long ticketId, Long studentId, String reason);
    // Thay đổi mức ưu tiên của ticket.
    Ticket changePriority(Long ticketId, Long actorUserId, TicketPriority priority, String reason);
    // Chuyển ticket sang chuyên mục khác.
    Ticket changeCategory(Long ticketId, Long actorUserId, Long newCategoryId, String reason);
    // Chuyển ticket sang chuyên mục hoặc tư vấn viên khác.
    Ticket escalate(Long ticketId, Long managerId, Long toCategoryId, Long toManagerId, String reason);
    // Đóng ticket bắt buộc theo yêu cầu của quản trị viên.
    Ticket forceClose(Long ticketId, Long adminId, String reason);
    // Tự động đóng ticket đã quá hạn.
    int autoCloseExpired();
}
