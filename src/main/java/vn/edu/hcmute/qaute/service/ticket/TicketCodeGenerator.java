package vn.edu.hcmute.qaute.service.ticket;

import java.time.Year;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.entity.ticket.TicketSequence;
import vn.edu.hcmute.qaute.repository.ticket.TicketSequenceRepository;

// Xử lý nghiệp vụ TicketCodeGenerator trong hệ thống.
@Component
public class TicketCodeGenerator {
    private final TicketSequenceRepository sequences;

    // Khởi tạo bộ tạo mã ticket với kho lưu trữ dãy số.
    public TicketCodeGenerator(TicketSequenceRepository sequences) {
        this.sequences = sequences;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    // Cấp mã ticket tiếp theo theo năm hiện tại.
    public String next() {
        int year = Year.now().getValue();
        TicketSequence sequence = sequences.lockByYear(year).orElseGet(() -> create(year));
        int nextValue = sequence.getLastValue() + 1;
        sequence.setLastValue(nextValue);
        sequences.save(sequence);
        return "QAUTE-%d-%06d".formatted(year, nextValue);
    }

    // Tạo dãy số cho năm nếu chưa tồn tại.
    private TicketSequence create(int year) {
        try {
            return sequences.saveAndFlush(new TicketSequence(year, 0));
        } catch (DataIntegrityViolationException exception) {
            return sequences.lockByYear(year).orElseThrow(() -> exception);
        }
    }
}
