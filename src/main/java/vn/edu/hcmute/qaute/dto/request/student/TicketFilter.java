package vn.edu.hcmute.qaute.dto.request.student;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;

@Getter
@Setter
@NoArgsConstructor
public class TicketFilter {
    private TicketStatus status;
    private Long categoryId;
    private String q;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;
    private String sort;
}

