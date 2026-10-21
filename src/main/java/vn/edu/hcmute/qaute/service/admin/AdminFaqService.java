package vn.edu.hcmute.qaute.service.admin;

import vn.edu.hcmute.qaute.common.util.BoundedPages;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.ConflictException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.exception.InvalidTransitionException;
import vn.edu.hcmute.qaute.common.util.PageUtil;
import vn.edu.hcmute.qaute.common.util.HtmlSanitizerUtil;
import vn.edu.hcmute.qaute.common.util.SlugUtil;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.dto.request.admin.AdminFaqFilter;
import vn.edu.hcmute.qaute.dto.request.admin.FaqForm;
import vn.edu.hcmute.qaute.dto.view.AdminFaqListItem;
import vn.edu.hcmute.qaute.dto.view.AdminFaqReviewView;
import vn.edu.hcmute.qaute.dto.view.FaqRevisionView;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.knowledge.Faq;
import vn.edu.hcmute.qaute.entity.knowledge.FaqTag;
import vn.edu.hcmute.qaute.entity.governance.FaqRevision;
import vn.edu.hcmute.qaute.repository.governance.FaqRevisionRepository;
import vn.edu.hcmute.qaute.repository.knowledge.FaqRepository;
import vn.edu.hcmute.qaute.repository.knowledge.FaqTagRepository;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;
import vn.edu.hcmute.qaute.repository.governance.AdminFaqRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.knowledge.FaqContentSupport;
import vn.edu.hcmute.qaute.service.media.MediaService;
import vn.edu.hcmute.qaute.service.notification.NotificationService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@Service
@Transactional(readOnly = true)
public class AdminFaqService {
    public static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("updatedAt", "viewCount", "helpfulCount", "title");
    public static final String DEFAULT_SORT_FIELD = "updatedAt";

    private final AdminFaqRepository faqs;
    private final CategoryQueryService categories;
    private final UserRepository users;
    private final FaqRepository faqRepository;
    private final FaqTagRepository tagRepository;
    private final FaqRevisionRepository revisions;
    private final MediaAssetRepository assets;
    private final MediaService media;
    private final AuditService audit;
    private final ObjectProvider<NotificationService> notifications;
    private final TicketRepository tickets;

    public AdminFaqService(AdminFaqRepository faqs, CategoryQueryService categories,
                           UserRepository users, FaqRepository faqRepository,
                           FaqTagRepository tagRepository, FaqRevisionRepository revisions,
                           MediaAssetRepository assets, MediaService media, AuditService audit,
                           ObjectProvider<NotificationService> notifications,
                           TicketRepository tickets) {
        this.faqs = faqs;
        this.categories = categories;
        this.users = users;
        this.faqRepository = faqRepository;
        this.tagRepository = tagRepository;
        this.revisions = revisions;
        this.assets = assets;
        this.media = media;
        this.audit = audit;
        this.notifications = notifications;
        this.tickets = tickets;
    }

    public AdminFaqReviewView review(Long id) {
        Faq faq = requireFaq(id);
        String authorName = faq.getAuthorId() == null ? "—" : users.findById(faq.getAuthorId())
                .map(User::getFullName).orElse("—");
        List<AdminFaqReviewView.RevisionNote> notes = revisions.findByFaqIdOrderByVersionNoDesc(id)
                .stream().map(revision -> new AdminFaqReviewView.RevisionNote(
                        revision.getVersionNo(), revision.getChangeNote(),
                        revision.getChangedBy(), revision.getCreatedAt())).toList();
        return new AdminFaqReviewView(faq.getId(), faq.getTitle(), faq.getSlug(),
                faq.getSummary(), HtmlSanitizerUtil.sanitize(faq.getBodyHtml()), categories.getDisplayPath(faq.getCategoryId()),
                authorName, faq.getStatus(), faq.isFeatured(), faq.getPublishAt(),
                faq.getPublishedAt(), faq.getTags().stream().map(FaqTag::getName).sorted().toList(),
                notes);
    }

