package vn.edu.hcmute.qaute.controller.admin;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.view.GuestHomeFaqView;
import vn.edu.hcmute.qaute.service.admin.AdminGuestHomeFaqService;

// Tiếp nhận và điều phối các yêu cầu quản lý FAQ trang chủ guest.
@Controller
@RequestMapping("/admin/guest-home-faq")
public class AdminGuestHomeFaqController {

    private final AdminGuestHomeFaqService service;

    // Khởi tạo controller quản lý GuestHomeFaq.
    public AdminGuestHomeFaqController(AdminGuestHomeFaqService service) {
        this.service = service;
    }

    @GetMapping
    // Hiển thị danh sách FAQ.
    public String list(Model model) {
        model.addAttribute("faqs", service.listAll());
        model.addAttribute("pageTitle", "Quản lý FAQ trang chủ");
        return "admin/guest-home-faq-list";
    }

    @GetMapping("/new")
    // Hiển thị form tạo FAQ mới.
    public String newForm(Model model) {
        model.addAttribute("form", new GuestHomeFaqForm());
        model.addAttribute("pageTitle", "Thêm FAQ trang chủ");
        return "admin/guest-home-faq-form";
    }

    @GetMapping("/{id}/edit")
    // Hiển thị form chỉnh sửa FAQ.
    public String editForm(@PathVariable Long id, Model model) {
        GuestHomeFaqView faq = service.getById(id);
        GuestHomeFaqForm form = new GuestHomeFaqForm();
        form.setId(faq.getId());
        form.setCategoryName(faq.getCategoryName());
        form.setQuestion(faq.getQuestion());
        form.setAnswer(faq.getAnswer());
        form.setDisplayOrder(faq.getDisplayOrder());
        form.setActive(faq.isActive());

        model.addAttribute("form", form);
        model.addAttribute("pageTitle", "Sửa FAQ trang chủ");
        return "admin/guest-home-faq-form";
    }

    @PostMapping("/save")
    // Lưu FAQ mới hoặc cập nhật.
    public String save(@Valid @ModelAttribute("form") GuestHomeFaqForm form,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", form.getId() == null ? "Thêm FAQ trang chủ" : "Sửa FAQ trang chủ");
            return "admin/guest-home-faq-form";
        }

        try {
            if (form.getId() == null) {
                service.create(form.getCategoryName(), form.getQuestion(), form.getAnswer(), form.getDisplayOrder());
                redirect.addFlashAttribute("flashSuccess", "Đã thêm FAQ mới");
            } else {
                service.update(form.getId(), form.getCategoryName(), form.getQuestion(), form.getAnswer(),
                        form.getDisplayOrder(), form.isActive());
                redirect.addFlashAttribute("flashSuccess", "Đã cập nhật FAQ");
            }
            return "redirect:/admin/guest-home-faq";
        } catch (NotFoundException e) {
            redirect.addFlashAttribute("flashError", e.getMessage());
            return "redirect:/admin/guest-home-faq";
        }
    }

    @PostMapping("/{id}/delete")
    // Xóa FAQ.
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.delete(id);
            redirect.addFlashAttribute("flashSuccess", "Đã xóa FAQ");
        } catch (NotFoundException e) {
            redirect.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/admin/guest-home-faq";
    }

    @PostMapping("/{id}/move-up")
    // Di chuyển FAQ lên trên.
    public String moveUp(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.moveUp(id);
            redirect.addFlashAttribute("flashSuccess", "Đã di chuyển FAQ lên trên");
        } catch (NotFoundException e) {
            redirect.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/admin/guest-home-faq";
    }

    @PostMapping("/{id}/move-down")
    // Di chuyển FAQ xuống dưới.
    public String moveDown(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.moveDown(id);
            redirect.addFlashAttribute("flashSuccess", "Đã di chuyển FAQ xuống dưới");
        } catch (NotFoundException e) {
            redirect.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/admin/guest-home-faq";
    }

    @PostMapping("/{id}/toggle")
    // Bật/tắt trạng thái hiển thị.
    public String toggleActive(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.toggleActive(id);
            redirect.addFlashAttribute("flashSuccess", "Đã cập nhật trạng thái FAQ");
        } catch (NotFoundException e) {
            redirect.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/admin/guest-home-faq";
    }

    // Form object cho GuestHomeFaq.
    public static class GuestHomeFaqForm {
        private Long id;
        private String categoryName;
        private String question;
        private String answer;
        private int displayOrder;
        private boolean isActive;

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getCategoryName() { return categoryName; }
        public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }

        public String getAnswer() { return answer; }
        public void setAnswer(String answer) { this.answer = answer; }

        public int getDisplayOrder() { return displayOrder; }
        public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }

        public boolean isActive() { return isActive; }
        public boolean getIsActive() { return isActive; }
        public void setActive(boolean active) { this.isActive = active; }
        public void setIsActive(boolean active) { this.isActive = active; }
    }
}
