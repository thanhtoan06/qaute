package vn.edu.hcmute.qaute.service.admin;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.common.util.BoundedPages;
import vn.edu.hcmute.qaute.dto.request.admin.AdminTicketFilter;
import vn.edu.hcmute.qaute.dto.view.ManagerTicketDetailView;
import vn.edu.hcmute.qaute.dto.view.ManagerTicketListItem;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.entity.ticket.TicketReview;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.manager.ManagerTicketDetailService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.ticket.TicketWorkflowService;

// Xử lý nghiệp vụ AdminTicket trong hệ thống.
@Service
public class AdminTicketService {
    private final TicketRepository tickets;
    private final CategoryRepository categoryRepository;
    private final CategoryQueryService categories;
    private final UserLookupService users;
    private final ManagerTicketDetailService details;
    private final TicketWorkflowService workflow;
    private final AuditService audit;

    // Khởi tạo dịch vụ quản trị ticket.
    public AdminTicketService(TicketRepository tickets, CategoryRepository categoryRepository,
            CategoryQueryService categories, UserLookupService users,
            ManagerTicketDetailService details, TicketWorkflowService workflow, AuditService audit) {
        this.tickets = tickets;
        this.categoryRepository = categoryRepository;
        this.categories = categories;
        this.users = users;
        this.details = details;
        this.workflow = workflow;
        this.audit = audit;
    }

