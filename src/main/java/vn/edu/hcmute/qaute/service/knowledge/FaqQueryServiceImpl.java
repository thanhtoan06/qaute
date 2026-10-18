package vn.edu.hcmute.qaute.service.knowledge;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import vn.edu.hcmute.qaute.common.constant.FaqSort;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.dto.request.FaqFilter;
import vn.edu.hcmute.qaute.dto.view.CategoryView;
import vn.edu.hcmute.qaute.dto.view.FaqDetailView;
import vn.edu.hcmute.qaute.dto.view.FaqListItem;
import vn.edu.hcmute.qaute.dto.view.FaqSuggestion;
import vn.edu.hcmute.qaute.dto.view.TagView;
import vn.edu.hcmute.qaute.entity.knowledge.Faq;
import vn.edu.hcmute.qaute.entity.knowledge.FaqTag;
import vn.edu.hcmute.qaute.repository.knowledge.FaqRepository;
import vn.edu.hcmute.qaute.repository.knowledge.FaqTagRepository;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@Service
@Transactional(readOnly = true)
public class FaqQueryServiceImpl implements FaqQueryService {

    private final FaqRepository faqRepository;
    private final FaqTagRepository faqTagRepository;
    private final CategoryQueryService categoryQueryService;

    public FaqQueryServiceImpl(FaqRepository faqRepository,
                               FaqTagRepository faqTagRepository,
                               CategoryQueryService categoryQueryService) {
        this.faqRepository = faqRepository;
        this.faqTagRepository = faqTagRepository;
        this.categoryQueryService = categoryQueryService;
    }

