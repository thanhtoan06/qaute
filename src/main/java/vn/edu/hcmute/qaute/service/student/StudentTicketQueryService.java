package vn.edu.hcmute.qaute.service.student;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.dto.request.student.TicketFilter;
import vn.edu.hcmute.qaute.dto.view.TicketListItem;

public interface StudentTicketQueryService {

    Page<TicketListItem> list(Long studentId, TicketFilter filter, Pageable pageable);

    Map<TicketStatus, Long> countByStatus(Long studentId);
}

