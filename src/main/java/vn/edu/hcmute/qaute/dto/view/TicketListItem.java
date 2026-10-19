package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;

public record TicketListItem(
        Long id,
        String code,
        String title,
        String categoryName,
        TicketStatus status,
        TicketPriority priority,
        String assigneeName,
        LocalDateTime createdAt,
        LocalDateTime lastActivityAt) {
}

