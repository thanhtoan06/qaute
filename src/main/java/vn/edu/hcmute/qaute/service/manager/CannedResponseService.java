package vn.edu.hcmute.qaute.service.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import vn.edu.hcmute.qaute.common.constant.CannedResponseScope;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.util.HtmlSanitizerUtil;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.dto.request.manager.CannedResponseForm;
import vn.edu.hcmute.qaute.dto.view.CannedResponseOption;
import vn.edu.hcmute.qaute.dto.view.CannedResponseView;
import vn.edu.hcmute.qaute.dto.view.StudentSummary;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.manager.CannedResponse;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.manager.CannedResponseRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.ticket.TicketAccessService;

@Service
@Transactional
public class CannedResponseService {
    private static final List<String> VARIABLES = List.of("student_name", "ticket_code",
            "advisor_name", "category");
    private final CannedResponseRepository repository;
    private final ManagerScopeService scopes;
    private final TicketAccessService ticketAccess;
    private final UserLookupService users;
    private final CategoryQueryService categories;

    public CannedResponseService(CannedResponseRepository repository, ManagerScopeService scopes,
            TicketAccessService ticketAccess, UserLookupService users,
            CategoryQueryService categories) {
        this.repository = repository;
        this.scopes = scopes;
        this.ticketAccess = ticketAccess;
        this.users = users;
        this.categories = categories;
    }

    @Transactional(readOnly = true)
    public List<CannedResponseView> list(Long managerId) {
        List<CannedResponse> visible = visible(managerId);
        Map<Long, String> paths = new LinkedHashMap<>();
        visible.stream().map(CannedResponse::getCategoryId).filter(id -> id != null)
                .distinct().forEach(id -> paths.put(id, categories.getDisplayPath(id)));
        return visible.stream().map(item -> new CannedResponseView(item.getId(), item.getTitle(),
                sanitizeTemplate(item.getBodyHtml()), item.getScope(), item.getCategoryId(),
                paths.getOrDefault(item.getCategoryId(), ""), item.isActive(), item.getUsageCount(),
                managerId.equals(item.getOwnerId()))).toList();
    }

    @Transactional(readOnly = true)
    public List<CannedResponseOption> listActive(Long managerId) {
        return visible(managerId).stream().filter(CannedResponse::isActive)
                .map(item -> new CannedResponseOption(item.getId(), item.getTitle(), item.getScope()))
                .toList();
    }

    @Transactional(readOnly = true)
    public CannedResponseForm getForm(Long id, Long managerId) {
        CannedResponse item = owned(id, managerId);
        CannedResponseForm form = new CannedResponseForm();
        form.setId(item.getId());
        form.setTitle(item.getTitle());
        form.setBodyHtml(item.getBodyHtml());
        form.setScope(item.getScope());
        form.setCategoryId(item.getCategoryId());
        form.setActive(item.isActive());
        return form;
    }

    public Long save(CannedResponseForm form, Long managerId) {
        if (form == null) throw new BadRequestException("Mẫu trả lời không hợp lệ");
        String title = form.getTitle() == null ? "" : form.getTitle().trim();
        if (title.length() < 2 || title.length() > 120) {
            throw new BadRequestException("Tiêu đề phải từ 2 đến 120 ký tự");
        }
        String rawHtml = form.getBodyHtml();
        if (rawHtml == null || rawHtml.length() > 10000) {
            throw new BadRequestException("Nội dung mẫu tối đa 10000 ký tự");
        }
        String safeHtml = sanitizeTemplate(rawHtml);
        if (TextUtil.stripHtml(safeHtml).isBlank()) {
            throw new BadRequestException("Vui lòng nhập nội dung mẫu");
        }
        CannedResponseScope scope = form.getScope();
        if (scope == null) throw new BadRequestException("Vui lòng chọn phạm vi mẫu");
        Long categoryId = null;
        if (scope == CannedResponseScope.SHARED) {
            categoryId = form.getCategoryId();
            List<Long> scopeIds = scopes.getScopeCategoryIds(managerId);
            if (categoryId == null || scopeIds == null || !scopeIds.contains(categoryId)) {
                throw new ForbiddenException("Chuyên mục không thuộc phạm vi của bạn");
            }
        }
        CannedResponse item = form.getId() == null ? CannedResponse.builder()
                .ownerId(managerId).usageCount(0).build() : owned(form.getId(), managerId);
        item.setTitle(title);
        item.setBodyHtml(safeHtml);
        item.setScope(scope);
        item.setCategoryId(categoryId);
        item.setActive(form.isActive());
        return repository.save(item).getId();
    }

