package vn.edu.hcmute.qaute.controller.admin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.dto.view.SettingView;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.admin.AdminSettingsService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho AdminSettings.
@Controller
public class AdminSettingsController {
    private final AdminSettingsService settings;

    // Khởi tạo bộ điều khiển cài đặt hệ thống.
    public AdminSettingsController(AdminSettingsService settings) {
        this.settings = settings;
    }

    @GetMapping("/admin/settings")
    // Hiển thị các nhóm cài đặt hệ thống.
    public String page(@RequestParam(name = "group", required = false) String group, Model model) {
        Map<String, List<SettingView>> groups = settings.groups();
        populate(model, groups, group, Map.of(), Map.of());
        return "admin/settings";
    }

    @PostMapping("/admin/settings/{group}")
    // Lưu giá trị cài đặt của nhóm được chọn.
    public String save(@PathVariable String group, @RequestParam Map<String, String> values,
                       Model model, RedirectAttributes redirect) {
        Map<String, String> errors = settings.saveGroup(group, values, SecurityUtils.requireUserId());
        if (errors.isEmpty()) {
            redirect.addFlashAttribute("flashSuccess", "Đã lưu cài đặt nhóm " + group);
            return "redirect:/admin/settings?group=" + URLEncoder.encode(group, StandardCharsets.UTF_8);
        }
        Map<String, List<SettingView>> groups = settings.groups();
        Map<String, String> invalidValues = new LinkedHashMap<>();
        errors.keySet().forEach(key -> invalidValues.put(key, values.get(key)));
        populate(model, groups, group, errors, invalidValues);
        model.addAttribute("saveError", "Các giá trị hợp lệ đã được lưu. Vui lòng sửa các dòng báo lỗi.");
        return "admin/settings";
    }

    // Nạp nhóm cài đặt và giá trị hiển thị vào trang.
    private static void populate(Model model, Map<String, List<SettingView>> groups,
                                 String selectedGroup, Map<String, String> errors,
                                 Map<String, String> invalidValues) {
        Map<String, String> groupPaths = new LinkedHashMap<>();
        Map<String, String> displayValues = new LinkedHashMap<>();
        groups.forEach((name, settings) -> {
            groupPaths.put(name, URLEncoder.encode(name, StandardCharsets.UTF_8)
                    .replace("+", "%20"));
            settings.forEach(setting -> displayValues.put(setting.key(), setting.value()));
        });
        displayValues.putAll(invalidValues);
        model.addAttribute("groups", groups);
        model.addAttribute("groupPaths", groupPaths);
        model.addAttribute("activeGroup", groups.containsKey(selectedGroup)
                ? selectedGroup : groups.keySet().stream().findFirst().orElse(""));
        model.addAttribute("displayValues", displayValues);
        model.addAttribute("errors", errors);
        model.addAttribute("pageTitle", "Cài đặt hệ thống");
    }
}
