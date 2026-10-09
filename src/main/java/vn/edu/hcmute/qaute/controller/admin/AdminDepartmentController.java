package vn.edu.hcmute.qaute.controller.admin;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.admin.DepartmentForm;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.admin.AdminDepartmentService;

/**
 * Quản lý phòng ban (ADM-05). Quyền /admin/** (chỉ ADMIN) đã được SecurityConfig kiểm soát.
 */
@Controller
public class AdminDepartmentController {

    private final AdminDepartmentService adminDepartmentService;

    public AdminDepartmentController(AdminDepartmentService adminDepartmentService) {
        this.adminDepartmentService = adminDepartmentService;
    }

    @GetMapping("/admin/departments")
    public String list(Model model) {
        model.addAttribute("departments", adminDepartmentService.list());
        model.addAttribute("pageTitle", "Phòng ban");
        return "admin/department-list";
    }

    @GetMapping("/admin/departments/new")
    public String newPage(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new DepartmentForm());
        }
        model.addAttribute("pageTitle", "Thêm phòng ban");
        return "admin/department-form";
    }

    @GetMapping("/admin/departments/{id}/edit")
    public String editPage(@PathVariable Long id, Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            if (!model.containsAttribute("form")) {
                model.addAttribute("form", adminDepartmentService.get(id));
            }
            model.addAttribute("pageTitle", "Sửa phòng ban");
            return "admin/department-form";
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("flashError", e.getMessage());
            return "redirect:/admin/departments";
        }
    }

    @PostMapping("/admin/departments")
    public String save(@Valid @ModelAttribute("form") DepartmentForm form,
                       BindingResult bindingResult, Model model,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", FormErrors.of(bindingResult));
            model.addAttribute("pageTitle", form.getId() == null ? "Thêm phòng ban" : "Sửa phòng ban");
            return "admin/department-form";
        }
        try {
            boolean creating = form.getId() == null;
            adminDepartmentService.save(form, SecurityUtils.requireUserId());
            redirectAttributes.addFlashAttribute("flashSuccess",
                    creating ? "Đã thêm phòng ban mới" : "Đã lưu thay đổi phòng ban");
            return "redirect:/admin/departments";
        } catch (BadRequestException e) {
            return showFormError(model, e.getFieldErrors(), form);
        } catch (DuplicateException e) {
            Map<String, String> errors = e.getField() == null
                    ? Map.of("global", e.getMessage())
                    : Map.of(e.getField(), e.getMessage());
            return showFormError(model, errors, form);
        }
    }

    @PostMapping("/admin/departments/{id}/toggle")
    public String toggle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            adminDepartmentService.toggleActive(id, SecurityUtils.requireUserId());
            redirectAttributes.addFlashAttribute("flashSuccess", "Đã đổi trạng thái phòng ban");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/admin/departments";
    }

    private String showFormError(Model model, Map<String, String> additions, DepartmentForm form) {
        Map<String, String> errors = new LinkedHashMap<>();
        Object existing = model.getAttribute("errors");
        if (existing instanceof Map<?, ?> existingErrors) {
            existingErrors.forEach((key, value) ->
                    errors.put(String.valueOf(key), String.valueOf(value)));
        }
        errors.putAll(additions);
        model.addAttribute("errors", errors);
        model.addAttribute("pageTitle", form.getId() == null ? "Thêm phòng ban" : "Sửa phòng ban");
        return "admin/department-form";
    }
}