    public List<FaqRevisionView> revisionHistory(Long id) {
        requireFaq(id);
        List<FaqRevision> history = revisions.findByFaqIdOrderByVersionNoDesc(id);
        Set<Long> editorIds = history.stream().map(FaqRevision::getChangedBy)
                .filter(java.util.Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> editorNames = new HashMap<>();
        users.findAllById(editorIds).forEach(user -> editorNames.put(user.getId(), user.getFullName()));
        return history.stream().map(revision -> toRevisionView(revision, editorNames)).toList();
    }

    public Page<FaqRevisionView> revisionHistory(Long id, Pageable requested) {
        requireFaq(id);
        Page<FaqRevision> page = revisions.findByFaqIdOrderByVersionNoDesc(id, BoundedPages.of(requested));
        Map<Long, String> names = namesFor(page.stream().map(FaqRevision::getChangedBy)
                .filter(java.util.Objects::nonNull).distinct().toList());
        return page.map(item -> toRevisionView(item, names));
    }

    public FaqRevisionView revision(Long id, int versionNo) {
        requireFaq(id);
        FaqRevision item = revisions.findByFaqIdAndVersionNo(id, versionNo)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên bản FAQ"));
        String editor = item.getChangedBy() == null ? null : users.findById(item.getChangedBy())
                .map(User::getFullName).orElse(null);
        return toRevisionView(item, editor == null ? Map.of() : Map.of(item.getChangedBy(), editor));
    }

    @Transactional
    public void restoreRevision(Long id, int versionNo, Long actorId) {
        requireAdmin();
        Faq faq = requireFaq(id);
        if (faq.getStatus() == FaqStatus.ARCHIVED) {
            throw new InvalidTransitionException("Không thể khôi phục phiên bản của FAQ đã lưu trữ");
        }
        FaqRevision source = revisions.findByFaqIdAndVersionNo(id, versionNo)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên bản FAQ"));
        int previousVersion = faq.getVersionNo();
        faq.setTitle(source.getTitle());
        faq.setSummary(source.getSummary());
        faq.setBodyHtml(source.getBodyHtml());
        if (faq.getStatus() != FaqStatus.PUBLISHED) {
            faq.setSlug(uniqueSlug(source.getTitle(), id));
        }
        faq.setVersionNo(previousVersion + 1);
        FaqContentSupport.prepare(faq);
        flushTransition(faq);
        revisions.save(FaqRevision.builder().faqId(id).versionNo(faq.getVersionNo())
                .title(faq.getTitle()).summary(faq.getSummary()).bodyHtml(faq.getBodyHtml())
                .changedBy(actorId).changeNote("Khôi phục từ phiên bản " + versionNo).build());
        audit.log(AuditActions.FAQ_REVISION_RESTORED, "Faq", id,
                "v" + previousVersion, "v" + faq.getVersionNo() + " từ v" + versionNo);
    }

    private static FaqRevisionView toRevisionView(FaqRevision item, Map<Long, String> editorNames) {
        return new FaqRevisionView(item.getVersionNo(), item.getTitle(), item.getSummary(),
                HtmlSanitizerUtil.sanitize(item.getBodyHtml()), item.getChangedBy(),
                item.getChangedBy() == null ? "—" : editorNames.getOrDefault(item.getChangedBy(), "—"),
                item.getChangeNote(), item.getCreatedAt());
    }

    @Transactional
    public void submitForReview(Long id, Long actorId) {
        submitForReview(id, actorId, false);
    }

    @Transactional
    public void submitForReview(Long id, Long actorId, boolean confirmedNoPersonalInfo) {
        Faq faq = requireFaq(id);
        requireStatus(faq, FaqStatus.DRAFT);
        if (!actorId.equals(faq.getAuthorId()) && !SecurityUtils.hasRole(RoleCode.ADMIN)) {
            throw new InvalidTransitionException("Chỉ tác giả hoặc quản trị viên có thể gửi duyệt");
        }
        if (faq.getSourceTicketId() != null && !confirmedNoPersonalInfo) {
            throw new BadRequestException("Vui lòng xác nhận đã loại bỏ thông tin cá nhân trước khi gửi duyệt");
        }
        faq.setStatus(FaqStatus.PENDING_REVIEW);
        flushTransition(faq);
        NotificationService notification = notifications.getIfAvailable();
        if (notification != null) {
            Map<String, String> details = Map.of("faqId", id.toString(), "title", faq.getTitle(),
                    "link", "/admin/faq/" + id + "/review");
            for (Long adminId : users.findIdsByRoleCodeAndStatus(RoleCode.ADMIN, UserStatus.ACTIVE)) {
                notification.notifyUser(adminId, NotificationType.FAQ_REVIEW_REQUESTED, details);
            }
        }
        audit.log(AuditActions.FAQ_SUBMITTED, "Faq", id, "DRAFT", "PENDING_REVIEW");
    }

    @Transactional
    public void approve(Long id, Long reviewerId, LocalDateTime publishAt) {
        requireAdmin();
        Faq faq = requireFaq(id);
        requireStatus(faq, FaqStatus.PENDING_REVIEW);
        publish(faq, reviewerId, publishAt, FaqStatus.PENDING_REVIEW);
    }

    @Transactional
    public void publishDirect(Long id, Long adminId, LocalDateTime publishAt) {
        requireAdmin();
        Faq faq = requireFaq(id);
        requireStatus(faq, FaqStatus.DRAFT);
        publish(faq, adminId, publishAt, FaqStatus.DRAFT);
    }

    @Transactional
    public void reject(Long id, Long reviewerId, String reason) {
        requireAdmin();
        Faq faq = requireFaq(id);
        requireStatus(faq, FaqStatus.PENDING_REVIEW);
        String note = requireReason(reason);
        FaqRevision latest = revisions.findByFaqIdOrderByVersionNoDesc(id).stream().findFirst()
                .orElseGet(() -> FaqRevision.builder().faqId(id).versionNo(faq.getVersionNo())
                        .title(faq.getTitle()).summary(faq.getSummary()).bodyHtml(faq.getBodyHtml())
                        .changedBy(reviewerId).build());
        latest.setChangeNote(note);
        revisions.save(latest);
        faq.setStatus(FaqStatus.DRAFT);
        faq.setReviewerId(reviewerId);
        flushTransition(faq);
        notifyAuthor(faq, NotificationType.FAQ_REJECTED,
                Map.of("faqId", id.toString(), "title", faq.getTitle(), "reason", note));
        audit.log(AuditActions.FAQ_REJECTED, "Faq", id, "PENDING_REVIEW", "DRAFT: " + note);
    }

    @Transactional
    public void archive(Long id, Long adminId, String reason) {
        requireAdmin();
        Faq faq = requireFaq(id);
        requireStatus(faq, FaqStatus.PUBLISHED);
        String note = requireReason(reason);
        faq.setStatus(FaqStatus.ARCHIVED);
        flushTransition(faq);
        audit.log(AuditActions.FAQ_ARCHIVED, "Faq", id, "PUBLISHED", "ARCHIVED: " + note);
    }

    @Transactional
    public void restore(Long id, Long adminId) {
        requireAdmin();
        Faq faq = requireFaq(id);
        requireStatus(faq, FaqStatus.ARCHIVED);
        faq.setStatus(FaqStatus.DRAFT);
        faq.setReviewerId(null);
        faq.setPublishAt(null);
        faq.setPublishedAt(null);
        flushTransition(faq);
        audit.log(AuditActions.FAQ_RESTORED, "Faq", id, "ARCHIVED", "DRAFT");
    }

    @Transactional
    public void toggleFeatured(Long id, Long adminId) {
        requireAdmin();
        Faq faq = requireFaq(id);
        requireStatus(faq, FaqStatus.PUBLISHED);
        boolean before = faq.isFeatured();
        faq.setFeatured(!before);
        flushTransition(faq);
        audit.log(AuditActions.FAQ_FEATURED_CHANGED, "Faq", id,
                Boolean.toString(before), Boolean.toString(faq.isFeatured()));
    }

    private void publish(Faq faq, Long reviewerId, LocalDateTime requestedAt, FaqStatus from) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime scheduledAt = requestedAt != null && requestedAt.isAfter(now) ? requestedAt : null;
        faq.setStatus(FaqStatus.PUBLISHED);
        faq.setReviewerId(reviewerId);
        faq.setPublishAt(scheduledAt);
        faq.setPublishedAt(scheduledAt == null ? now : scheduledAt);
        flushTransition(faq);
        notifyAuthor(faq, NotificationType.FAQ_APPROVED,
                Map.of("faqId", faq.getId().toString(), "title", faq.getTitle()));
        if (faq.getSourceTicketId() != null) {
            NotificationService notification = notifications.getIfAvailable();
            if (notification != null) {
                tickets.findById(faq.getSourceTicketId()).ifPresent(ticket ->
                        notification.notifyUser(ticket.getStudentId(),
                                NotificationType.FAQ_FROM_TICKET_PUBLISHED,
                                Map.of("faqId", faq.getId().toString(), "title", faq.getTitle(),
                                        "link", "/faq/" + faq.getSlug())));
            }
        }
        audit.log(AuditActions.FAQ_PUBLISHED, "Faq", faq.getId(), from.name(),
                "PUBLISHED at " + (scheduledAt == null ? now : scheduledAt));
    }

