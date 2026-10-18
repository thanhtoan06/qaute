package vn.edu.hcmute.qaute.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.service.admin.AdminTagService;

@Controller
public class AdminTagController {
    private final AdminTagService tags;

    public AdminTagController(AdminTagService tags) {
        this.tags = tags;
    }

    @GetMapping("/admin/faq/tags")
    public String list(Model model) {
        model.addAttribute("tags", tags.list());
        model.addAttribute("pageTitle", "Quản lý thẻ FAQ");
        return "admin/tags";
    }

    @PostMapping("/admin/faq/tags")
    public String create(@RequestParam(required = false) String name, RedirectAttributes redirect) {
        return action(redirect, () -> tags.create(name), "Đã thêm thẻ");
    }

    @PostMapping("/admin/faq/tags/{id}/rename")
    public String rename(@PathVariable Long id, @RequestParam(required = false) String name,
                         RedirectAttributes redirect) {
        return action(redirect, () -> tags.rename(id, name), "Đã đổi tên thẻ");
    }

    @PostMapping("/admin/faq/tags/{id}/merge")
    public String merge(@PathVariable Long id, @RequestParam Long targetId,
                        RedirectAttributes redirect) {
        return action(redirect, () -> tags.merge(id, targetId), "Đã gộp thẻ");
    }

    @PostMapping("/admin/faq/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        return action(redirect, () -> tags.delete(id), "Đã xóa thẻ");
    }

    private String action(RedirectAttributes redirect, Runnable operation, String success) {
        try {
            operation.run();
            redirect.addFlashAttribute("flashSuccess", success);
        } catch (BadRequestException | DuplicateException e) {
            redirect.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/admin/faq/tags";
    }
}
