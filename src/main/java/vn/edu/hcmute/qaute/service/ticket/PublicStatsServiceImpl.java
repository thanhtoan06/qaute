package vn.edu.hcmute.qaute.service.ticket;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;

@Service
@Transactional(readOnly = true)
public class PublicStatsServiceImpl implements PublicStatsService {

    private final TicketRepository ticketRepository;

    public PublicStatsServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Double getMedianFirstResponseMinutes(int days) {
        int windowDays = days > 0 ? days : 30;
        LocalDateTime since = LocalDateTime.now().minusDays(windowDays);
        List<Object[]> rows = ticketRepository.findFirstResponseTimesSince(since);
        if (rows == null || rows.isEmpty()) {
            return null;
        }

        List<Double> minutesList = new ArrayList<>();
        for (Object[] row : rows) {
            if (row == null || row.length < 2) {
                continue;
            }
            LocalDateTime createdAt = (LocalDateTime) row[0];
            LocalDateTime firstResponseAt = (LocalDateTime) row[1];
            if (createdAt != null && firstResponseAt != null && !firstResponseAt.isBefore(createdAt)) {
                long minutes = Duration.between(createdAt, firstResponseAt).toMinutes();
                minutesList.add((double) minutes);
            }
        }

        if (minutesList.isEmpty()) {
            return null;
        }

        Collections.sort(minutesList);
        int n = minutesList.size();
        if (n % 2 == 1) {
            return minutesList.get(n / 2);
        } else {
            return (minutesList.get(n / 2 - 1) + minutesList.get(n / 2)) / 2.0;
        }
    }
}

