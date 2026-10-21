package vn.edu.hcmute.qaute.controller.guest;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.hcmute.qaute.common.constant.AnnouncementAudience;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.service.governance.AnnouncementQueryService;
import vn.edu.hcmute.qaute.service.knowledge.FaqQueryService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.system.SettingsService;
import vn.edu.hcmute.qaute.service.ticket.PublicStatsService;

@Controller
public class HomeController {

    private final CategoryQueryService categoryQueryService;
    private final FaqQueryService faqQueryService;
    private final SettingsService settingsService;
    private final AnnouncementQueryService announcementQueryService;
    private final PublicStatsService publicStatsService;

    public HomeController(CategoryQueryService categoryQueryService,
                          FaqQueryService faqQueryService,
                          SettingsService settingsService,
                          AnnouncementQueryService announcementQueryService,
                          PublicStatsService publicStatsService) {
        this.categoryQueryService = categoryQueryService;
        this.faqQueryService = faqQueryService;
        this.settingsService = settingsService;
        this.announcementQueryService = announcementQueryService;
        this.publicStatsService = publicStatsService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tree", categoryQueryService.getActiveTree());
        model.addAttribute("categoryCounts", faqQueryService.countPublishedByCategory());
        model.addAttribute("topViewed", faqQueryService.topViewed(10, 10));
        model.addAttribute("featured", faqQueryService.featured(4));
        model.addAttribute("latest", faqQueryService.latest(6));
        
        long totalFaq = faqQueryService.countPublished();
        model.addAttribute("totalFaq", totalFaq);
        
        model.addAttribute("contactEmail", settingsService.getString(SettingKeys.SITE_CONTACT_EMAIL));
        model.addAttribute("contactPhone", settingsService.getString(SettingKeys.SITE_CONTACT_PHONE));

        // T1-62: Announcements (ALL, 3) & Stats
        model.addAttribute("announcements", announcementQueryService.findActiveFor(AnnouncementAudience.ALL, 3));
        
        Double medianFirstResponseMinutes = publicStatsService.getMedianFirstResponseMinutes(30);
        model.addAttribute("medianFirstResponseMinutes", medianFirstResponseMinutes);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalFaq", totalFaq);
        stats.put("medianFirstResponseMinutes", medianFirstResponseMinutes);
        model.addAttribute("stats", stats);

        return "guest/home";
    }
}
