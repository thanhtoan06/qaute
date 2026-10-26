package vn.edu.hcmute.qaute.service.ticket.event;

import java.util.Map;

// Xử lý nghiệp vụ TicketDomainEvent trong hệ thống.
public record TicketDomainEvent(TicketEventType type, Long ticketId, Long actorId,
                                Map<String, String> extra) {
}
