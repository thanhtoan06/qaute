package vn.edu.hcmute.qaute.service.student;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.dto.request.student.TicketFilter;
import vn.edu.hcmute.qaute.dto.view.TicketListItem;

// Xử lý nghiệp vụ StudentTicketQuery trong hệ thống.
public interface StudentTicketQueryService {

    // Liệt kê yêu cầu theo bộ lọc và thông tin phân trang.
    Page<TicketListItem> list(Long studentId, TicketFilter filter, Pageable pageable);

    // Đếm số yêu cầu theo từng trạng thái.
    Map<TicketStatus, Long> countByStatus(Long studentId);
}
