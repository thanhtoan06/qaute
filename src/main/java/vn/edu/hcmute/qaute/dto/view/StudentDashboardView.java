package vn.edu.hcmute.qaute.dto.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;

public record StudentDashboardView(
        Map<TicketStatus, Long> ticketCounts,
        List<TicketListItem> needMyAction,
        AppointmentView nextAppointment,
        long unreadNotifications,
        List<FaqListItem> suggestedFaqs
) {

    public long countFor(TicketStatus status) {
        return ticketCounts != null ? ticketCounts.getOrDefault(status, 0L) : 0L;
    }

    public long getNewCount() {
        return countFor(TicketStatus.NEW);
    }

    public long getInProgressCount() {
        return countFor(TicketStatus.IN_PROGRESS);
    }

    public long getWaitingStudentCount() {
        return countFor(TicketStatus.WAITING_STUDENT);
    }

    public long getResolvedCount() {
        return countFor(TicketStatus.RESOLVED);
    }

    public Map<String, Long> getCountsByName() {
        if (ticketCounts == null) {
            return Map.of();
        }
        Map<String, Long> map = new HashMap<>();
        ticketCounts.forEach((k, v) -> map.put(k.name(), v));
        return map;
    }
}

