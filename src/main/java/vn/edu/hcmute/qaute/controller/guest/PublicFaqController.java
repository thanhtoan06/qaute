package vn.edu.hcmute.qaute.controller.guest;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.hcmute.qaute.common.constant.FaqSort;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.util.TokenUtil;
import vn.edu.hcmute.qaute.common.web.ClientIpResolver;
import vn.edu.hcmute.qaute.dto.request.FaqFilter;
import vn.edu.hcmute.qaute.dto.view.CategoryView;
import vn.edu.hcmute.qaute.dto.view.FaqDetailView;
import vn.edu.hcmute.qaute.dto.view.FaqListItem;
import vn.edu.hcmute.qaute.dto.view.TagView;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.knowledge.FaqQueryService;
import vn.edu.hcmute.qaute.service.knowledge.FaqPersonalService;
import vn.edu.hcmute.qaute.service.knowledge.FaqViewCounter;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho PublicFaq.
@Controller
public class PublicFaqController {

    private static final int PAGE_SIZE = 12;

    private final FaqQueryService faqQueryService;
    private final CategoryQueryService categoryQueryService;
    private final FaqViewCounter faqViewCounter;
    private final FaqPersonalService faqPersonalService;

    // Khởi tạo controller xử lý câu hỏi thường gặp công khai.
    public PublicFaqController(FaqQueryService faqQueryService,
                               CategoryQueryService categoryQueryService,
                               FaqViewCounter faqViewCounter,
                               FaqPersonalService faqPersonalService) {
        this.faqQueryService = faqQueryService;
        this.categoryQueryService = categoryQueryService;
        this.faqViewCounter = faqViewCounter;
        this.faqPersonalService = faqPersonalService;
    }

    // Hiển thị danh sách câu hỏi thường gặp theo bộ lọc.
    @GetMapping("/faq")
    public String listFaqs(@RequestParam(required = false) Long category,
                           @RequestParam(required = false) String tag,
                           @RequestParam(required = false) String sort,
                           @RequestParam(required = false) String q,
                           @RequestParam(defaultValue = "0") int page,
                           Model model) {
        FaqSort faqSort = FaqSort.LATEST;
        if (sort != null && !sort.isBlank()) {
            try {
                faqSort = FaqSort.valueOf(sort.trim().toUpperCase(Locale.ROOT));
                if (faqSort == FaqSort.RELEVANCE) {
                    faqSort = FaqSort.LATEST;
                }
            } catch (IllegalArgumentException exception) {
                faqSort = FaqSort.LATEST;
            }
        }

        List<Long> categoryIds = null;
        String selectedCategoryName = null;
        if (category != null) {
            List<Long> descendants = categoryQueryService.getDescendantIds(category);
            categoryIds = (descendants != null && !descendants.isEmpty())
                    ? descendants
                    : List.of(category);
            selectedCategoryName = categoryQueryService.findCategory(category)
                    .map(CategoryView::name)
                    .orElse(null);
        }

        String tagSlug = (tag != null && !tag.isBlank()) ? tag.trim() : null;
        String searchQuery = (q != null && !q.isBlank()) ? q.trim() : null;

        FaqFilter filter = FaqFilter.builder()
                .categoryIds(categoryIds)
                .tagSlug(tagSlug)
                .query(searchQuery)
                .sort(faqSort)
                .build();

        int safePage = Math.max(0, page);
        Pageable pageable = PageRequest.of(safePage, PAGE_SIZE);
        Page<FaqListItem> faqPage = faqQueryService.list(filter, pageable);

        List<String> queryParams = new ArrayList<>();
        if (searchQuery != null) {
            queryParams.add("q=" + URLEncoder.encode(searchQuery, StandardCharsets.UTF_8));
        }
        if (category != null) {
            queryParams.add("category=" + category);
        }
        if (tagSlug != null) {
            queryParams.add("tag=" + URLEncoder.encode(tagSlug, StandardCharsets.UTF_8));
        }
        if (faqSort != FaqSort.LATEST) {
            queryParams.add("sort=" + faqSort.name());
        }
        String queryString = String.join("&", queryParams);

        List<TagView> popularTags = faqQueryService.popularTags(20);

        model.addAttribute("page", faqPage);
        model.addAttribute("filter", filter);
        model.addAttribute("tree", categoryQueryService.getActiveTree());
        model.addAttribute("tags", popularTags);
        model.addAttribute("queryString", queryString);
        model.addAttribute("selectedCategoryName", selectedCategoryName);
        model.addAttribute("category", category);
        model.addAttribute("tag", tagSlug);
        model.addAttribute("sort", faqSort.name());
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("isSearch", searchQuery != null && !searchQuery.isBlank());

        return "guest/faq-list";
    }

    // Hiển thị chi tiết câu hỏi thường gặp và dữ liệu cá nhân liên quan.
    @GetMapping("/faq/{slug}")
    public String viewFaq(@PathVariable String slug,
                          HttpServletRequest request,
                          Model model) {
        FaqDetailView faq = faqQueryService.getPublishedBySlug(slug);

        String viewerKey = SecurityUtils.currentUser()
                .map(user -> "u" + user.getId())
                .orElseGet(() -> "ip" + TokenUtil.sha256Hex(ClientIpResolver.resolve(request)));

        faqViewCounter.recordView(faq.id(), viewerKey);

        List<FaqListItem> related = faqQueryService.related(faq.id(), 5);

        model.addAttribute("faq", faq);
        model.addAttribute("related", related);
        Boolean myVote = null;
        Boolean bookmarked = false;
        var student = SecurityUtils.currentUser().filter(user -> user.getRoleCode() == RoleCode.STUDENT);
        if (student.isPresent()) {
            Long userId = student.get().getId();
            faqPersonalService.recordView(userId, faq.id());
            bookmarked = faqQueryService.bookmarkedIds(userId, List.of(faq.id())).contains(faq.id());
            myVote = faqQueryService.myVotes(userId, List.of(faq.id())).get(faq.id());
        }
        model.addAttribute("myVote", myVote);
        model.addAttribute("bookmarked", bookmarked);

        return "guest/faq-detail";
    }
}
