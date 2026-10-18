package vn.edu.hcmute.qaute.service.admin;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;
import vn.edu.hcmute.qaute.common.util.PageUtil;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.dto.request.admin.AdminFaqFilter;
import vn.edu.hcmute.qaute.dto.view.AdminFaqListItem;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.knowledge.Faq;
import vn.edu.hcmute.qaute.repository.governance.AdminFaqRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
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

    public AdminFaqService(AdminFaqRepository faqs, CategoryQueryService categories,
                           UserRepository users) {
        this.faqs = faqs;
        this.categories = categories;
        this.users = users;
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