    private void notifyAuthor(Faq faq, NotificationType type, Map<String, String> details) {
        NotificationService notification = notifications.getIfAvailable();
        if (notification != null && faq.getAuthorId() != null) {
            notification.notifyUser(faq.getAuthorId(), type, details);
        }
    }

    private Faq requireFaq(Long id) {
        return faqRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy FAQ"));
    }

    private static void requireStatus(Faq faq, FaqStatus expected) {
        if (faq.getStatus() != expected) {
            throw new InvalidTransitionException("FAQ phải ở trạng thái " + expected.name());
        }
    }

    private static void requireAdmin() {
        if (!SecurityUtils.hasRole(RoleCode.ADMIN)) {
            throw new InvalidTransitionException("Chỉ quản trị viên được thực hiện thao tác này");
        }
    }

    private static String requireReason(String reason) {
        String trimmed = reason == null ? "" : reason.trim();
        if (trimmed.length() < 10 || trimmed.length() > 500) {
            throw new BadRequestException("Lý do phải từ 10 đến 500 ký tự");
        }
        return trimmed;
    }

    private void flushTransition(Faq faq) {
        try {
            faqRepository.saveAndFlush(faq);
        } catch (OptimisticLockingFailureException | jakarta.persistence.OptimisticLockException e) {
            throw new ConflictException("FAQ đã được cập nhật đồng thời. Vui lòng tải lại trang");
        }
    }

