package vn.edu.hcmute.qaute.controller.admin;

import vn.edu.hcmute.qaute.common.util.BoundedPages;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.admin.ManagerScopeForm;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.admin.AdminManagerScopeService;

/** Quản lý phạm vi chuyên mục của Manager (ADM-07). */
@Controller
public class AdminManagerController {

    private final AdminManagerScopeService adminManagerScopeService;

    public AdminManagerController(AdminManagerScopeService adminManagerScopeService) {
        this.adminManagerScopeService = adminManagerScopeService;
    }

    @GetMapping("/admin/managers")
    public String list(@RequestParam(required = false) String q, @RequestParam(defaultValue="0") int page, Model model) {
        var results = adminManagerScopeService.page(q, BoundedPages.of(page, 25));
        model.addAttribute("managers", results.getContent());
        model.addAttribute("resultPage", results);
        model.addAttribute("paginationParams", q == null ? "" : "q=" + URLEncoder.encode(q, StandardCharsets.UTF_8));
        model.addAttribute("uncoveredCategories", adminManagerScopeService.uncoveredCategories());
        model.addAttribute("q", q);
        model.addAttribute("pageTitle", "Manager và phạm vi");
        return "admin/manager-list";
    }

    @GetMapping("/admin/managers/{id}/scope")
    public String scope(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (!model.containsAttribute("form")) {
                model.addAttribute("form", adminManagerScopeService.getForm(id));
            }
            return scopeView(model, id);
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("flashError", e.getMessage());
            return "redirect:/admin/managers";
        }
    }

    @PostMapping("/admin/managers/{id}/scope")
    public String save(@PathVariable Long id, @Valid @ModelAttribute("form") ManagerScopeForm form,
                       BindingResult bindingResult, Model model,
                       RedirectAttributes redirectAttributes) {
        // Id ở URL là nguồn tin cậy; không cho hidden field chuyển phạm vi sang Manager khác.
        form.setManagerId(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", FormErrors.of(bindingResult));
            return scopeView(model, id);
        }
        try {
            adminManagerScopeService.save(form, SecurityUtils.requireUserId());
            redirectAttributes.addFlashAttribute("flashSuccess", "Đã lưu phạm vi phụ trách của Manager");
            return "redirect:/admin/managers";
        } catch (BadRequestException e) {
            Map<String, String> fieldErrors = e.getFieldErrors();
            return showFormError(model, fieldErrors == null || fieldErrors.isEmpty()
                    ? Map.of("global", e.getMessage()) : fieldErrors, form, id);
        } catch (AppException e) {
            redirectAttributes.addFlashAttribute("flashError", e.getMessage());
            return "redirect:/admin/managers";
        }
    }

    private String scopeView(Model model, Long managerId) {
        model.addAttribute("manager", adminManagerScopeService.getManagerHeader(managerId));
        model.addAttribute("departments", adminManagerScopeService.departmentOptions());
        model.addAttribute("departmentTrees", adminManagerScopeService.categoryTree());
        model.addAttribute("pageTitle", "Phạm vi phụ trách");
        return "admin/manager-scope";
    }

    private String showFormError(Model model, Map<String, String> additions,
                                 ManagerScopeForm form, Long managerId) {
        Map<String, String> errors = new LinkedHashMap<>();
        Object existing = model.getAttribute("errors");
        if (existing instanceof Map<?, ?> existingErrors) {
            existingErrors.forEach((key, value) ->
                    errors.put(String.valueOf(key), String.valueOf(value)));
        }
        errors.putAll(additions);
        model.addAttribute("errors", errors);
        return scopeView(model, managerId);
    }
}
