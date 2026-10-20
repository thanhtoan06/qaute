package vn.edu.hcmute.qaute.controller.admin;

import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.constant.AnnouncementAudience;
import vn.edu.hcmute.qaute.common.constant.AnnouncementStatus;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.admin.AnnouncementForm;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.admin.AdminAnnouncementService;

@Controller
public class AdminAnnouncementController {
    private final AdminAnnouncementService service;

    public AdminAnnouncementController(AdminAnnouncementService service) { this.service = service; }

    @GetMapping("/admin/announcements")
    public String list(@RequestParam(required = false) AnnouncementStatus status,
                       @RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("announcements", service.list(status, PageRequest.of(Math.max(0, page), 15,
                Sort.by(Sort.Order.desc("pinned"), Sort.Order.desc("createdAt")))));
        model.addAttribute("status", status);
        model.addAttribute("statuses", AnnouncementStatus.values());
        model.addAttribute("pageTitle", "Quản lý thông báo");
        return "admin/announcement-list";
    }

    @GetMapping("/admin/announcements/new")
    public String create(Model model) {
        AnnouncementForm form = new AnnouncementForm();
        form.setAudience(AnnouncementAudience.ALL);
        model.addAttribute("form", form);
        options(model);
        return "admin/announcement-form";
    }

    @GetMapping("/admin/announcements/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("form", service.getForm(id));
        options(model);
        return "admin/announcement-form";
    }

    @PostMapping("/admin/announcements")
    public String save(@Valid @ModelAttribute("form") AnnouncementForm form, BindingResult result,
                       Model model, RedirectAttributes redirect) {
        Map<String, String> errors = FormErrors.of(result);
        if (!result.hasErrors()) {
            try {
                Long id = service.save(form, SecurityUtils.requireUserId());
                redirect.addFlashAttribute("flashSuccess", "Đã lưu thông báo");
                return "redirect:/admin/announcements/" + id + "/edit";
            } catch (AppException e) {
                errors.put("global", e.getMessage());
            }
        }
        model.addAttribute("errors", errors);
        options(model);
        return "admin/announcement-form";
    }

    @PostMapping("/admin/announcements/{id}/publish")
    public String publish(@PathVariable Long id, RedirectAttributes redirect) {
        return action(id, redirect, "Đã xuất bản thông báo", () -> service.publish(id, SecurityUtils.requireUserId()));
    }

    @PostMapping("/admin/announcements/{id}/unpublish")
    public String unpublish(@PathVariable Long id, RedirectAttributes redirect) {
        return action(id, redirect, "Đã chuyển về bản nháp", () -> service.unpublish(id, SecurityUtils.requireUserId()));
    }

    @PostMapping("/admin/announcements/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        return action(id, redirect, "Đã xóa thông báo", () -> service.delete(id, SecurityUtils.requireUserId()));
    }

    private String action(Long id, RedirectAttributes redirect, String message, Runnable work) {
        try { work.run(); redirect.addFlashAttribute("flashSuccess", message); }
        catch (AppException e) { redirect.addFlashAttribute("flashError", e.getMessage()); }
        return "redirect:/admin/announcements";
    }

    private void options(Model model) {
        model.addAttribute("audiences", AnnouncementAudience.values());
        model.addAttribute("pageTitle", "Soạn thông báo");
    }
}
