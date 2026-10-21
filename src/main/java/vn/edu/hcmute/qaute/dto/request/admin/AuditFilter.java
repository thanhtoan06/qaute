package vn.edu.hcmute.qaute.dto.request.admin;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class AuditFilter {
    private Long actorId;
    private String actorQ;
    private String action;
    private String entityType;
    private Long entityId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;
}
