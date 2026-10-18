package vn.edu.hcmute.qaute.dto.request.manager;

import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;

@Getter
@Setter
public class QueueFilter {
    private String tab = "unassigned";
    private Long categoryId;
    private TicketPriority priority;
    private TicketStatus status;
    private Boolean urgentRequested;
    private Long assigneeId;
    private Integer minAgeHours;
    private String q;
    private String sort;
}
