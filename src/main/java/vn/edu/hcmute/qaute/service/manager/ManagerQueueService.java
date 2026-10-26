package vn.edu.hcmute.qaute.service.manager;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.SlaStatus;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.common.util.BoundedPages;
import vn.edu.hcmute.qaute.dto.request.manager.QueueFilter;
import vn.edu.hcmute.qaute.dto.view.ManagerTicketListItem;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;

// Xử lý nghiệp vụ ManagerQueue trong hệ thống.
@Service
@Transactional(readOnly = true)
public class ManagerQueueService {
    public static final List<String> TABS = List.of("unassigned", "mine", "waiting", "slarisk", "all");
    public static final Set<String> SORT_FIELDS = Set.of("createdAt", "lastActivityAt");
    private static final Set<String> MINE_STATUSES = Set.of("active", "processed");

    private final TicketRepository tickets;
    private final ManagerScopeService scopes;
    private final UserLookupService users;
    private final CategoryQueryService categories;

    // Khởi tạo dịch vụ hàng đợi yêu cầu của tư vấn viên.
    public ManagerQueueService(TicketRepository tickets, ManagerScopeService scopes,
                               UserLookupService users, CategoryQueryService categories) {
        this.tickets = tickets;
        this.scopes = scopes;
        this.users = users;
        this.categories = categories;
    }

    // Tìm kiếm và phân trang các yêu cầu trong phạm vi quản lý.
    public Page<ManagerTicketListItem> list(Long managerId, QueueFilter filter, Pageable pageable) {
        pageable = BoundedPages.of(pageable);
        QueueFilter safeFilter = filter == null ? new QueueFilter() : filter;
        String tab = tab(safeFilter.getTab());
        safeFilter.setMineStatus(mineStatus(safeFilter.getMineStatus()));
        boolean explicitSort = safeFilter.getSort() != null && SORT_FIELDS.contains(safeFilter.getSort());
        List<Long> scopeIds = scopes.getScopeCategoryIds(managerId);
        LocalDateTime now = LocalDateTime.now();
        Specification<Ticket> spec = specification(managerId, scopeIds, tab, safeFilter, now,
                !explicitSort);
        Pageable effectivePageable = explicitSort
                ? pageable : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<Ticket> page = tickets.findAll(spec, effectivePageable);
        Set<Long> userIds = new LinkedHashSet<>();
        Set<Long> categoryIds = new LinkedHashSet<>();
        for (Ticket ticket : page) {
            userIds.add(ticket.getStudentId());
            if (ticket.getCurrentAssigneeId() != null) userIds.add(ticket.getCurrentAssigneeId());
            categoryIds.add(ticket.getCategoryId());
        }
        Map<Long, UserSummary> summaries = users.getSummaries(userIds);
        Map<Long, String> paths = new HashMap<>();
        categoryIds.forEach(id -> paths.put(id, categories.getDisplayPath(id)));
        return page.map(ticket -> new ManagerTicketListItem(ticket.getId(), ticket.getCode(),
                ticket.getTitle(), name(summaries, ticket.getStudentId()),
                paths.getOrDefault(ticket.getCategoryId(), ""), ticket.getPriority(),
                ticket.getStatus(), ticket.getSlaStatus(),
                name(summaries, ticket.getCurrentAssigneeId()),
                managerId.equals(ticket.getCurrentAssigneeId()), ticket.isUrgentRequested(),
                ticket.getCreatedAt(), ticket.getLastActivityAt(),
                ticket.getCreatedAt() == null ? 0 : Math.max(0, Duration.between(
                        ticket.getCreatedAt(), now).toHours())));
    }

    // Đếm số yêu cầu tương ứng với từng tab hàng đợi.
    public Map<String, Long> tabCounts(Long managerId) {
        List<Long> scopeIds = scopes.getScopeCategoryIds(managerId);
        Map<String, Long> counts = new LinkedHashMap<>();
        for (String tab : TABS) {
            QueueFilter filter = new QueueFilter();
            if (tab.equals("mine")) {
                filter.setMineStatus("active");
                long active = tickets.count(specification(managerId, scopeIds, tab,
                        filter, LocalDateTime.now(), false));
                filter.setMineStatus("processed");
                long processed = tickets.count(specification(managerId, scopeIds, tab,
                        filter, LocalDateTime.now(), false));
                counts.put("mineActive", active);
                counts.put("mineProcessed", processed);
                counts.put(tab, active + processed);
            } else {
                counts.put(tab, tickets.count(specification(managerId, scopeIds, tab,
                        filter, LocalDateTime.now(), false)));
            }
        }
        return counts;
    }

    // Lấy tên các chuyên mục trong phạm vi và đã được giao.
    public Map<Long, String> scopeCategories(Long managerId) {
        Map<Long, String> result = new LinkedHashMap<>();
        Set<Long> visibleCategories = new LinkedHashSet<>(scopes.getScopeCategoryIds(managerId));
        visibleCategories.addAll(tickets.findAssignedCategoryIds(managerId));
        for (Long id : visibleCategories) {
            result.put(id, categories.getDisplayPath(id));
        }
        return result;
    }