    @Override
    public Page<FaqListItem> list(FaqFilter filter, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        Specification<Faq> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("status"), FaqStatus.PUBLISHED));
            predicates.add(cb.or(
                    cb.isNull(root.get("publishAt")),
                    cb.lessThanOrEqualTo(root.get("publishAt"), now)
            ));

            if (filter != null) {
                if (filter.getCategoryIds() != null && !filter.getCategoryIds().isEmpty()) {
                    predicates.add(root.get("categoryId").in(filter.getCategoryIds()));
                }
                if (filter.getTagSlug() != null && !filter.getTagSlug().isBlank()) {
                    Join<Faq, FaqTag> tagJoin = root.join("tags", JoinType.INNER);
                    predicates.add(cb.equal(tagJoin.get("slug"), filter.getTagSlug().trim()));
                    if (query != null) {
                        query.distinct(true);
                    }
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Pageable effectivePageable = resolvePageable(filter, pageable);
        return faqRepository.findAll(spec, effectivePageable).map(this::toListItem);
    }

    @Override
    public FaqDetailView getPublishedBySlug(String slug) {
        Faq faq = faqRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy FAQ"));

        LocalDateTime now = LocalDateTime.now();
        if (faq.getStatus() != FaqStatus.PUBLISHED
                || (faq.getPublishAt() != null && faq.getPublishAt().isAfter(now))) {
            throw new NotFoundException("Không tìm thấy FAQ");
        }

        String categoryPath = categoryQueryService.getDisplayPath(faq.getCategoryId());
        List<TagView> tagViews = faq.getTags().stream()
                .map(t -> new TagView(t.getName(), t.getSlug(), 0L))
                .toList();

        return new FaqDetailView(
                faq.getId(),
                faq.getTitle(),
                faq.getSlug(),
                faq.getSummary(),
                faq.getBodyHtml(),
                faq.getCategoryId(),
                categoryPath,
                faq.getViewCount(),
                faq.getHelpfulCount(),
                faq.getNotHelpfulCount(),
                faq.getUpdatedAt(),
                tagViews
        );
    }

    @Override
    public List<FaqListItem> topViewed(long minViews, int limit) {
        LocalDateTime now = LocalDateTime.now();
        Specification<Faq> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("status"), FaqStatus.PUBLISHED),
                cb.or(cb.isNull(root.get("publishAt")), cb.lessThanOrEqualTo(root.get("publishAt"), now)),
                cb.greaterThan(root.get("viewCount"), minViews)
        );
        Pageable pageable = PageRequest.of(0, Math.max(1, limit), Sort.by(Sort.Direction.DESC, "viewCount"));
        return faqRepository.findAll(spec, pageable).map(this::toListItem).getContent();
    }

    @Override
    public List<FaqListItem> featured(int limit) {
        LocalDateTime now = LocalDateTime.now();
        Specification<Faq> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("status"), FaqStatus.PUBLISHED),
                cb.or(cb.isNull(root.get("publishAt")), cb.lessThanOrEqualTo(root.get("publishAt"), now)),
                cb.isTrue(root.get("featured"))
        );
        Pageable pageable = PageRequest.of(0, Math.max(1, limit), Sort.by(Sort.Direction.DESC, "updatedAt"));
        return faqRepository.findAll(spec, pageable).map(this::toListItem).getContent();
    }

    @Override
    public List<FaqListItem> latest(int limit) {
        LocalDateTime now = LocalDateTime.now();
        Specification<Faq> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("status"), FaqStatus.PUBLISHED),
                cb.or(cb.isNull(root.get("publishAt")), cb.lessThanOrEqualTo(root.get("publishAt"), now))
        );
        Pageable pageable = PageRequest.of(0, Math.max(1, limit), Sort.by(Sort.Direction.DESC, "updatedAt"));
        return faqRepository.findAll(spec, pageable).map(this::toListItem).getContent();
    }

    @Override
    public List<FaqListItem> related(Long faqId, int limit) {
        if (faqId == null) {
            return List.of();
        }
        Faq baseFaq = faqRepository.findById(faqId).orElse(null);
        if (baseFaq == null) {
            return List.of();
        }

        LocalDateTime now = LocalDateTime.now();
        Set<Long> tagIds = baseFaq.getTags().stream().map(FaqTag::getId).collect(Collectors.toSet());

        Specification<Faq> spec = (root, query, cb) -> {
            List<Predicate> matchPredicates = new ArrayList<>();
            matchPredicates.add(cb.equal(root.get("categoryId"), baseFaq.getCategoryId()));

            if (!tagIds.isEmpty()) {
                Join<Faq, FaqTag> tagJoin = root.join("tags", JoinType.LEFT);
                matchPredicates.add(tagJoin.get("id").in(tagIds));
            }

            if (query != null) {
                query.distinct(true);
            }

            return cb.and(
                    cb.notEqual(root.get("id"), faqId),
                    cb.equal(root.get("status"), FaqStatus.PUBLISHED),
                    cb.or(cb.isNull(root.get("publishAt")), cb.lessThanOrEqualTo(root.get("publishAt"), now)),
                    cb.or(matchPredicates.toArray(new Predicate[0]))
            );
        };

        Sort sort = Sort.by(Sort.Direction.DESC, "helpfulCount").and(Sort.by(Sort.Direction.DESC, "viewCount"));
        Pageable pageable = PageRequest.of(0, Math.max(1, limit), sort);
        return faqRepository.findAll(spec, pageable).map(this::toListItem).getContent();
    }

    @Override
    public List<TagView> popularTags(int limit) {
        Pageable pageable = PageRequest.of(0, Math.max(1, limit));
        return faqTagRepository.findPopularTags(LocalDateTime.now(), pageable).stream()
                .map(row -> new TagView(
                        (String) row[0],
                        (String) row[1],
                        ((Number) row[2]).longValue()
                ))
                .toList();
    }

    @Override
    public long countPublished() {
        LocalDateTime now = LocalDateTime.now();
        Specification<Faq> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("status"), FaqStatus.PUBLISHED),
                cb.or(cb.isNull(root.get("publishAt")), cb.lessThanOrEqualTo(root.get("publishAt"), now))
        );
        return faqRepository.count(spec);
    }

    @Override
    public Map<Long, Long> countPublishedByCategory() {
        Map<Long, Long> counts = new LinkedHashMap<>();
        for (Object[] row : faqRepository.countPublishedByCategory(LocalDateTime.now())) {
            Long categoryId = (Long) row[0];
            Long count = ((Number) row[1]).longValue();
            counts.put(categoryId, count);
        }
        return counts;
    }

    @Override
    public List<FaqSuggestion> suggest(String query, int limit) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        String q = TextUtil.normalizeVi(query);
        if (q.length() < 2) {
            return List.of();
        }
        int maxLimit = Math.min(limit > 0 ? limit : 10, 10);
        String escaped = TextUtil.escapeLike(q);
        String prefix = escaped + "%";
        String contains = "%" + escaped + "%";
        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, maxLimit);

        List<Faq> faqs = faqRepository.suggestFaqs(prefix, contains, now, pageable);
        return faqs.stream()
                .map(faq -> new FaqSuggestion(
                        faq.getId(),
                        faq.getTitle(),
                        faq.getSlug(),
                        categoryQueryService.getDisplayPath(faq.getCategoryId())
                ))
                .toList();
    }

    private Pageable resolvePageable(FaqFilter filter, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt");
        if (filter != null && filter.getSort() != null) {
            sort = switch (filter.getSort()) {
                case LATEST -> Sort.by(Sort.Direction.DESC, "updatedAt");
                case MOST_VIEWED -> Sort.by(Sort.Direction.DESC, "viewCount");
                case MOST_HELPFUL -> Sort.by(Sort.Direction.DESC, "helpfulCount");
                case RELEVANCE -> pageable != null && pageable.getSort().isSorted()
                        ? pageable.getSort()
                        : Sort.by(Sort.Direction.DESC, "updatedAt");
            };
        } else if (pageable != null && pageable.getSort().isSorted()) {
            sort = pageable.getSort();
        }

        int page = pageable != null ? pageable.getPageNumber() : 0;
        int size = pageable != null ? pageable.getPageSize() : 10;
        return PageRequest.of(page, size, sort);
    }

    private FaqListItem toListItem(Faq faq) {
        String categoryName = categoryQueryService.findCategory(faq.getCategoryId())
                .map(CategoryView::name)
                .orElse("");
        List<String> tags = faq.getTags().stream()
                .map(FaqTag::getName)
                .sorted()
                .toList();

        return new FaqListItem(
                faq.getId(),
                faq.getTitle(),
                faq.getSlug(),
                faq.getSummary(),
                faq.getCategoryId(),
                categoryName,
                faq.getViewCount(),
                faq.getHelpfulCount(),
                faq.getNotHelpfulCount(),
                faq.getUpdatedAt(),
                tags
        );
    }
}

