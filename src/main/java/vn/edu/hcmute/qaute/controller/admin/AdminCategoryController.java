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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.admin.CategoryForm;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.admin.AdminCategoryService;

/**
 * Quản lý chuyên mục (ADM-06). Quyền /admin/** (chỉ ADMIN) đã được SecurityConfig kiểm soát.
 */
@Controller
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    // Khởi tạo bộ điều khiển chuyên mục.
    public AdminCategoryController(AdminCategoryService adminCategoryService) {
        this.adminCategoryService = adminCategoryService;
    }

    @GetMapping("/admin/categories")
    // Hiển thị cây chuyên mục theo phòng ban.
    public String list(@RequestParam(required = false) Long departmentId, Model model) {
        model.addAttribute("trees", adminCategoryService.tree(departmentId));
        model.addAttribute("departments", adminCategoryService.departmentOptions());
        model.addAttribute("selectedDepartmentId", departmentId);
        model.addAttribute("pageTitle", "Chuyên mục");
        return "admin/category-list";
    }

    @GetMapping("/admin/categories/new")
    // Chuẩn bị biểu mẫu tạo chuyên mục.
    public String newPage(@RequestParam(required = false) Long departmentId,
                          @RequestParam(required = false) Long parentId, Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", adminCategoryService.newForm(departmentId, parentId));
        }
        return formView(model, (CategoryForm) model.getAttribute("form"));
    }

    @GetMapping("/admin/categories/{id}/edit")
    // Tải biểu mẫu chỉnh sửa chuyên mục.
    public String editPage(@PathVariable Long id, Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            if (!model.containsAttribute("form")) {
                model.addAttribute("form", adminCategoryService.get(id));
            }
            return formView(model, (CategoryForm) model.getAttribute("form"));
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("flashError", e.getMessage());
            return "redirect:/admin/categories";
        }
    }

    @PostMapping("/admin/categories")
    // Kiểm tra và lưu biểu mẫu chuyên mục.
    public String save(@Valid @ModelAttribute("form") CategoryForm form,
                       BindingResult bindingResult, Model model,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", FormErrors.of(bindingResult));
            return formView(model, form);
        }
        try {
            boolean creating = form.getId() == null;
            adminCategoryService.save(form, SecurityUtils.requireUserId());
            redirectAttributes.addFlashAttribute("flashSuccess",
                    creating ? "Đã thêm chuyên mục mới" : "Đã lưu thay đổi chuyên mục");
            return "redirect:/admin/categories";
        } catch (BadRequestException e) {
            Map<String, String> fieldErrors = e.getFieldErrors();
            return showFormError(model, fieldErrors == null || fieldErrors.isEmpty()
                    ? Map.of("global", e.getMessage()) : fieldErrors, form);
        } catch (DuplicateException e) {
            Map<String, String> errors = e.getField() == null
                    ? Map.of("global", e.getMessage())
                    : Map.of(e.getField(), e.getMessage());
            return showFormError(model, errors, form);
        } catch (AppException e) {
            return showFormError(model, Map.of("global", e.getMessage()), form);
        }
    }

    @PostMapping("/admin/categories/{id}/toggle")
    // Đổi trạng thái hoạt động của chuyên mục.
    public String toggle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            adminCategoryService.toggleActive(id, SecurityUtils.requireUserId());
            redirectAttributes.addFlashAttribute("flashSuccess", "Đã đổi trạng thái chuyên mục");
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    /** Nạp dữ liệu cho form: danh sách phòng ban và các chuyên mục cha có thể chọn. */
    // Nạp dữ liệu cần thiết cho biểu mẫu chuyên mục.
    private String formView(Model model, CategoryForm form) {
        model.addAttribute("pageTitle", form.getId() == null ? "Thêm chuyên mục" : "Sửa chuyên mục");
        model.addAttribute("departments", adminCategoryService.departmentOptions());
        model.addAttribute("parents", adminCategoryService.parentOptions(form.getId()));
        return "admin/category-form";
    }

    // Gộp lỗi và hiển thị lại biểu mẫu chuyên mục.
    private String showFormError(Model model, Map<String, String> additions, CategoryForm form) {
        Map<String, String> errors = new LinkedHashMap<>();
        Object existing = model.getAttribute("errors");
        if (existing instanceof Map<?, ?> existingErrors) {
            existingErrors.forEach((key, value) ->
                    errors.put(String.valueOf(key), String.valueOf(value)));
        }
        errors.putAll(additions);
        model.addAttribute("errors", errors);
        return formView(model, form);
    }
}
