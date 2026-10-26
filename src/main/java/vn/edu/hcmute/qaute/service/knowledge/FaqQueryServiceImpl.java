package vn.edu.hcmute.qaute.service.knowledge;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import vn.edu.hcmute.qaute.entity.knowledge.FaqBookmark;
import vn.edu.hcmute.qaute.entity.knowledge.FaqTag;
import vn.edu.hcmute.qaute.repository.knowledge.FaqBookmarkRepository;
import vn.edu.hcmute.qaute.repository.knowledge.FaqFeedbackRepository;
import vn.edu.hcmute.qaute.repository.knowledge.FaqRepository;
import vn.edu.hcmute.qaute.repository.knowledge.FaqTagRepository;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

// Xử lý nghiệp vụ FaqQuery trong hệ thống.
@Service
@Transactional(readOnly = true)
public class FaqQueryServiceImpl implements FaqQueryService {

    private final FaqRepository faqRepository;
    private final FaqTagRepository faqTagRepository;
    private final FaqBookmarkRepository faqBookmarkRepository;
    private final FaqFeedbackRepository faqFeedbackRepository;
    private final CategoryQueryService categoryQueryService;

    // Khởi tạo dịch vụ truy vấn FAQ và các kho dữ liệu liên quan.
    public FaqQueryServiceImpl(FaqRepository faqRepository,
                               FaqTagRepository faqTagRepository,
                               FaqBookmarkRepository faqBookmarkRepository,
                               FaqFeedbackRepository faqFeedbackRepository,
                               CategoryQueryService categoryQueryService) {
        this.faqRepository = faqRepository;
        this.faqTagRepository = faqTagRepository;
        this.faqBookmarkRepository = faqBookmarkRepository;
        this.faqFeedbackRepository = faqFeedbackRepository;
        this.categoryQueryService = categoryQueryService;
    }

    @Override
    // Lấy danh sách FAQ đã xuất bản theo bộ lọc.
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
                if (filter.getQuery() != null && !filter.getQuery().isBlank()) {
                    String normalizedQuery = TextUtil.normalizeVi(filter.getQuery().trim());
                    predicates.add(cb.or(
                            cb.like(root.get("titleNorm"), "%" + normalizedQuery + "%"),
                            cb.like(root.get("bodyNorm"), "%" + normalizedQuery + "%")
                    ));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Pageable effectivePageable = resolvePageable(filter, pageable);
        return faqRepository.findAll(spec, effectivePageable).map(this::toListItem);
    }

    @Override
    // Lấy danh sách FAQ người dùng đã đánh dấu.
    public Page<FaqListItem> listBookmarked(Long userId, Pageable pageable) {
        if (userId == null) return Page.empty(pageable == null ? Pageable.unpaged() : pageable);
        Pageable effectivePageable = pageable == null ? PageRequest.of(0, 20) : pageable;
        Page<FaqBookmark> bookmarkPage = faqBookmarkRepository.findByUserIdOrderByIdDesc(userId, effectivePageable);
        if (bookmarkPage.isEmpty()) return new PageImpl<>(List.of(), effectivePageable, bookmarkPage.getTotalElements());

        List<Long> faqIds = bookmarkPage.getContent().stream().map(FaqBookmark::getFaqId).toList();
        Map<Long, Faq> faqsById = faqRepository.findAllById(faqIds).stream()
                .filter(this::isPublishedNow)
                .collect(Collectors.toMap(Faq::getId, faq -> faq));
        List<FaqListItem> items = faqIds.stream().map(faqsById::get).filter(java.util.Objects::nonNull)
                .map(this::toListItem).toList();
        return new PageImpl<>(items, effectivePageable, bookmarkPage.getTotalElements());
    }

    @Override
    // Lấy mã các FAQ đã được người dùng đánh dấu.
    public Set<Long> bookmarkedIds(Long userId, Collection<Long> faqIds) {
        if (userId == null || faqIds == null || faqIds.isEmpty()) return Set.of();
        return faqBookmarkRepository.findByUserIdAndFaqIdIn(userId, faqIds).stream()
                .map(FaqBookmark::getFaqId).collect(Collectors.toSet());
    }

    @Override
    // Lấy bình chọn của người dùng cho các FAQ đã cho.
    public Map<Long, Boolean> myVotes(Long userId, Collection<Long> faqIds) {
        if (userId == null || faqIds == null || faqIds.isEmpty()) return Map.of();
        Map<Long, Boolean> votes = new HashMap<>();
        faqFeedbackRepository.findByUserIdAndFaqIdIn(userId, faqIds)
                .forEach(feedback -> votes.put(feedback.getFaqId(), feedback.isHelpful()));
        return Map.copyOf(votes);
    }

    @Override
    // Lấy chi tiết FAQ đang xuất bản theo slug.
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
    // Lấy các FAQ có lượt xem vượt ngưỡng tối thiểu.
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
    // Lấy các FAQ có lượt đánh giá hữu ích cao nhất.
    public List<FaqListItem> topHelpful(int limit) {
        LocalDateTime now = LocalDateTime.now();
        Specification<Faq> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("status"), FaqStatus.PUBLISHED),
                cb.or(cb.isNull(root.get("publishAt")), cb.lessThanOrEqualTo(root.get("publishAt"), now)),
                cb.greaterThan(root.get("helpfulCount"), 0)
        );
        Sort sort = Sort.by(Sort.Direction.DESC, "helpfulCount")
                .and(Sort.by(Sort.Direction.DESC, "viewCount"));
        Pageable pageable = PageRequest.of(0, Math.max(1, limit), sort);
        return faqRepository.findAll(spec, pageable).map(this::toListItem).getContent();
    }

    @Override
    // Lấy danh sách FAQ nổi bật.
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
    // Lấy danh sách FAQ mới cập nhật.
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
    // Lấy các FAQ liên quan theo danh mục hoặc thẻ.
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
    // Lấy danh sách thẻ phổ biến.
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
    // Đếm số FAQ đang được xuất bản.
    public long countPublished() {
        LocalDateTime now = LocalDateTime.now();
        Specification<Faq> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("status"), FaqStatus.PUBLISHED),
                cb.or(cb.isNull(root.get("publishAt")), cb.lessThanOrEqualTo(root.get("publishAt"), now))
        );
        return faqRepository.count(spec);
    }

    @Override
    // Đếm số FAQ đang xuất bản theo danh mục.
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
    // Gợi ý FAQ phù hợp với truy vấn.
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
                        categoryQueryService.getDisplayPath(faq.getCategoryId()),
                        faq.getSummary(),
                        faq.getBodyHtml()
                ))
                .toList();
    }

    // Xác định phân trang và thứ tự sắp xếp cho danh sách FAQ.
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

    // Chuyển FAQ thành dữ liệu hiển thị trong danh sách.
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

    // Kiểm tra FAQ đang được xuất bản và đã đến thời điểm hiển thị.
    private boolean isPublishedNow(Faq faq) {
        return faq.getStatus() == FaqStatus.PUBLISHED
                && (faq.getPublishAt() == null || !faq.getPublishAt().isAfter(LocalDateTime.now()));
    }
}
