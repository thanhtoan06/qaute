package vn.edu.hcmute.qaute.service.ticket.event;

import java.util.Map;

public record TicketDomainEvent(TicketEventType type, Long ticketId, Long actorId,
                                Map<String, String> extra) {
}
