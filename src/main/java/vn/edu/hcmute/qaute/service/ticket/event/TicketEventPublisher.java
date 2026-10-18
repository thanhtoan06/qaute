package vn.edu.hcmute.qaute.service.ticket.event;

import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;

@Component
public class TicketEventPublisher {
    private final ApplicationEventPublisher publisher;

    public TicketEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(TicketEventType type, Ticket ticket, Long actorId, Map<String, String> extra) {
        publisher.publishEvent(new TicketDomainEvent(type, ticket.getId(), actorId,
                extra == null ? Map.of() : Map.copyOf(extra)));
    }
}
