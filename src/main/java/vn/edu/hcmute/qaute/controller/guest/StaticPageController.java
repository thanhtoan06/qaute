package vn.edu.hcmute.qaute.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@Controller
public class StaticPageController {

    private final CategoryQueryService categoryQueryService;
    private final SettingsService settingsService;

    public StaticPageController(CategoryQueryService categoryQueryService,
                                SettingsService settingsService) {
        this.categoryQueryService = categoryQueryService;
        this.settingsService = settingsService;
    }

    @GetMapping("/pages/about")
    public String about() {
        return "guest/about";
    }

    @GetMapping("/pages/guide")
    public String guide() {
        return "guest/guide";
    }

    @GetMapping("/pages/contact")
    public String contact(Model model) {
        model.addAttribute("tree", categoryQueryService.getActiveTree());
        model.addAttribute("contactEmail", settingsService.getString(SettingKeys.SITE_CONTACT_EMAIL));
        model.addAttribute("contactPhone", settingsService.getString(SettingKeys.SITE_CONTACT_PHONE));
        model.addAttribute("contactAddress", settingsService.getString(SettingKeys.SITE_CONTACT_ADDRESS));
        return "guest/contact";
    }
}

