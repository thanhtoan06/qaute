package vn.edu.hcmute.qaute.service.admin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.dto.view.SettingView;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.system.SettingsService;

// Xử lý nghiệp vụ AdminSettings trong hệ thống.
@Service
public class AdminSettingsService {
    private static final List<String> GROUP_ORDER = List.of(
            "OTP", "BẢO MẬT", "TICKET", "SLA", "TỆP", "CHAT", "LỊCH HẸN",
            "FAQ", "CHUNG", "LƯU TRỮ", "LIÊN HỆ");

    private final SettingsService settings;
    private final AuditService audit;

    // Khởi tạo dịch vụ quản trị cài đặt.
    public AdminSettingsService(SettingsService settings, AuditService audit) {
        this.settings = settings;
        this.audit = audit;
    }

    // Nhóm cài đặt theo thứ tự hiển thị.
    public Map<String, List<SettingView>> groups() {
        Map<String, List<SettingView>> found = new LinkedHashMap<>();
        settings.listAll().forEach(setting -> found
                .computeIfAbsent(setting.groupName(), ignored -> new ArrayList<>()).add(setting));
        Map<String, List<SettingView>> ordered = new LinkedHashMap<>();
        GROUP_ORDER.forEach(name -> {
            if (found.containsKey(name)) {
                ordered.put(name, List.copyOf(found.remove(name)));
            }
        });
        found.forEach((name, values) -> ordered.put(name, List.copyOf(values)));
        return ordered;
    }

    // Lưu giá trị cài đặt trong một nhóm và trả lỗi xác thực.
    public Map<String, String> saveGroup(String groupName, Map<String, String> values, Long actorId) {
        List<SettingView> group = groups().get(groupName);
        if (group == null) {
            throw new BadRequestException("Nhóm cài đặt không tồn tại");
        }
        Map<String, String> submitted = values == null ? Map.of() : values;
        Map<String, String> errors = new LinkedHashMap<>();
        for (SettingView setting : group) {
            String key = setting.key();
            String value = "BOOL".equals(setting.valueType())
                    && !submitted.containsKey(key) ? "false" : submitted.get(key);
            if (Objects.equals(setting.value(), value)) {
                continue;
            }
            try {
                settings.update(key, value, actorId);
                audit.log(AuditActions.SETTING_CHANGED, "setting", null,
                        key + "=" + setting.value(), key + "=" + value);
            } catch (BadRequestException e) {
                errors.put(key, e.getMessage());
            }
        }
        return errors;
    }
}
