package vn.edu.hcmute.qaute.dto.view;

import java.util.List;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;

public record ChatStudentContextView(StudentSummary student, List<RecentTicket> recentTickets) {
    public record RecentTicket(Long id, String code, String title, TicketStatus status) {}
}