    // Chuẩn hóa tab không hợp lệ về tab mặc định.
    public static String tab(String raw) {
        return raw != null && TABS.contains(raw) ? raw : "unassigned";
    }

    // Chuẩn hóa nhóm trạng thái ticket của manager.
    public static String mineStatus(String raw) {
        return raw != null && MINE_STATUSES.contains(raw) ? raw : "active";
    }

    // Tạo điều kiện truy vấn yêu cầu theo phạm vi và bộ lọc.
    private Specification<Ticket> specification(Long managerId, List<Long> scopeIds, String tab,
                                                 QueueFilter filter, LocalDateTime now,
                                                 boolean defaultOrder) {
        return (root, query, cb) -> {
            List<Predicate> where = new ArrayList<>();
            Predicate assignedToMe = cb.equal(root.get("currentAssigneeId"), managerId);
            Predicate inScope = scopeIds == null || scopeIds.isEmpty() ? cb.disjunction()
                    : root.get("categoryId").in(scopeIds);
            where.add(cb.or(inScope, assignedToMe));
            switch (tab) {
                case "unassigned" -> {
                    where.add(inScope);
                    where.add(cb.equal(root.get("status"), TicketStatus.NEW));
                    where.add(cb.isNull(root.get("currentAssigneeId")));
                }
                case "mine" -> {
                    where.add(assignedToMe);
                    if ("processed".equals(filter.getMineStatus())) {
                        where.add(root.get("status").in(
                                TicketStatus.RESOLVED, TicketStatus.CLOSED, TicketStatus.CANCELLED));
                    } else {
                        where.add(root.get("status").in(
                                TicketStatus.ASSIGNED, TicketStatus.IN_PROGRESS,
                                TicketStatus.WAITING_STUDENT));
                    }
                }
                case "waiting" -> {
                    where.add(assignedToMe);
                    where.add(cb.equal(root.get("status"), TicketStatus.WAITING_STUDENT));
                }
                case "slarisk" -> {
                    where.add(root.get("slaStatus").in(SlaStatus.AT_RISK, SlaStatus.BREACHED));
                    where.add(notFinished(root.get("status"), cb));
                }
                default -> { }
            }
            if (filter.getCategoryId() != null) {
                List<Long> descendants = categories.getDescendantIds(filter.getCategoryId());
                where.add(descendants.isEmpty() ? cb.disjunction() : root.get("categoryId").in(descendants));
            }
            if (filter.getPriority() != null) where.add(cb.equal(root.get("priority"), filter.getPriority()));
            if (filter.getStatus() != null) where.add(cb.equal(root.get("status"), filter.getStatus()));
            if (filter.getUrgentRequested() != null) {
                where.add(cb.equal(root.get("urgentRequested"), filter.getUrgentRequested()));
            }
            if (filter.getAssigneeId() != null) {
                where.add(cb.equal(root.get("currentAssigneeId"), filter.getAssigneeId()));
            }
            if (filter.getMinAgeHours() != null && filter.getMinAgeHours() > 0) {
                where.add(cb.lessThanOrEqualTo(root.get("createdAt"), now.minusHours(
                        Math.min(filter.getMinAgeHours(), 100000))));
            }
            if (filter.getQ() != null && !filter.getQ().isBlank()) {
                String needle = "%" + TextUtil.escapeLike(filter.getQ().trim().toLowerCase(Locale.ROOT)) + "%";
                where.add(cb.or(cb.like(cb.lower(root.get("code")), needle, '\\'),
                        cb.like(cb.lower(root.get("title")), needle, '\\')));
            }
            if (defaultOrder && query != null && !Long.class.equals(query.getResultType())) {
                Expression<Integer> slaRank = cb.<Integer>selectCase()
                        .when(cb.equal(root.get("slaStatus"), SlaStatus.BREACHED), 0)
                        .when(cb.equal(root.get("slaStatus"), SlaStatus.AT_RISK), 1).otherwise(2);
                Expression<Integer> priorityRank = cb.<Integer>selectCase()
                        .when(cb.equal(root.get("priority"), TicketPriority.URGENT), 0)
                        .when(cb.equal(root.get("priority"), TicketPriority.HIGH), 1)
                        .when(cb.equal(root.get("priority"), TicketPriority.NORMAL), 2).otherwise(3);
                query.orderBy(cb.asc(slaRank), cb.asc(priorityRank),
                        cb.asc(root.get("createdAt")), cb.asc(root.get("id")));
            }
            return cb.and(where.toArray(Predicate[]::new));
        };
    }

    // Tạo điều kiện loại trừ yêu cầu đã đóng hoặc hủy.
    private static Predicate notFinished(Expression<TicketStatus> status,
                                         jakarta.persistence.criteria.CriteriaBuilder cb) {
        return cb.not(status.in(TicketStatus.CLOSED, TicketStatus.CANCELLED));
    }

    // Lấy tên người dùng từ bản đồ tóm tắt.
    private static String name(Map<Long, UserSummary> summaries, Long id) {
        UserSummary summary = id == null ? null : summaries.get(id);
        return summary == null ? "—" : summary.fullName();
    }
}
