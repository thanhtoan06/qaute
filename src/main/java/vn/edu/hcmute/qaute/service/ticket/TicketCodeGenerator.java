package vn.edu.hcmute.qaute.service.ticket;

import java.time.Year;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.entity.ticket.TicketSequence;
import vn.edu.hcmute.qaute.repository.ticket.TicketSequenceRepository;

@Component
public class TicketCodeGenerator {
    private final TicketSequenceRepository sequences;

    public TicketCodeGenerator(TicketSequenceRepository sequences) {
        this.sequences = sequences;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String next() {
        int year = Year.now().getValue();
        TicketSequence sequence = sequences.lockByYear(year).orElseGet(() -> create(year));
        int nextValue = sequence.getLastValue() + 1;
        sequence.setLastValue(nextValue);
        sequences.save(sequence);
        return "QAUTE-%d-%06d".formatted(year, nextValue);
    }

    private TicketSequence create(int year) {
        try {
            return sequences.saveAndFlush(new TicketSequence(year, 0));
        } catch (DataIntegrityViolationException exception) {
            return sequences.lockByYear(year).orElseThrow(() -> exception);
        }
    }
}
