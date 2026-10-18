package vn.edu.hcmute.qaute.service.ticket;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.SlaStatus;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.dto.view.SlaSnapshot;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;

public interface SlaService {
    SlaSnapshot computeInitial(Long categoryId, TicketPriority priority, LocalDateTime start);
    LocalDateTime addBusinessMinutes(LocalDateTime from, long minutes);
    long businessMinutesBetween(LocalDateTime from, LocalDateTime to);
    SlaStatus evaluate(Ticket ticket, LocalDateTime now);
    void pause(Ticket ticket, LocalDateTime now);
    void resume(Ticket ticket, LocalDateTime now);
    void recompute(Ticket ticket, LocalDateTime now);
}
