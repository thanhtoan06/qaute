package vn.edu.hcmute.qaute.dto.request.admin;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import vn.edu.hcmute.qaute.common.constant.SlaStatus;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;

// Đóng gói dữ liệu đầu vào cho thao tác AdminTicketFilter.
@Getter
@Setter
public class AdminTicketFilter {
    private String q;
    private TicketStatus status;
    private TicketPriority priority;
    private Long categoryId;
    private Long departmentId;
    private Long assigneeId;
    private SlaStatus slaStatus;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;
    private Integer minRating;
    private Integer maxRating;
    private String sort;
}