    public FaqForm getForm(Long id) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy FAQ"));
        FaqForm form = new FaqForm();
        form.setId(faq.getId());
        form.setVersion(faq.getVersion());
        form.setTitle(faq.getTitle());
        form.setSummary(faq.getSummary());
        form.setCategoryId(faq.getCategoryId());
        form.setBodyHtml(faq.getBodyHtml());
        form.setTags(faq.getTags().stream().map(FaqTag::getName).sorted().collect(Collectors.joining(", ")));
        form.setFeatured(faq.isFeatured());
        form.setPublishAt(faq.getPublishAt());
        form.setImageAssetIds(assets.findByUsageTypeAndUsageRefId(MediaUsageType.FAQ_IMAGE, id)
                .stream().filter(asset -> asset.getStatus() == MediaStatus.ACTIVE)
                .map(asset -> asset.getId()).toList());
        return form;
    }

    @Transactional
    public Long save(FaqForm form, Long actorId) {
        validate(form);
        if (categories.findCategory(form.getCategoryId()).filter(category -> category.active()).isEmpty()) {
            throw new BadRequestException("Vui lòng chọn chuyên mục đang hoạt động",
                    Map.of("categoryId", "Chuyên mục không hoạt động hoặc không tồn tại"));
        }
        boolean creating = form.getId() == null;
        Faq faq = creating ? Faq.builder().status(FaqStatus.DRAFT).authorId(actorId)
                .versionNo(1).build() : faqRepository.findById(form.getId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy FAQ"));
        if (!creating) {
            if (faq.getStatus() == FaqStatus.ARCHIVED
                    || (faq.getStatus() != FaqStatus.DRAFT && !SecurityUtils.hasRole(RoleCode.ADMIN))) {
                throw new BadRequestException("Trạng thái bài viết không cho phép chỉnh sửa");
            }
            if (!SecurityUtils.hasRole(RoleCode.ADMIN) && !actorId.equals(faq.getAuthorId())) {
                throw new BadRequestException("Bạn chỉ có thể sửa bản nháp của mình");
            }
            if (form.getVersion() == null || !form.getVersion().equals(faq.getVersion())) {
                throw new ConflictException("Bài viết đã được người khác cập nhật. Vui lòng tải lại trang");
            }
        }
        String before = creating ? null : faq.getTitle() + " | v" + faq.getVersionNo();
        String title = form.getTitle().trim();
        if (creating || faq.getStatus() != FaqStatus.PUBLISHED) {
            faq.setSlug(uniqueSlug(title, creating ? null : faq.getId()));
        }
        faq.setTitle(title);
        faq.setSummary(blankToNull(form.getSummary()));
        faq.setCategoryId(form.getCategoryId());
        faq.setBodyHtml(form.getBodyHtml());
        faq.setFeatured(form.isFeatured());
        faq.setPublishAt(form.getPublishAt());
        faq.setTags(resolveTags(form.getTags()));
        if (!creating) faq.setVersionNo(faq.getVersionNo() + 1);
        FaqContentSupport.prepare(faq);
        if (faq.getBodyHtml().isBlank()) {
            throw new BadRequestException("Nội dung không hợp lệ", Map.of("bodyHtml", "Nội dung không được để trống"));
        }
        try {
            faqRepository.saveAndFlush(faq);
        } catch (OptimisticLockingFailureException | jakarta.persistence.OptimisticLockException e) {
            throw new ConflictException("Bài viết đã được người khác cập nhật. Vui lòng tải lại trang");
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Tiêu đề hoặc tag đã được tạo đồng thời. Vui lòng thử lại");
        }
        revisions.save(FaqRevision.builder().faqId(faq.getId()).versionNo(faq.getVersionNo())
                .title(faq.getTitle()).summary(faq.getSummary()).bodyHtml(faq.getBodyHtml())
                .changedBy(actorId).changeNote(blankToNull(form.getChangeNote())).build());
        if (form.getImageAssetIds() != null) {
            for (Long assetId : form.getImageAssetIds().stream().distinct().toList()) {
                if (assetId == null) throw new BadRequestException("Ảnh FAQ không hợp lệ");
                media.attach(assetId, MediaUsageType.FAQ_IMAGE, faq.getId());
            }
        }
        audit.log(creating ? AuditActions.FAQ_CREATED : AuditActions.FAQ_UPDATED,
                "Faq", faq.getId(), before, faq.getTitle() + " | v" + faq.getVersionNo());
        return faq.getId();
    }

    private String uniqueSlug(String title, Long currentId) {
        String base = SlugUtil.slugify(title);
        if (base.length() > 220) base = base.substring(0, 220).replaceAll("-+$", "");
        String candidate = base;
        for (int suffix = 2; slugExists(candidate, currentId); suffix++) {
            String ending = "-" + suffix;
            candidate = base.substring(0, Math.min(base.length(), 220 - ending.length())) + ending;
        }
        return candidate;
    }

    private boolean slugExists(String slug, Long currentId) {
        return currentId == null ? faqRepository.existsBySlug(slug)
                : faqRepository.existsBySlugAndIdNot(slug, currentId);
    }

    private Set<FaqTag> resolveTags(String input) {
        Set<FaqTag> result = new LinkedHashSet<>();
        if (input == null || input.isBlank()) return result;
        String[] entries = input.split(",", -1);
        if (entries.length > 8) {
            throw new BadRequestException("Tối đa 8 tag", Map.of("tags", "Tối đa 8 tag"));
        }
        Set<String> seen = new LinkedHashSet<>();
        for (String entry : entries) {
            String name = entry.trim();
            if (name.length() < 2 || name.length() > 40) {
                throw new BadRequestException("Tag phải từ 2 đến 40 ký tự",
                        Map.of("tags", "Mỗi tag phải từ 2 đến 40 ký tự"));
            }
            String slug = SlugUtil.slugify(name);
            if (!seen.add(slug)) continue;
            FaqTag tag = tagRepository.findBySlug(slug).orElseGet(() ->
                    tagRepository.save(FaqTag.builder().name(name)
                            .nameNorm(TextUtil.normalizeVi(name)).slug(slug).build()));
            result.add(tag);
        }
        return result;
    }

    private static void validate(FaqForm form) {
        if (form == null || form.getTitle() == null || form.getTitle().trim().length() < 10
                || form.getTitle().trim().length() > 200 || form.getCategoryId() == null
                || form.getBodyHtml() == null || form.getBodyHtml().isBlank()
                || (form.getSummary() != null && form.getSummary().length() > 500)
                || (form.getChangeNote() != null && form.getChangeNote().length() > 255)) {
            throw new BadRequestException("Vui lòng kiểm tra thông tin FAQ");
        }
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public Page<AdminFaqListItem> search(AdminFaqFilter filter, Pageable pageable) {
        AdminFaqFilter safeFilter = filter == null ? new AdminFaqFilter() : filter;
        List<Long> categoryIds = safeFilter.getCategoryId() == null ? null
                : categories.getDescendantIds(safeFilter.getCategoryId());
        Specification<Faq> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (safeFilter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), safeFilter.getStatus()));
            }
            if (categoryIds != null) {
                predicates.add(categoryIds.isEmpty() ? cb.disjunction()
                        : root.get("categoryId").in(categoryIds));
            }
            if (safeFilter.getAuthorId() != null) {
                predicates.add(cb.equal(root.get("authorId"), safeFilter.getAuthorId()));
            }
            if (safeFilter.getFeatured() != null) {
                predicates.add(cb.equal(root.get("featured"), safeFilter.getFeatured()));
            }
            if (safeFilter.getQ() != null && !safeFilter.getQ().isBlank()) {
                String pattern = "%" + TextUtil.escapeLike(safeFilter.getQ().trim())
                        .toLowerCase(Locale.ROOT) + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), pattern, '\\'),
                        cb.like(cb.lower(root.get("slug")), pattern, '\\')));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        int page = pageable == null ? 0 : pageable.getPageNumber();
        int size = pageable == null ? 15 : pageable.getPageSize();
        String sort = safeFilter.getSort();
        Pageable safePageable = PageUtil.of(page, size, sort, !"title".equals(sort),
                ALLOWED_SORT_FIELDS, DEFAULT_SORT_FIELD);
        Page<Faq> result = faqs.findAll(specification, safePageable);
        Map<Long, String> authorNames = namesFor(result.getContent().stream()
                .map(Faq::getAuthorId).filter(id -> id != null).distinct().toList());
        Map<Long, String> categoryPaths = new HashMap<>();
        return result.map(faq -> new AdminFaqListItem(
                faq.getId(), faq.getTitle(), faq.getSlug(),
                categoryPaths.computeIfAbsent(faq.getCategoryId(), categories::getDisplayPath),
                faq.getStatus(), faq.isFeatured(),
                authorNames.getOrDefault(faq.getAuthorId(), "—"),
                faq.getViewCount(), faq.getHelpfulCount(), faq.getNotHelpfulCount(),
                faq.getUpdatedAt(), faq.getPublishAt(), faq.getSourceTicketId()));
    }

    public Map<FaqStatus, Long> countByStatus() {
        Map<FaqStatus, Long> counts = new EnumMap<>(FaqStatus.class);
        for (FaqStatus status : FaqStatus.values()) {
            counts.put(status, 0L);
        }
        for (Object[] row : faqs.countGroupedByStatus()) {
            counts.put((FaqStatus) row[0], (Long) row[1]);
        }
        return counts;
    }

    public Map<Long, String> authors() {
        return namesFor(faqs.findDistinctAuthorIds()).entrySet().stream()
                .sorted(Map.Entry.comparingByValue(String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (first, second) -> first, LinkedHashMap::new));
    }

    private Map<Long, String> namesFor(List<Long> ids) {
        if (ids.isEmpty()) return Map.of();
        Map<Long, String> names = new HashMap<>();
        for (User user : users.findAllById(ids)) {
            names.put(user.getId(), user.getFullName());
        }
        return names;
    }
}
