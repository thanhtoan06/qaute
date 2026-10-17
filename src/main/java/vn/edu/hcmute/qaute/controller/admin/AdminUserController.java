package vn.edu.hcmute.qaute.controller.admin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.util.PageUtil;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.admin.UserCreateForm;
import vn.edu.hcmute.qaute.dto.request.admin.UserEditForm;
import vn.edu.hcmute.qaute.dto.request.admin.UserFilter;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.FacultyRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.admin.AdminUserQueryService;
import vn.edu.hcmute.qaute.service.admin.AdminUserService;

/** Administrator account directory (ADM-02, part 1). */
@Controller
public class AdminUserController {

    private static final int PAGE_SIZE = 15;

    private final AdminUserQueryService adminUserQueryService;
    private final AdminUserService adminUserService;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;

    public AdminUserController(AdminUserQueryService adminUserQueryService,
                               AdminUserService adminUserService, FacultyRepository facultyRepository,
                               DepartmentRepository departmentRepository) {
        this.adminUserQueryService = adminUserQueryService;
        this.adminUserService = adminUserService;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
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

    @GetMapping("/admin/users/new")
    public String newPage(Model model) {
        model.addAttribute("form", new UserCreateForm());
        formOptions(model, false, null);
        return "admin/user-form";
    }

    @PostMapping("/admin/users")
    public String create(@Valid @ModelAttribute("form") UserCreateForm form, BindingResult result,
                         Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("errors", FormErrors.of(result));
            formOptions(model, false, null);
            return "admin/user-form";
        }
        try {
            User user = adminUserService.create(form, SecurityUtils.requireUserId());
            redirect.addFlashAttribute("flashSuccess", "Đã gửi lời mời kích hoạt tới " + user.getEmail());
            return "redirect:/admin/users/" + user.getId();
        } catch (DuplicateException e) {
            model.addAttribute("errors", java.util.Map.of(e.getField(), e.getMessage()));
        } catch (BadRequestException e) {
            model.addAttribute("errors", e.getFieldErrors().isEmpty()
                    ? java.util.Map.of("global", e.getMessage()) : e.getFieldErrors());
        }
        formOptions(model, false, null);
        return "admin/user-form";
    }

    @GetMapping("/admin/users/{id}/edit")
    public String editPage(@PathVariable Long id, Model model) {
        model.addAttribute("form", adminUserService.getEditForm(id));
        formOptions(model, true, id);
        return "admin/user-form";
    }

    @PostMapping("/admin/users/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") UserEditForm form,
                         BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("errors", FormErrors.of(result));
            formOptions(model, true, id);
            return "admin/user-form";
        }
        try {
            adminUserService.update(id, form, SecurityUtils.requireUserId());
            redirect.addFlashAttribute("flashSuccess", "Đã lưu thông tin người dùng");
            return "redirect:/admin/users/" + id;
        } catch (DuplicateException e) {
            model.addAttribute("errors", java.util.Map.of(e.getField(), e.getMessage()));
        } catch (BadRequestException e) {
            model.addAttribute("errors", e.getFieldErrors().isEmpty()
                    ? java.util.Map.of("global", e.getMessage()) : e.getFieldErrors());
        }
        formOptions(model, true, id);
        return "admin/user-form";
    }

    @PostMapping("/admin/users/{id}/resend-invitation")
    public String resendInvitation(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            adminUserService.resendInvitation(id, SecurityUtils.requireUserId());
            redirect.addFlashAttribute("flashSuccess", "Đã gửi lại lời mời kích hoạt");
        } catch (BadRequestException e) {
            redirect.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/admin/users/" + id;
    }

    private void formOptions(Model model, boolean edit, Long id) {
        model.addAttribute("isEdit", edit);
        model.addAttribute("account", edit ? adminUserQueryService.getDetail(id) : null);
        model.addAttribute("roles", RoleCode.values());
        model.addAttribute("faculties", facultyRepository.findByActiveTrueOrderByNameAsc());
        model.addAttribute("departments", departmentRepository.findByActiveTrueOrderBySortOrderAscNameAsc());
        model.addAttribute("pageTitle", edit ? "Sửa người dùng" : "Thêm người dùng");
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
