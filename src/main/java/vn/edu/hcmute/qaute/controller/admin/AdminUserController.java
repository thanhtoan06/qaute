package vn.edu.hcmute.qaute.controller.admin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.util.PageUtil;
import vn.edu.hcmute.qaute.dto.request.admin.UserFilter;
import vn.edu.hcmute.qaute.service.admin.AdminUserQueryService;

/** Administrator account directory (ADM-02, part 1). */
@Controller
public class AdminUserController {

    private static final int PAGE_SIZE = 15;

    private final AdminUserQueryService adminUserQueryService;

    public AdminUserController(AdminUserQueryService adminUserQueryService) {
        this.adminUserQueryService = adminUserQueryService;
    }

    @GetMapping("/admin/users")
    public String list(@ModelAttribute("filter") UserFilter filter,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        Pageable pageable = PageUtil.of(page, PAGE_SIZE, filter.getSort(),
                !"fullName".equals(filter.getSort()),
                AdminUserQueryService.ALLOWED_SORT_FIELDS,
                AdminUserQueryService.DEFAULT_SORT_FIELD);
        model.addAttribute("users", adminUserQueryService.search(filter, pageable));
        model.addAttribute("paginationParams", paginationParams(filter));
        model.addAttribute("roles", RoleCode.values());
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("pageTitle", "Danh sách người dùng");
        return "admin/user-list";
    }

    @GetMapping("/admin/users/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("user", adminUserQueryService.getDetail(id));
        model.addAttribute("pageTitle", "Chi tiết người dùng");
        return "admin/user-detail";
    }

    private static String paginationParams(UserFilter filter) {
        StringJoiner params = new StringJoiner("&amp;");
        addParam(params, "q", filter.getQ());
        addParam(params, "role", filter.getRole());
        addParam(params, "status", filter.getStatus());
        addParam(params, "from", filter.getFrom());
        addParam(params, "to", filter.getTo());
        addParam(params, "sort", filter.getSort());
        return params.toString();
    }

    private static void addParam(StringJoiner params, String name, Object value) {
        if (value != null && !value.toString().isBlank()) {
            params.add(name + "=" + URLEncoder.encode(value.toString(), StandardCharsets.UTF_8));
        }
    }
}
