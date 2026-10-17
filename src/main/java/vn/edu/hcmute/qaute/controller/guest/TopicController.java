package vn.edu.hcmute.qaute.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.hcmute.qaute.service.knowledge.FaqQueryService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@Controller
public class TopicController {

    private final CategoryQueryService categoryQueryService;
    private final FaqQueryService faqQueryService;

    public TopicController(CategoryQueryService categoryQueryService,
                           FaqQueryService faqQueryService) {
        this.categoryQueryService = categoryQueryService;
        this.faqQueryService = faqQueryService;
    }

    @GetMapping("/topics")
    public String topics(Model model) {
        model.addAttribute("tree", categoryQueryService.getActiveTree());
        model.addAttribute("counts", faqQueryService.countPublishedByCategory());
        return "guest/topics";
    }
}

