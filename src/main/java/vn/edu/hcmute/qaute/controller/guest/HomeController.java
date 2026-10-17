package vn.edu.hcmute.qaute.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.service.knowledge.FaqQueryService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@Controller
public class HomeController {

    private final CategoryQueryService categoryQueryService;
    private final FaqQueryService faqQueryService;
    private final SettingsService settingsService;

    public HomeController(CategoryQueryService categoryQueryService,
                          FaqQueryService faqQueryService,
                          SettingsService settingsService) {
        this.categoryQueryService = categoryQueryService;
        this.faqQueryService = faqQueryService;
        this.settingsService = settingsService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tree", categoryQueryService.getActiveTree());
        model.addAttribute("categoryCounts", faqQueryService.countPublishedByCategory());
        model.addAttribute("topViewed", faqQueryService.topViewed(10, 10));
        model.addAttribute("featured", faqQueryService.featured(4));
        model.addAttribute("latest", faqQueryService.latest(6));
        model.addAttribute("totalFaq", faqQueryService.countPublished());
        model.addAttribute("contactEmail", settingsService.getString(SettingKeys.SITE_CONTACT_EMAIL));
        model.addAttribute("contactPhone", settingsService.getString(SettingKeys.SITE_CONTACT_PHONE));
        return "guest/home";
    }
}
