package vn.edu.hcmute.qaute.controller.admin;

import vn.edu.hcmute.qaute.common.util.BoundedPages;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.edu.hcmute.qaute.dto.request.admin.TagNameForm;
import vn.edu.hcmute.qaute.dto.request.admin.TagMergeForm;

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

// Tiếp nhận và điều phối các yêu cầu giao diện cho AdminTag.
@Controller
public class AdminTagController {
    private final AdminTagService tags;

    // Khởi tạo bộ điều khiển thẻ FAQ.
    public AdminTagController(AdminTagService tags) {
        this.tags = tags;
    }

    @GetMapping("/admin/faq/tags")
    // Phân trang và hiển thị danh sách thẻ FAQ.
    public String list(@RequestParam(defaultValue="0") int page, Model model) {
        var results = tags.page(BoundedPages.of(page, 25));
        model.addAttribute("tags", results.getContent());
        model.addAttribute("resultPage", results);
        model.addAttribute("pageTitle", "Quản lý thẻ FAQ");
        return "admin/tags";
    }

    @PostMapping("/admin/faq/tags")
    // Kiểm tra và tạo thẻ FAQ.
    public String create(@Valid @ModelAttribute TagNameForm form, BindingResult errors, RedirectAttributes redirect) {
        if (errors.hasErrors()) return invalid(errors, redirect);
        return action(redirect, () -> tags.create(form.getName()), "Đã thêm thẻ");
    }

    @PostMapping("/admin/faq/tags/{id}/rename")
    // Kiểm tra và đổi tên thẻ FAQ.
    public String rename(@PathVariable Long id, @Valid @ModelAttribute TagNameForm form, BindingResult errors,
                         RedirectAttributes redirect) {
        if (errors.hasErrors()) return invalid(errors, redirect);
        return action(redirect, () -> tags.rename(id, form.getName()), "Đã đổi tên thẻ");
    }

    @PostMapping("/admin/faq/tags/{id}/merge")
    // Kiểm tra và gộp thẻ FAQ.
    public String merge(@PathVariable Long id, @Valid @ModelAttribute TagMergeForm form, BindingResult errors,
                        RedirectAttributes redirect) {
        if (errors.hasErrors()) return invalid(errors, redirect);
        return action(redirect, () -> tags.merge(id, form.getTargetId()), "Đã gộp thẻ");
    }

    @PostMapping("/admin/faq/tags/{id}/delete")
    // Xóa thẻ FAQ.
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        return action(redirect, () -> tags.delete(id), "Đã xóa thẻ");
    }

    // Chuyển lỗi biểu mẫu thành thông báo.
    private String invalid(BindingResult errors, RedirectAttributes redirect) {
        redirect.addFlashAttribute("flashError", errors.getAllErrors().getFirst().getDefaultMessage());
        return "redirect:/admin/faq/tags";
    }

    // Thực hiện thao tác thẻ và báo kết quả.
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
