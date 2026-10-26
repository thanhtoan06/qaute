package vn.edu.hcmute.qaute.service.ticket;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.SlaStatus;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.dto.view.SlaSnapshot;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;

// Xử lý nghiệp vụ Sla trong hệ thống.
public interface SlaService {
    // Tính các mốc SLA ban đầu cho chuyên mục và mức ưu tiên.
    SlaSnapshot computeInitial(Long categoryId, TicketPriority priority, LocalDateTime start);
    // Cộng số phút làm việc vào thời điểm bắt đầu.
    LocalDateTime addBusinessMinutes(LocalDateTime from, long minutes);
    // Tính số phút làm việc giữa hai thời điểm.
    long businessMinutesBetween(LocalDateTime from, LocalDateTime to);
    // Đánh giá trạng thái SLA của ticket tại thời điểm cho trước.
    SlaStatus evaluate(Ticket ticket, LocalDateTime now);
    // Tạm dừng đồng hồ SLA của ticket.
    void pause(Ticket ticket, LocalDateTime now);
    // Tiếp tục đồng hồ SLA và cập nhật các mốc đến hạn.
    void resume(Ticket ticket, LocalDateTime now);
    // Tính lại các chỉ tiêu và trạng thái SLA của ticket.
    void recompute(Ticket ticket, LocalDateTime now);
}
