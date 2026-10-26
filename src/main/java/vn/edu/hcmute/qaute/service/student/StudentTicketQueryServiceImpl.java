package vn.edu.hcmute.qaute.service.student;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.dto.request.student.TicketFilter;
import vn.edu.hcmute.qaute.dto.view.TicketListItem;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

// Xử lý nghiệp vụ StudentTicketQuery trong hệ thống.
@Service
public class StudentTicketQueryServiceImpl implements StudentTicketQueryService {

    private static final Set<String> SORT_WHITELIST = Set.of("lastActivityAt", "createdAt");
    private static final String DEFAULT_SORT = "lastActivityAt";

    private final TicketRepository ticketRepository;
    private final CategoryQueryService categoryQueryService;
    private final UserLookupService userLookupService;

    // Khởi tạo dịch vụ truy vấn yêu cầu với các phụ thuộc cần thiết.
    public StudentTicketQueryServiceImpl(TicketRepository ticketRepository,
                                         CategoryQueryService categoryQueryService,
                                         UserLookupService userLookupService) {
        this.ticketRepository = ticketRepository;
        this.categoryQueryService = categoryQueryService;
        this.userLookupService = userLookupService;
    }

    @Override
    @Transactional(readOnly = true)
    // Liệt kê yêu cầu của sinh viên theo bộ lọc và thông tin phân trang.
    public Page<TicketListItem> list(Long studentId, TicketFilter filter, Pageable pageable) {
        String sortField = filter.getSort() != null && SORT_WHITELIST.contains(filter.getSort())
                ? filter.getSort() : DEFAULT_SORT;
        Pageable sorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, sortField));

        Specification<Ticket> spec = buildSpec(studentId, filter);
        Page<Ticket> page = ticketRepository.findAll(spec, sorted);

        Set<Long> assigneeIds = page.getContent().stream()
                .map(Ticket::getCurrentAssigneeId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        Map<Long, UserSummary> assignees = userLookupService.getSummaries(assigneeIds);

        return page.map(t -> {
            String categoryName = categoryQueryService.getDisplayPath(t.getCategoryId());
            UserSummary assignee = t.getCurrentAssigneeId() != null
                    ? assignees.get(t.getCurrentAssigneeId()) : null;
            String assigneeName = assignee != null ? assignee.fullName() : null;
            return new TicketListItem(t.getId(), t.getCode(), t.getTitle(), categoryName,
                    t.getStatus(), t.getPriority(), assigneeName, t.getCreatedAt(), t.getLastActivityAt());
        });
    }

    @Override
    @Transactional(readOnly = true)
    // Đếm số yêu cầu của sinh viên theo từng trạng thái.
    public Map<TicketStatus, Long> countByStatus(Long studentId) {
        Specification<Ticket> base = (root, query, cb) ->
                cb.equal(root.get("studentId"), studentId);
        List<Ticket> all = ticketRepository.findAll(base);
        Map<TicketStatus, Long> counts = new EnumMap<>(TicketStatus.class);
        for (Ticket t : all) {
            counts.merge(t.getStatus(), 1L, Long::sum);
        }
        return counts;
    }

    // Tạo bộ điều kiện truy vấn yêu cầu của sinh viên.
    private Specification<Ticket> buildSpec(Long studentId, TicketFilter f) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("studentId"), studentId));

            if (f.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), f.getStatus()));
            }
            if (f.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), f.getCategoryId()));
            }
            if (f.getQ() != null && !f.getQ().isBlank()) {
                String escaped = "%" + TextUtil.escapeLike(f.getQ().trim()) + "%";
                Predicate byTitle = cb.like(root.get("title"), escaped);
                Predicate byCode = cb.like(cb.upper(root.get("code")),
                        "%" + TextUtil.escapeLike(f.getQ().trim().toUpperCase()) + "%");
                predicates.add(cb.or(byTitle, byCode));
            }
            if (f.getFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"),
                        f.getFrom().atStartOfDay()));
            }
            if (f.getTo() != null) {
                LocalDateTime end = f.getTo().plusDays(1).atStartOfDay();
                predicates.add(cb.lessThan(root.get("createdAt"), end));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
