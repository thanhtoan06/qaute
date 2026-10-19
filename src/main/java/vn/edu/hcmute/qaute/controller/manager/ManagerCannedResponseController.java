package vn.edu.hcmute.qaute.controller.manager;

import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.dto.request.manager.CannedResponseForm;
import vn.edu.hcmute.qaute.dto.view.CannedResponseOption;
import vn.edu.hcmute.qaute.dto.view.RenderedCannedResponse;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.manager.CannedResponseService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;

@Controller
public class ManagerCannedResponseController {
    private final CannedResponseService service;
    private final ManagerScopeService scopes;
    private final CategoryQueryService categories;

    public ManagerCannedResponseController(CannedResponseService service,
            ManagerScopeService scopes, CategoryQueryService categories) {
        this.service = service;
        this.scopes = scopes;
        this.categories = categories;
    }

    @GetMapping("/manager/canned-responses")
    public String list(@RequestParam(name = "edit", required = false) Long editId, Model model) {
        Long managerId = SecurityUtils.requireUserId();
        model.addAttribute("items", service.list(managerId));
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", editId == null ? new CannedResponseForm()
                    : service.getForm(editId, managerId));
        }
        List<Long> scopeIds = scopes.getScopeCategoryIds(managerId);
        Map<Long, String> categoryOptions = new LinkedHashMap<>();
        if (scopeIds != null && !scopeIds.isEmpty()) {
            categories.findAllActiveLeaf().stream().filter(category -> scopeIds.contains(category.id()))
                    .forEach(category -> categoryOptions.put(category.id(),
                            categories.getDisplayPath(category.id())));
        }
        model.addAttribute("categoryOptions", categoryOptions);
        model.addAttribute("pageTitle", "Mẫu trả lời nhanh");
        return "manager/canned-responses";
    }

    @PostMapping("/manager/canned-responses")
    public String save(@Valid @ModelAttribute("form") CannedResponseForm form,
            BindingResult result, RedirectAttributes flash) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("flashError", result.getAllErrors().getFirst().getDefaultMessage());
            flash.addFlashAttribute("form", form);
            return "redirect:/manager/canned-responses";
        }
        try {
            Long id = service.save(form, SecurityUtils.requireUserId());
            flash.addFlashAttribute("flashSuccess", "Đã lưu mẫu trả lời");
            return "redirect:/manager/canned-responses?edit=" + id;
        } catch (AppException exception) {
            flash.addFlashAttribute("flashError", exception.getMessage());
            flash.addFlashAttribute("form", form);
            return "redirect:/manager/canned-responses";
        }
    }

    @PostMapping("/manager/canned-responses/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        try {
            service.delete(id, SecurityUtils.requireUserId());
            flash.addFlashAttribute("flashSuccess", "Đã xóa mẫu trả lời");
        } catch (AppException exception) {
            flash.addFlashAttribute("flashError", exception.getMessage());
        }
        return "redirect:/manager/canned-responses";
    }

    @GetMapping("/api/manager/canned-responses")
    @ResponseBody
    public ApiResponse<List<CannedResponseOption>> options() {
        return ApiResponse.ok(service.listActive(SecurityUtils.requireUserId()));
    }

    @GetMapping("/api/manager/canned-responses/{id}/render")
    @ResponseBody
    public ApiResponse<RenderedCannedResponse> render(@PathVariable Long id,
            @RequestParam Long ticketId) {
        return ApiResponse.ok(new RenderedCannedResponse(
                service.render(id, SecurityUtils.requireUserId(), ticketId)));
    }
}
