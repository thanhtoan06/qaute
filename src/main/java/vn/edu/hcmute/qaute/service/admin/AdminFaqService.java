package vn.edu.hcmute.qaute.service.admin;

import jakarta.persistence.criteria.Predicate;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.ConflictException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.util.PageUtil;
import vn.edu.hcmute.qaute.common.util.SlugUtil;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.dto.request.admin.AdminFaqFilter;
import vn.edu.hcmute.qaute.dto.request.admin.FaqForm;
import vn.edu.hcmute.qaute.dto.view.AdminFaqListItem;
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
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.knowledge.FaqContentSupport;
import vn.edu.hcmute.qaute.service.media.MediaService;
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

    public AdminFaqService(AdminFaqRepository faqs, CategoryQueryService categories,
                           UserRepository users, FaqRepository faqRepository,
                           FaqTagRepository tagRepository, FaqRevisionRepository revisions,
                           MediaAssetRepository assets, MediaService media, AuditService audit) {
        this.faqs = faqs;
        this.categories = categories;
        this.users = users;
        this.faqRepository = faqRepository;
        this.tagRepository = tagRepository;
        this.revisions = revisions;
        this.assets = assets;
        this.media = media;
        this.audit = audit;
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
                faq.getUpdatedAt(), faq.getPublishAt()));
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
