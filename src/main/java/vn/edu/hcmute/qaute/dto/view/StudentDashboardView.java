package vn.edu.hcmute.qaute.dto.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;

// Đóng gói dữ liệu StudentDashboard để hiển thị hoặc trả về API.
public record StudentDashboardView(
        Map<TicketStatus, Long> ticketCounts,
        List<TicketListItem> needMyAction,
        AppointmentView nextAppointment,
        long unreadNotifications,
        List<FaqListItem> suggestedFaqs
) {

    // Lấy số ticket theo trạng thái, mặc định là 0.
    public long countFor(TicketStatus status) {
        return ticketCounts != null ? ticketCounts.getOrDefault(status, 0L) : 0L;
    }

    // Trả về số ticket mới.
    public long getNewCount() {
        return countFor(TicketStatus.NEW);
    }

    // Trả về số ticket đang xử lý.
    public long getInProgressCount() {
        return countFor(TicketStatus.IN_PROGRESS);
    }

    // Trả về số ticket đang chờ sinh viên.
    public long getWaitingStudentCount() {
        return countFor(TicketStatus.WAITING_STUDENT);
    }

    // Trả về số ticket đã giải quyết.
    public long getResolvedCount() {
        return countFor(TicketStatus.RESOLVED);
    }

    // Chuyển số lượng ticket thành map theo tên trạng thái.
    public Map<String, Long> getCountsByName() {
        if (ticketCounts == null) {
            return Map.of();
        }
        Map<String, Long> map = new HashMap<>();
        ticketCounts.forEach((k, v) -> map.put(k.name(), v));
        return map;
    }
}
