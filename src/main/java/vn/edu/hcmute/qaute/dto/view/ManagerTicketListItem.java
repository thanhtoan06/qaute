package vn.edu.hcmute.qaute.dto.view;

import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.SlaStatus;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;

public record ManagerTicketListItem(Long id, String code, String title, String studentName,
                                    String categoryPath, TicketPriority priority, TicketStatus status,
                                    SlaStatus slaStatus, String assigneeName, boolean mine,
                                    boolean urgentRequested, LocalDateTime createdAt,
                                    LocalDateTime lastActivityAt, long ageHours) {
}
