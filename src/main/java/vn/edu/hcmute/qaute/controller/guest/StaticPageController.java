package vn.edu.hcmute.qaute.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.system.SettingsService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho StaticPage.
@Controller
public class StaticPageController {

    private final CategoryQueryService categoryQueryService;
    private final SettingsService settingsService;

    // Khởi tạo controller các trang tĩnh.
    public StaticPageController(CategoryQueryService categoryQueryService,
                                SettingsService settingsService) {
        this.categoryQueryService = categoryQueryService;
        this.settingsService = settingsService;
    }

    // Hiển thị trang giới thiệu.
    @GetMapping("/pages/about")
    public String about() {
        return "guest/about";
    }

    // Hiển thị trang hướng dẫn.
    @GetMapping("/pages/guide")
    public String guide() {
        return "guest/guide";
    }

    // Hiển thị thông tin liên hệ và danh mục chủ đề.
    @GetMapping("/pages/contact")
    public String contact(Model model) {
        model.addAttribute("tree", categoryQueryService.getActiveTree());
        model.addAttribute("contactAddress", settingsService.getString(SettingKeys.SITE_CONTACT_ADDRESS));
        model.addAttribute("contactPhone", settingsService.getString(SettingKeys.SITE_CONTACT_PHONE));
        model.addAttribute("contactEmail", settingsService.getString(SettingKeys.SITE_CONTACT_EMAIL));
        return "guest/contact";
    }

    // Hiển thị trang chính sách quyền riêng tư.
    @GetMapping("/pages/privacy")
    public String privacy() {
        return "guest/privacy";
    }

    // Hiển thị trang điều khoản sử dụng.
    @GetMapping("/pages/terms")
    public String terms() {
        return "guest/terms";
    }
}