    // Tìm ticket theo bộ lọc và phân trang.
    @Transactional(readOnly = true)
    public Page<ManagerTicketListItem> search(AdminTicketFilter filter, Pageable pageable) {
        pageable = BoundedPages.of(pageable);
        AdminTicketFilter f = filter == null ? new AdminTicketFilter() : filter;
        List<Long> categoryIds = f.getCategoryId() == null ? null : categories.getDescendantIds(f.getCategoryId());
        List<Long> departmentIds = f.getDepartmentId() == null ? null : categoryRepository
                .findByDepartmentIdOrderBySortOrderAscNameAsc(f.getDepartmentId()).stream()
                .map(category -> category.getId()).toList();
        Specification<Ticket> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (f.getQ() != null && !f.getQ().isBlank()) {
                String needle = "%" + TextUtil.escapeLike(f.getQ().trim().toLowerCase(Locale.ROOT)) + "%";
                predicates.add(cb.or(cb.like(cb.lower(root.get("code")), needle, '\\'),
                        cb.like(cb.lower(root.get("title")), needle, '\\')));
            }
            if (f.getStatus() != null) predicates.add(cb.equal(root.get("status"), f.getStatus()));
            if (f.getPriority() != null) predicates.add(cb.equal(root.get("priority"), f.getPriority()));
            if (f.getSlaStatus() != null) predicates.add(cb.equal(root.get("slaStatus"), f.getSlaStatus()));
            if (f.getAssigneeId() != null) predicates.add(cb.equal(root.get("currentAssigneeId"), f.getAssigneeId()));
            if (categoryIds != null) predicates.add(categoryIds.isEmpty() ? cb.disjunction()
                    : root.get("categoryId").in(categoryIds));
            if (departmentIds != null) predicates.add(departmentIds.isEmpty() ? cb.disjunction()
                    : root.get("categoryId").in(departmentIds));
            if (f.getFrom() != null) predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), f.getFrom().atStartOfDay()));
            if (f.getTo() != null) predicates.add(cb.lessThan(root.get("createdAt"), f.getTo().plusDays(1).atStartOfDay()));
            if (f.getMinRating() != null || f.getMaxRating() != null) {
                Subquery<Long> reviewed = query.subquery(Long.class);
                Root<TicketReview> review = reviewed.from(TicketReview.class);
                List<Predicate> rating = new ArrayList<>();
                rating.add(cb.equal(review.get("ticketId"), root.get("id")));
                if (f.getMinRating() != null) rating.add(cb.ge(review.get("rating"), f.getMinRating()));
                if (f.getMaxRating() != null) rating.add(cb.le(review.get("rating"), f.getMaxRating()));
                reviewed.select(review.get("ticketId")).where(rating.toArray(Predicate[]::new));
                predicates.add(cb.exists(reviewed));
            }
            if ("priority".equals(f.getSort()) && !Long.class.equals(query.getResultType())) {
                var rank = cb.<Integer>selectCase()
                        .when(cb.equal(root.get("priority"), TicketPriority.URGENT), 0)
                        .when(cb.equal(root.get("priority"), TicketPriority.HIGH), 1)
                        .when(cb.equal(root.get("priority"), TicketPriority.NORMAL), 2)
                        .otherwise(3);
                query.orderBy(cb.asc(rank), cb.desc(root.get("createdAt")), cb.desc(root.get("id")));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        LocalDateTime now = LocalDateTime.now();
        Page<Ticket> page = tickets.findAll(spec, pageable);
        Set<Long> userIds = new HashSet<>();
        Set<Long> categoryKeys = new HashSet<>();
        page.forEach(ticket -> {
            userIds.add(ticket.getStudentId());
            if (ticket.getCurrentAssigneeId() != null) userIds.add(ticket.getCurrentAssigneeId());
            categoryKeys.add(ticket.getCategoryId());
        });
        Map<Long, UserSummary> summaries = userIds.isEmpty() ? Map.of() : users.getSummaries(userIds);
        Map<Long, String> paths = new HashMap<>();
        categoryKeys.forEach(id -> paths.put(id, categories.getDisplayPath(id)));
        Long viewerId = SecurityUtils.requireUserId();
        return page.map(ticket -> new ManagerTicketListItem(ticket.getId(), ticket.getCode(),
                ticket.getTitle(), name(summaries, ticket.getStudentId()),
                paths.getOrDefault(ticket.getCategoryId(), ""), ticket.getPriority(), ticket.getStatus(),
                ticket.getSlaStatus(), name(summaries, ticket.getCurrentAssigneeId()),
                viewerId.equals(ticket.getCurrentAssigneeId()), ticket.isUrgentRequested(),
                ticket.getCreatedAt(), ticket.getLastActivityAt(), ticket.getCreatedAt() == null ? 0
                        : Math.max(0, Duration.between(ticket.getCreatedAt(), now).toHours())));
    }

    // Lấy chi tiết ticket với quyền xem của quản trị viên.
    @Transactional(readOnly = true)
    public ManagerTicketDetailView detail(Long ticketId) {
        return details.getDetail(SecurityUtils.requireUserId(), ticketId, RoleCode.ADMIN);
    }

    // Chuyển ticket sang Manager khác với lý do.
    @Transactional
    public void reassign(Long ticketId, Long toManagerId, String reason, Long adminId) {
        requireReason(reason);
        if (toManagerId == null) throw new vn.edu.hcmute.qaute.common.exception.BadRequestException("Vui lòng chọn tư vấn viên");
        Ticket before = tickets.findById(ticketId).orElseThrow(vn.edu.hcmute.qaute.common.exception.NotFoundException::new);
        String old = state(before, reason);
        Ticket after = workflow.assign(ticketId, toManagerId, adminId, reason);
        audit.log(AuditActions.TICKET_REASSIGNED, "ticket", ticketId, old, state(after, reason));
    }

    // Đổi mức ưu tiên ticket theo yêu cầu quản trị.
    @Transactional
    public void changePriority(Long ticketId, TicketPriority priority, String reason, Long adminId) {
        requireReason(reason);
        Ticket before = tickets.findById(ticketId).orElseThrow(vn.edu.hcmute.qaute.common.exception.NotFoundException::new);
        String old = state(before, reason);
        Ticket after = workflow.changePriority(ticketId, adminId, priority, reason);
        audit.log(AuditActions.TICKET_PRIORITY_CHANGED, "ticket", ticketId, old, state(after, reason));
    }

    // Đổi chuyên mục ticket theo yêu cầu quản trị.
    @Transactional
    public void changeCategory(Long ticketId, Long categoryId, String reason, Long adminId) {
        requireReason(reason);
        Ticket before = tickets.findById(ticketId).orElseThrow(vn.edu.hcmute.qaute.common.exception.NotFoundException::new);
        String old = state(before, reason);
        Ticket after = workflow.changeCategory(ticketId, adminId, categoryId, reason);
        audit.log(AuditActions.TICKET_CATEGORY_CHANGED, "ticket", ticketId, old, state(after, reason));
    }

    // Đóng ticket cưỡng chế với lý do quản trị.
    @Transactional
    public void forceClose(Long ticketId, String reason, Long adminId) {
        requireReason(reason);
        Ticket before = tickets.findById(ticketId).orElseThrow(vn.edu.hcmute.qaute.common.exception.NotFoundException::new);
        String old = state(before, reason);
        Ticket after = workflow.forceClose(ticketId, adminId, reason);
        audit.log(AuditActions.TICKET_FORCE_CLOSED, "ticket", ticketId, old, state(after, reason));
    }

    // Kiểm tra lý do thao tác quản trị.
    private static void requireReason(String reason) {
        if (reason == null || reason.trim().length() < 10 || reason.trim().length() > 500)
            throw new vn.edu.hcmute.qaute.common.exception.BadRequestException("Lý do phải từ 10 đến 500 ký tự");
    }

    // Tạo dữ liệu trạng thái ticket để ghi nhật ký.
    private static String state(Ticket ticket, String reason) {
        return "status=" + ticket.getStatus() + ", priority=" + ticket.getPriority()
                + ", categoryId=" + ticket.getCategoryId() + ", assigneeId="
                + ticket.getCurrentAssigneeId() + ", reason=" + reason;
    }

    // Lấy tên người dùng hoặc giá trị thay thế khi thiếu.
    private static String name(Map<Long, UserSummary> users, Long id) {
        UserSummary user = id == null ? null : users.get(id);
        return user == null ? "—" : user.fullName();
    }
}
