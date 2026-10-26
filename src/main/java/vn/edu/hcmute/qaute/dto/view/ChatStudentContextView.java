package vn.edu.hcmute.qaute.dto.view;

import java.util.List;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;

// Đóng gói dữ liệu ChatStudentContext để hiển thị hoặc trả về API.
public record ChatStudentContextView(StudentSummary student, List<RecentTicket> recentTickets) {
    public record RecentTicket(Long id, String code, String title, TicketStatus status) {}
}
