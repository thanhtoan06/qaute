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
import vn.edu.hcmute.qaute.dto.request.admin.UserActionForm;
import vn.edu.hcmute.qaute.dto.request.admin.UserEditForm;
import vn.edu.hcmute.qaute.dto.request.admin.UserFilter;
import vn.edu.hcmute.qaute.dto.request.admin.UserRoleChangeForm;
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

    // Khởi tạo bộ điều khiển người dùng quản trị.
    public AdminUserController(AdminUserQueryService adminUserQueryService,
                               AdminUserService adminUserService, FacultyRepository facultyRepository,
                               DepartmentRepository departmentRepository) {
        this.adminUserQueryService = adminUserQueryService;
        this.adminUserService = adminUserService;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping("/admin/users")
    // Tìm kiếm và phân trang danh sách người dùng.
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
    // Hiển thị thông tin chi tiết người dùng.
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("user", adminUserQueryService.getDetail(id));
        model.addAttribute("currentUserId", SecurityUtils.requireUserId());
        model.addAttribute("roles", RoleCode.values());
        model.addAttribute("pageTitle", "Chi tiết người dùng");
        return "admin/user-detail";
    }

    @GetMapping("/admin/users/new")
    // Chuẩn bị biểu mẫu tạo người dùng.
    public String newPage(Model model) {
        model.addAttribute("form", new UserCreateForm());
        formOptions(model, false, null);
        return "admin/user-form";
    }

    @PostMapping("/admin/users")
    // Kiểm tra và tạo tài khoản người dùng.
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
    // Tải biểu mẫu chỉnh sửa người dùng.
    public String editPage(@PathVariable Long id, Model model) {
        model.addAttribute("form", adminUserService.getEditForm(id));
        formOptions(model, true, id);
        return "admin/user-form";
    }

    @PostMapping("/admin/users/{id}")
    // Kiểm tra và cập nhật thông tin người dùng.
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
    // Gửi lại lời mời kích hoạt tài khoản.
    public String resendInvitation(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            adminUserService.resendInvitation(id, SecurityUtils.requireUserId());
            redirect.addFlashAttribute("flashSuccess", "Đã gửi lại lời mời kích hoạt");
        } catch (BadRequestException e) {
            redirect.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/admin/users/" + id;
    }

    @PostMapping("/admin/users/{id}/lock")
    // Kiểm tra yêu cầu rồi khóa tài khoản.
    public String lock(@PathVariable Long id, @Valid @ModelAttribute UserActionForm form,
                       BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return actionError(id, redirect, "Lý do khóa phải từ 10 đến 500 ký tự");
        }
        return runAction(id, redirect,
                () -> adminUserService.lock(id, form.getReason(), SecurityUtils.requireUserId()),
                "Đã khóa tài khoản và thu hồi các phiên đăng nhập");
    }

    @PostMapping("/admin/users/{id}/unlock")
    // Mở khóa tài khoản người dùng.
    public String unlock(@PathVariable Long id, RedirectAttributes redirect) {
        return runAction(id, redirect,
                () -> adminUserService.unlock(id, SecurityUtils.requireUserId()),
                "Đã mở khóa tài khoản");
    }

    @PostMapping("/admin/users/{id}/deactivate")
    // Kiểm tra yêu cầu rồi vô hiệu hóa tài khoản.
    public String deactivate(@PathVariable Long id, @Valid @ModelAttribute UserActionForm form,
                             BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return actionError(id, redirect, "Lý do vô hiệu hóa phải từ 10 đến 500 ký tự");
        }
        return runAction(id, redirect,
                () -> adminUserService.deactivate(id, form.getReason(), SecurityUtils.requireUserId()),
                "Đã vô hiệu hóa tài khoản và thu hồi các phiên đăng nhập");
    }

    @PostMapping("/admin/users/{id}/reactivate")
    // Kích hoạt lại tài khoản người dùng.
    public String reactivate(@PathVariable Long id, RedirectAttributes redirect) {
        return runAction(id, redirect,
                () -> adminUserService.reactivate(id, SecurityUtils.requireUserId()),
                "Đã kích hoạt lại tài khoản; lời mời được gửi nếu cần đặt mật khẩu");
    }

    @PostMapping("/admin/users/{id}/force-reset")
    // Gửi lời mời đặt lại mật khẩu.
    public String forcePasswordReset(@PathVariable Long id, RedirectAttributes redirect) {
        return runAction(id, redirect,
                () -> adminUserService.forcePasswordReset(id, SecurityUtils.requireUserId()),
                "Đã gửi lời mời đặt lại mật khẩu và thu hồi các phiên đăng nhập");
    }

    @PostMapping("/admin/users/{id}/role")
    // Kiểm tra yêu cầu rồi đổi vai trò người dùng.
    public String changeRole(@PathVariable Long id,
                             @Valid @ModelAttribute UserRoleChangeForm form,
                             BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return actionError(id, redirect, "Vui lòng chọn vai trò và nhập lý do từ 10 đến 500 ký tự");
        }
        return runAction(id, redirect,
                () -> adminUserService.changeRole(id, form.getRole(), form.getReason(), SecurityUtils.requireUserId()),
                "Đã đổi vai trò và thu hồi các phiên đăng nhập");
    }

    @GetMapping("/admin/users/{id}/security")
    // Hiển thị phiên và lịch sử đăng nhập của người dùng.
    public String security(@PathVariable Long id, Model model) {
        model.addAttribute("user", adminUserQueryService.getDetail(id));
        model.addAttribute("sessions", adminUserService.sessions(id));
        model.addAttribute("loginHistory", adminUserService.loginHistory(id));
        model.addAttribute("pageTitle", "Bảo mật người dùng");
        return "admin/user-security";
    }

    @PostMapping("/admin/users/{id}/sessions/{sessionId}/revoke")
    // Thu hồi một phiên đăng nhập của người dùng.
    public String revokeSession(@PathVariable Long id, @PathVariable Long sessionId,
                                RedirectAttributes redirect) {
        return runSecurityAction(id, redirect,
                () -> adminUserService.revokeSession(sessionId, id, SecurityUtils.requireUserId()),
                "Đã thu hồi phiên đăng nhập");
    }

    @PostMapping("/admin/users/{id}/sessions/revoke-all")
    // Thu hồi mọi phiên đăng nhập của người dùng.
    public String revokeAllSessions(@PathVariable Long id, RedirectAttributes redirect) {
        return runSecurityAction(id, redirect,
                () -> adminUserService.revokeAllSessions(id, SecurityUtils.requireUserId()),
                "Đã thu hồi mọi phiên đăng nhập");
    }

    // Thực hiện thao tác bảo mật và báo kết quả.
    private String runSecurityAction(Long id, RedirectAttributes redirect, Runnable action, String success) {
        try {
            action.run();
            redirect.addFlashAttribute("flashSuccess", success);
        } catch (BadRequestException e) {
            redirect.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/admin/users/" + id + "/security";
    }

    // Thực hiện thao tác tài khoản và báo kết quả.
    private String runAction(Long id, RedirectAttributes redirect, Runnable action, String success) {
        try {
            action.run();
            redirect.addFlashAttribute("flashSuccess", success);
        } catch (BadRequestException e) {
            redirect.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/admin/users/" + id;
    }

    // Chuyển lỗi thao tác thành thông báo.
    private String actionError(Long id, RedirectAttributes redirect, String message) {
        redirect.addFlashAttribute("flashError", message);
        return "redirect:/admin/users/" + id;
    }

    // Nạp các lựa chọn cần thiết cho biểu mẫu người dùng.
    private void formOptions(Model model, boolean edit, Long id) {
        model.addAttribute("isEdit", edit);
        model.addAttribute("account", edit ? adminUserQueryService.getDetail(id) : null);
        model.addAttribute("roles", RoleCode.values());
        model.addAttribute("faculties", facultyRepository.findByActiveTrueOrderByNameAsc());
        model.addAttribute("departments", departmentRepository.findByActiveTrueOrderBySortOrderAscNameAsc());
        model.addAttribute("pageTitle", edit ? "Sửa người dùng" : "Thêm người dùng");
    }

    // Tạo chuỗi tham số lọc cho phân trang.
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

    // Thêm tham số không rỗng vào chuỗi lọc.
    private static void addParam(StringJoiner params, String name, Object value) {
        if (value != null && !value.toString().isBlank()) {
            params.add(name + "=" + URLEncoder.encode(value.toString(), StandardCharsets.UTF_8));
        }
    }
}
