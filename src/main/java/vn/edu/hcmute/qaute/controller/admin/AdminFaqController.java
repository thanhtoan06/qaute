package vn.edu.hcmute.qaute.controller.admin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;
import vn.edu.hcmute.qaute.common.util.PageUtil;
import vn.edu.hcmute.qaute.dto.request.admin.AdminFaqFilter;
import vn.edu.hcmute.qaute.dto.view.CategoryNode;
import vn.edu.hcmute.qaute.dto.view.DepartmentNode;
import vn.edu.hcmute.qaute.dto.view.SlaCategoryOption;
import vn.edu.hcmute.qaute.service.admin.AdminFaqService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@Controller
public class AdminFaqController {
    private static final int PAGE_SIZE = 15;
    private final AdminFaqService faqs;
    private final CategoryQueryService categories;

    public AdminFaqController(AdminFaqService faqs, CategoryQueryService categories) {
        this.faqs = faqs;
        this.categories = categories;
    }

    @GetMapping("/admin/faq")
    public String list(@ModelAttribute("filter") AdminFaqFilter filter,
                       @RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageUtil.of(page, PAGE_SIZE, filter.getSort(),
                !"title".equals(filter.getSort()), AdminFaqService.ALLOWED_SORT_FIELDS,
                AdminFaqService.DEFAULT_SORT_FIELD);
        model.addAttribute("faqs", faqs.search(filter, pageable));
        Map<String, Long> counts = new LinkedHashMap<>();
        faqs.countByStatus().forEach((status, count) -> counts.put(status.name(), count));
        model.addAttribute("counts", counts);
        model.addAttribute("totalCount", counts.values().stream().mapToLong(Long::longValue).sum());
        model.addAttribute("statuses", FaqStatus.values());
        model.addAttribute("categoryOptions", categoryOptions(filter.getCategoryId()));
        model.addAttribute("authors", faqs.authors());
        model.addAttribute("paginationParams", params(filter, true));
        String tabParams = params(filter, false);
        model.addAttribute("tabParams", tabParams.isEmpty() ? "" : tabParams + "&amp;");
        model.addAttribute("pageTitle", "Quản lý FAQ");
        return "admin/faq-list";
    }

    private List<SlaCategoryOption> categoryOptions(Long selectedId) {
        List<SlaCategoryOption> options = new ArrayList<>();
        for (DepartmentNode department : categories.getActiveTree()) {
            for (CategoryNode root : department.categories()) {
                String rootLabel = department.name() + " › " + root.name();
                options.add(new SlaCategoryOption(root.id(), rootLabel));
                for (CategoryNode child : root.children()) {
                    options.add(new SlaCategoryOption(child.id(), rootLabel + " › " + child.name()));
                }
            }
        }
        if (selectedId != null && options.stream().noneMatch(option -> option.id().equals(selectedId))) {
            String path = categories.getDisplayPath(selectedId);
            if (!path.isBlank()) {
                options.add(new SlaCategoryOption(selectedId, path));
            }
        }
        return List.copyOf(options);
    }

    private static String params(AdminFaqFilter filter, boolean includeStatus) {
        StringJoiner params = new StringJoiner("&amp;");
        if (includeStatus) add(params, "status", filter.getStatus());
        add(params, "categoryId", filter.getCategoryId());
        add(params, "authorId", filter.getAuthorId());
        add(params, "q", filter.getQ());
        add(params, "featured", filter.getFeatured());
        add(params, "sort", filter.getSort());
        return params.toString();
    }

    private static void add(StringJoiner params, String key, Object value) {
        if (value != null && !value.toString().isBlank()) {
            params.add(key + "=" + URLEncoder.encode(value.toString(), StandardCharsets.UTF_8));
        }
    }
}
