package vn.edu.hcmute.qaute.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.hcmute.qaute.service.knowledge.FaqQueryService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho Topic.
@Controller
public class TopicController {

    private final CategoryQueryService categoryQueryService;
    private final FaqQueryService faqQueryService;

    // Khởi tạo controller danh mục chủ đề.
    public TopicController(CategoryQueryService categoryQueryService,
                           FaqQueryService faqQueryService) {
        this.categoryQueryService = categoryQueryService;
        this.faqQueryService = faqQueryService;
    }

    // Hiển thị cây chủ đề và số lượng câu hỏi.
    @GetMapping("/topics")
    public String topics(@RequestParam(required = false) String q, Model model) {
        String searchQuery = (q != null && !q.isBlank()) ? q.trim() : null;
        model.addAttribute("tree", categoryQueryService.getActiveTree());
        model.addAttribute("counts", faqQueryService.countPublishedByCategory());
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("isSearch", searchQuery != null && !searchQuery.isBlank());
        return "guest/topics";
    }
}
