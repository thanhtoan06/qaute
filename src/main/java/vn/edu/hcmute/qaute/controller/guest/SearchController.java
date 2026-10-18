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
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.hcmute.qaute.common.constant.FaqSort;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.util.TokenUtil;
import vn.edu.hcmute.qaute.common.web.ClientIpResolver;
import vn.edu.hcmute.qaute.dto.view.CategoryView;
import vn.edu.hcmute.qaute.dto.view.SearchResultItem;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.knowledge.SearchService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@Controller
public class SearchController {

    private static final int PAGE_SIZE = 12;

    private final SearchService searchService;
    private final CategoryQueryService categoryQueryService;

    public SearchController(SearchService searchService,
                            CategoryQueryService categoryQueryService) {
        this.searchService = searchService;
        this.categoryQueryService = categoryQueryService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String q,
                         @RequestParam(required = false) Long category,
                         @RequestParam(required = false) String tag,
                         @RequestParam(defaultValue = "RELEVANCE") String sort,
                         @RequestParam(defaultValue = "0") int page,
                         HttpServletRequest request,
                         Model model) {
        FaqSort faqSort = FaqSort.RELEVANCE;
        if (sort != null && !sort.isBlank()) {
            try {
                faqSort = FaqSort.valueOf(sort.trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ignored) {
                faqSort = FaqSort.RELEVANCE;
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

        String clientIp = ClientIpResolver.resolve(request);
        String ipHash = TokenUtil.sha256Hex(clientIp != null ? clientIp : "");
        Long userId = SecurityUtils.currentUser().map(QauteUserDetails::getId).orElse(null);
        String source = "SEARCH";

        int safePage = Math.max(0, page);
        Pageable pageable = PageRequest.of(safePage, PAGE_SIZE);

        List<String> queryParams = new ArrayList<>();
        if (q != null && !q.isBlank()) {
            queryParams.add("q=" + URLEncoder.encode(q, StandardCharsets.UTF_8));
        }
        if (category != null) {
            queryParams.add("category=" + category);
        }
        if (tagSlug != null) {
            queryParams.add("tag=" + URLEncoder.encode(tagSlug, StandardCharsets.UTF_8));
        }
        if (faqSort != FaqSort.RELEVANCE) {
            queryParams.add("sort=" + faqSort.name());
        }
        String queryString = String.join("&", queryParams);

        model.addAttribute("q", q != null ? q : "");
        model.addAttribute("category", category);
        model.addAttribute("selectedCategoryName", selectedCategoryName);
        model.addAttribute("tag", tagSlug);
        model.addAttribute("sort", faqSort.name());
        model.addAttribute("tree", categoryQueryService.getActiveTree());
        model.addAttribute("queryString", queryString);

        if (q == null || q.isBlank()) {
            model.addAttribute("page", Page.<SearchResultItem>empty());
            return "guest/search-results";
        }

        try {
            Page<SearchResultItem> resultPage = searchService.search(
                    q, categoryIds, tagSlug, faqSort, pageable, userId, ipHash, source);
            model.addAttribute("page", resultPage);
        } catch (BadRequestException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("page", Page.<SearchResultItem>empty());
        }

        return "guest/search-results";
    }
}

