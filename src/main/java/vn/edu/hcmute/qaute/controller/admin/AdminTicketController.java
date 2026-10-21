package vn.edu.hcmute.qaute.controller.admin;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import vn.edu.hcmute.qaute.dto.request.manager.AssignForm;
import vn.edu.hcmute.qaute.dto.request.manager.PriorityForm;
import vn.edu.hcmute.qaute.dto.request.manager.CategoryChangeForm;
import vn.edu.hcmute.qaute.dto.request.admin.UserActionForm;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.SlaStatus;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.util.PageUtil;
import vn.edu.hcmute.qaute.dto.request.admin.AdminTicketFilter;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.admin.AdminTicketService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@Controller
public class AdminTicketController {
    private static final Set<String> SORT_FIELDS = Set.of("createdAt", "lastActivityAt", "priority");
    private final AdminTicketService service;
    private final DepartmentRepository departments;
    private final CategoryRepository categoryRepository;
    private final CategoryQueryService categories;
    private final UserRepository users;

    public AdminTicketController(AdminTicketService service, DepartmentRepository departments,
            CategoryRepository categoryRepository, CategoryQueryService categories, UserRepository users) {
        this.service = service;
        this.departments = departments;
        this.categoryRepository = categoryRepository;
        this.categories = categories;
        this.users = users;
    }

    @GetMapping("/admin/tickets")
    public String list(@ModelAttribute("filter") AdminTicketFilter filter,
            @RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = "priority".equals(filter.getSort())
                ? PageRequest.of(Math.max(0, page), 20)
                : PageUtil.of(page, 20, filter.getSort(), true, SORT_FIELDS, "createdAt");
        model.addAttribute("tickets", service.search(filter, pageable));
        model.addAttribute("paginationParams", params(filter));
        options(model);
        model.addAttribute("pageTitle", "Giám sát yêu cầu");
        return "admin/ticket-list";
    }

    @GetMapping("/admin/tickets/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("detail", service.detail(id));
        Map<Long, String> leafCategories = new LinkedHashMap<>();
        categories.findAllActiveLeaf().forEach(category -> leafCategories.put(category.id(),
                categories.getDisplayPath(category.id())));
        model.addAttribute("categoryOptions", leafCategories);
        model.addAttribute("pageTitle", "Chi tiết yêu cầu");
        model.addAttribute("priorities", TicketPriority.values());
        model.addAttribute("managers", users.searchByRoleAndKeyword(RoleCode.MANAGER, null));
        return "admin/ticket-detail";
    }

    @PostMapping("/admin/tickets/{id}/reassign")
    public String reassign(@PathVariable Long id, @Valid @ModelAttribute AssignForm form,
            BindingResult errors, RedirectAttributes flash) {
        if (errors.hasErrors()) return invalid(id, errors, flash);
        Long toManagerId = form.getToManagerId();
        String reason = form.getReason();
        return run(id, reason, toManagerId, flash,
                () -> service.reassign(id, toManagerId, reason, SecurityUtils.requireUserId()),
                "Đã phân công yêu cầu");
    }

    @PostMapping("/admin/tickets/{id}/priority")
    public String priority(@PathVariable Long id, @Valid @ModelAttribute PriorityForm form,
            BindingResult errors, RedirectAttributes flash) {
        if (errors.hasErrors()) return invalid(id, errors, flash);
        TicketPriority priority = form.getPriority();
        String reason = form.getReason();
        return run(id, reason, priority, flash,
                () -> service.changePriority(id, priority, reason, SecurityUtils.requireUserId()),
                "Đã đổi mức ưu tiên");
    }

    @PostMapping("/admin/tickets/{id}/category")
    public String category(@PathVariable Long id, @Valid @ModelAttribute CategoryChangeForm form,
            BindingResult errors, RedirectAttributes flash) {
        if (errors.hasErrors()) return invalid(id, errors, flash);
        Long newCategoryId = form.getNewCategoryId();
        String reason = form.getReason();
        return run(id, reason, newCategoryId, flash,
                () -> service.changeCategory(id, newCategoryId, reason, SecurityUtils.requireUserId()),
                "Đã đổi chuyên mục");
    }

    @PostMapping("/admin/tickets/{id}/force-close")
    public String forceClose(@PathVariable Long id, @Valid @ModelAttribute UserActionForm form,
            BindingResult errors, RedirectAttributes flash) {
        if (errors.hasErrors()) return invalid(id, errors, flash);
        String reason = form.getReason();
        return run(id, reason, Boolean.TRUE, flash,
                () -> service.forceClose(id, reason, SecurityUtils.requireUserId()),
                "Đã đóng yêu cầu");
    }

    private static String invalid(Long id, BindingResult errors, RedirectAttributes flash) {
        flash.addFlashAttribute("flashError", errors.getAllErrors().getFirst().getDefaultMessage());
        return "redirect:/admin/tickets/" + id;
    }

    private static String run(Long id, String reason, Object choice, RedirectAttributes flash,
            Runnable action, String success) {
        if (choice == null || reason == null || reason.trim().length() < 10 || reason.trim().length() > 500) {
            flash.addFlashAttribute("flashError", "Vui lòng chọn giá trị và nhập lý do từ 10 đến 500 ký tự");
            return "redirect:/admin/tickets/" + id;
        }
        try {
            action.run();
            flash.addFlashAttribute("flashSuccess", success);
        } catch (AppException exception) {
            flash.addFlashAttribute("flashError", exception.getMessage());
        }
        return "redirect:/admin/tickets/" + id;
    }

    private void options(Model model) {
        model.addAttribute("statuses", TicketStatus.values());
        model.addAttribute("priorities", TicketPriority.values());
        model.addAttribute("slaStatuses", SlaStatus.values());
        model.addAttribute("departments", departments.findAllByOrderBySortOrderAscNameAsc());
        Map<Long, String> categoryOptions = new LinkedHashMap<>();
        categoryRepository.findAll().forEach(category -> categoryOptions.put(category.getId(),
                categories.getDisplayPath(category.getId())));
        model.addAttribute("categories", categoryOptions);
        model.addAttribute("managers", users.searchByRoleAndKeyword(RoleCode.MANAGER, null));
    }

    private static String params(AdminTicketFilter f) {
        StringJoiner params = new StringJoiner("&amp;");
        add(params, "q", f.getQ());
        add(params, "status", f.getStatus());
        add(params, "priority", f.getPriority());
        add(params, "categoryId", f.getCategoryId());
        add(params, "departmentId", f.getDepartmentId());
        add(params, "assigneeId", f.getAssigneeId());
        add(params, "slaStatus", f.getSlaStatus());
        add(params, "from", f.getFrom());
        add(params, "to", f.getTo());
        add(params, "minRating", f.getMinRating());
        add(params, "maxRating", f.getMaxRating());
        add(params, "sort", f.getSort());
        return params.toString();
    }

    private static void add(StringJoiner params, String name, Object value) {
        if (value != null && !value.toString().isBlank()) {
            params.add(name + "=" + URLEncoder.encode(value.toString(), StandardCharsets.UTF_8));
        }
    }
}