    public void delete(Long id, Long managerId) {
        repository.delete(owned(id, managerId));
    }

    public String render(Long templateId, Long managerId, Long ticketId) {
        Ticket ticket = ticketAccess.requireViewable(ticketId, managerId, RoleCode.MANAGER);
        CannedResponse item = repository.findById(templateId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy mẫu trả lời"));
        if (!item.isActive() || !canView(item, managerId, scopes.getScopeCategoryIds(managerId))) {
            throw new NotFoundException("Không tìm thấy mẫu trả lời");
        }
        StudentSummary student = users.getStudentSummary(ticket.getStudentId());
        UserSummary advisor = users.getSummary(managerId);
        String html = sanitizeTemplate(item.getBodyHtml())
                .replace("{{student_name}}", escape(student.user().fullName()))
                .replace("{{ticket_code}}", escape(ticket.getCode()))
                .replace("{{advisor_name}}", escape(advisor.fullName()))
                .replace("{{category}}", escape(categories.getDisplayPath(ticket.getCategoryId())));
        repository.incrementUsageCount(item.getId());
        return html;
    }

    private List<CannedResponse> visible(Long managerId) {
        List<Long> scopeIds = scopes.getScopeCategoryIds(managerId);
        Map<Long, CannedResponse> candidates = new LinkedHashMap<>();
        repository.findByOwnerIdAndScopeOrderByTitleAsc(managerId, CannedResponseScope.PERSONAL)
                .forEach(item -> candidates.put(item.getId(), item));
        if (scopeIds != null && !scopeIds.isEmpty()) {
            repository.findByScopeAndCategoryIdInOrderByTitleAsc(CannedResponseScope.SHARED, scopeIds)
                    .forEach(item -> candidates.put(item.getId(), item));
        }
        List<CannedResponse> global = repository.findByScopeAndCategoryIdIsNullOrderByTitleAsc(
                CannedResponseScope.SHARED);
        Set<Long> ownerIds = global.stream().map(CannedResponse::getOwnerId).collect(Collectors.toSet());
        Map<Long, UserSummary> owners = ownerIds.isEmpty() ? Map.of() : users.getSummaries(ownerIds);
        for (CannedResponse item : global) {
            UserSummary owner = owners.get(item.getOwnerId());
            if (owner != null && owner.role() == RoleCode.ADMIN) candidates.put(item.getId(), item);
        }
        List<CannedResponse> result = new ArrayList<>();
        for (CannedResponse item : candidates.values()) {
            if (item.isActive() || managerId.equals(item.getOwnerId())) result.add(item);
        }
        result.sort(Comparator.comparing(CannedResponse::getTitle, String.CASE_INSENSITIVE_ORDER));
        return result;
    }

    private boolean canView(CannedResponse item, Long managerId, List<Long> scopeIds) {
        if (item.getScope() == CannedResponseScope.PERSONAL) {
            return managerId.equals(item.getOwnerId());
        }
        if (item.getScope() != CannedResponseScope.SHARED) return false;
        if (item.getCategoryId() != null) {
            return scopeIds != null && scopeIds.contains(item.getCategoryId());
        }
        UserSummary owner = users.getSummary(item.getOwnerId());
        return owner.role() == RoleCode.ADMIN;
    }

    private CannedResponse owned(Long id, Long managerId) {
        CannedResponse item = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy mẫu trả lời"));
        if (!managerId.equals(item.getOwnerId())) {
            throw new ForbiddenException("Chỉ chủ sở hữu được sửa mẫu trả lời");
        }
        return item;
    }

    private static String escape(String value) {
        return HtmlUtils.htmlEscape(value == null ? "" : value);
    }

    private static String sanitizeTemplate(String html) {
        Map<String, String> placeholders = new LinkedHashMap<>();
        String masked = html;
        for (String variable : VARIABLES) {
            String placeholder = "QAUTEPLACEHOLDER" + UUID.randomUUID().toString().replace("-", "");
            placeholders.put(placeholder, "{{" + variable + "}}");
            masked = masked.replace("{{" + variable + "}}", placeholder);
        }
        String safe = HtmlSanitizerUtil.sanitize(masked);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            safe = safe.replace(entry.getKey(), entry.getValue());
        }
        return safe;
    }
}
