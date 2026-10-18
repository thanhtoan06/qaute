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
import vn.edu.hcmute.qaute.service.admin.AdminSynonymService;

@Controller
public class AdminSynonymController {
    private final AdminSynonymService synonyms;

    public AdminSynonymController(AdminSynonymService synonyms) {
        this.synonyms = synonyms;
    }

    @GetMapping("/admin/faq/synonyms")
    public String list(@RequestParam(required = false) String term, Model model) {
        model.addAttribute("synonyms", synonyms.list());
        model.addAttribute("noResultQueries", synonyms.noResultQueries());
        model.addAttribute("prefillTerm", term == null ? "" : term);
        model.addAttribute("pageTitle", "Từ đồng nghĩa FAQ");
        return "admin/synonyms";
    }

    @PostMapping("/admin/faq/synonyms")
    public String create(@RequestParam(required = false) String term,
                         @RequestParam(required = false) String synonym,
                         RedirectAttributes redirect) {
        return action(redirect, () -> synonyms.create(term, synonym), "Đã thêm cặp từ đồng nghĩa");
    }

    @PostMapping("/admin/faq/synonyms/{id}/toggle")
    public String toggle(@PathVariable Long id, RedirectAttributes redirect) {
        return action(redirect, () -> synonyms.toggle(id), "Đã đổi trạng thái cặp từ");
    }

    @PostMapping("/admin/faq/synonyms/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        return action(redirect, () -> synonyms.delete(id), "Đã xóa cặp từ đồng nghĩa");
    }

    private String action(RedirectAttributes redirect, Runnable operation, String success) {
        try {
            operation.run();
            redirect.addFlashAttribute("flashSuccess", success);
        } catch (BadRequestException | DuplicateException e) {
            redirect.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/admin/faq/synonyms";
    }
}
