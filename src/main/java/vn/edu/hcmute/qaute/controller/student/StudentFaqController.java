package vn.edu.hcmute.qaute.controller.student;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.hcmute.qaute.common.constant.FaqSort;
import vn.edu.hcmute.qaute.dto.request.FaqFilter;
import vn.edu.hcmute.qaute.dto.view.FaqListItem;
import vn.edu.hcmute.qaute.dto.view.SearchResultItem;
import vn.edu.hcmute.qaute.repository.knowledge.FaqViewRepository;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.knowledge.FaqQueryService;
import vn.edu.hcmute.qaute.service.knowledge.SearchService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@Controller
public class StudentFaqController {
    private static final int PAGE_SIZE = 20;
    private static final Set<String> TABS = Set.of("latest", "views", "helpful", "saved");

    private final FaqQueryService faqs;
    private final SearchService search;
    private final CategoryQueryService categories;
    private final FaqViewRepository views;

    public StudentFaqController(FaqQueryService faqs, SearchService search,
                                CategoryQueryService categories, FaqViewRepository views) {
        this.faqs = faqs;
        this.search = search;
        this.categories = categories;
        this.views = views;
    }

    @GetMapping("/student/faq")
    public String list(@RequestParam(required = false) String tab,
                       @RequestParam(required = false) Long category,
                       @RequestParam(required = false) String q,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        Long userId = SecurityUtils.requireUserId();
        String activeTab = normalizeTab(tab);
        String query = q == null ? "" : q.trim();
        List<Long> categoryIds = categoryIds(category);
        Pageable pageable = PageRequest.of(Math.max(0, page), PAGE_SIZE);

        Page<FaqListItem> faqPage;
        if (!query.isEmpty()) {
            Page<SearchResultItem> results = search.search(query, categoryIds, null, FaqSort.RELEVANCE,
                    pageable, userId, null, "STUDENT_FAQ");
            faqPage = new PageImpl<>(results.getContent().stream().map(StudentFaqController::toListItem).toList(), pageable,
                    results.getTotalElements());
        } else if ("saved".equals(activeTab)) {
            faqPage = faqs.listBookmarked(userId, pageable);
        } else {
            FaqFilter filter = FaqFilter.builder().categoryIds(categoryIds).sort(sortFor(activeTab)).build();
            faqPage = faqs.list(filter, pageable);
        }

        List<Long> faqIds = faqPage.getContent().stream().map(FaqListItem::id).toList();
        Set<Long> bookmarkedIds = faqs.bookmarkedIds(userId, faqIds);
        Set<Long> viewedIds = views.findByUserIdAndFaqIdIn(userId, faqIds).stream()
                .map(view -> view.getFaqId()).collect(java.util.stream.Collectors.toSet());

        model.addAttribute("page", faqPage);
        model.addAttribute("tab", activeTab);
        model.addAttribute("category", category);
        model.addAttribute("q", query);
        model.addAttribute("tree", categories.getActiveTree());
        model.addAttribute("bookmarkedIds", bookmarkedIds);
        model.addAttribute("myVotes", faqs.myVotes(userId, faqIds));
        model.addAttribute("viewedIds", viewedIds);
        model.addAttribute("queryString", queryString(activeTab, category, query));
        return "student/faq-list";
    }

    private List<Long> categoryIds(Long category) {
        if (category == null) return null;
        List<Long> descendants = categories.getDescendantIds(category);
        return descendants == null || descendants.isEmpty() ? List.of(category) : descendants;
    }

    private static String normalizeTab(String tab) {
        if (tab == null) return "latest";
        String value = tab.trim().toLowerCase(Locale.ROOT);
        return TABS.contains(value) ? value : "latest";
    }

    private static FaqSort sortFor(String tab) {
        return switch (tab) {
            case "views" -> FaqSort.MOST_VIEWED;
            case "helpful" -> FaqSort.MOST_HELPFUL;
            default -> FaqSort.LATEST;
        };
    }

    private static FaqListItem toListItem(SearchResultItem item) {
        return new FaqListItem(item.id(), item.title(), item.slug(), item.summary(), null,
                item.categoryName(), item.viewCount(), item.helpfulCount(), 0, item.updatedAt(), List.of());
    }

    private static String queryString(String tab, Long category, String q) {
        List<String> params = new ArrayList<>();
        params.add("tab=" + tab);
        if (category != null) params.add("category=" + category);
        if (q != null && !q.isBlank()) params.add("q=" + URLEncoder.encode(q, StandardCharsets.UTF_8));
        return String.join("&", params);
    }
}
