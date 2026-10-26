package vn.edu.hcmute.qaute.controller.manager;

import jakarta.validation.Valid;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.constant.FaqStatus;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.util.PageUtil;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.admin.FaqForm;
import vn.edu.hcmute.qaute.dto.request.admin.FaqSubmitForm;
import vn.edu.hcmute.qaute.dto.view.SlaCategoryOption;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.manager.ManagerFaqService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho ManagerFaq.
@Controller
public class ManagerFaqController {
    private final ManagerFaqService faqs;
    private final ManagerScopeService scopes;
    private final CategoryQueryService categories;

    // Khởi tạo bộ điều khiển FAQ của quản lý.
    public ManagerFaqController(ManagerFaqService faqs, ManagerScopeService scopes,
            CategoryQueryService categories) {
        this.faqs = faqs;
        this.scopes = scopes;
        this.categories = categories;
    }

    @GetMapping("/manager/faq")
    // Hiển thị danh sách FAQ theo trạng thái.
    public String list(@RequestParam(required = false) FaqStatus status,
            @RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("faqs", faqs.list(SecurityUtils.requireUserId(), status,
                PageUtil.of(page, 15, "updatedAt", true, java.util.Set.of("updatedAt"), "updatedAt")));
        model.addAttribute("status", status);
        model.addAttribute("statuses", FaqStatus.values());
        model.addAttribute("pageTitle", "FAQ của tôi");
        return "manager/faq-list";
    }

    @GetMapping("/manager/faq/new")
    // Hiển thị biểu mẫu tạo FAQ mới.
    public String newPage(Model model) {
        model.addAttribute("form", new FaqForm());
        formOptions(model, null);
        return "manager/faq-form";
    }

    @GetMapping("/manager/faq/{id}/edit")
    // Hiển thị biểu mẫu chỉnh sửa FAQ.
    public String edit(@PathVariable Long id, Model model) {
        Long managerId = SecurityUtils.requireUserId();
        model.addAttribute("form", faqs.getForm(id, managerId));
        model.addAttribute("sourceTicketId", faqs.sourceTicketId(id, managerId));
        formOptions(model, id);
        return "manager/faq-form";
    }

    @PostMapping("/manager/faq")
    // Lưu bản nháp FAQ và hiển thị lỗi nếu có.
    public String save(@Valid @ModelAttribute("form") FaqForm form, BindingResult result,
            Model model, RedirectAttributes redirect) {
        Map<String, String> errors = FormErrors.of(result);
        if (!result.hasErrors()) {
            try {
                Long id = faqs.save(form, SecurityUtils.requireUserId());
                redirect.addFlashAttribute("flashSuccess", "Đã lưu bản nháp FAQ");
                return "redirect:/manager/faq/" + id + "/edit";
            } catch (AppException exception) {
                if (exception instanceof BadRequestException bad && !bad.getFieldErrors().isEmpty()) {
                    errors.putAll(bad.getFieldErrors());
                } else {
                    errors.put("global", exception.getMessage());
                }
            }
        }
        model.addAttribute("errors", errors);
        if (form.getId() != null) {
            model.addAttribute("sourceTicketId", faqs.sourceTicketId(form.getId(),
                    SecurityUtils.requireUserId()));
        }
        formOptions(model, form.getId());
        return "manager/faq-form";
    }

    @PostMapping("/manager/faq/{id}/submit")
    // Gửi FAQ đã xác nhận để chờ duyệt.
    public String submit(@PathVariable Long id, @Valid @ModelAttribute FaqSubmitForm form, BindingResult errors,
            RedirectAttributes redirect) {
        try {
            if (errors.hasErrors()) throw new BadRequestException("Dữ liệu xác nhận không hợp lệ");
            faqs.submit(id, SecurityUtils.requireUserId(), form.isConfirmedNoPersonalInfo());
            redirect.addFlashAttribute("flashSuccess", "Đã gửi FAQ chờ duyệt");
            return "redirect:/manager/faq";
        } catch (AppException exception) {
            redirect.addFlashAttribute("flashError", exception.getMessage());
            return "redirect:/manager/faq/" + id + "/edit";
        }
    }

    @PostMapping("/manager/tickets/{id}/to-faq")
    // Tạo bản nháp FAQ từ yêu cầu hỗ trợ.
    public String fromTicket(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            Long faqId = faqs.createFromTicket(id, SecurityUtils.requireUserId());
            redirect.addFlashAttribute("flashSuccess", "Đã tạo bản nháp FAQ; hãy kiểm tra thông tin cá nhân");
            return "redirect:/manager/faq/" + faqId + "/edit";
        } catch (AppException exception) {
            redirect.addFlashAttribute("flashError", exception.getMessage());
            return "redirect:/manager/tickets/" + id;
        }
    }

    // Nạp các lựa chọn chuyên mục và trạng thái biểu mẫu FAQ.
    private void formOptions(Model model, Long id) {
        Long managerId = SecurityUtils.requireUserId();
        List<Long> scopeIds = scopes.getScopeCategoryIds(managerId);
        List<SlaCategoryOption> options = scopeIds == null ? List.of()
                : categories.findAllActiveLeaf().stream()
                        .filter(category -> scopeIds.contains(category.id()))
                        .map(category -> new SlaCategoryOption(category.id(),
                                categories.getDisplayPath(category.id()))).toList();
        model.addAttribute("categoryOptions", options);
        model.addAttribute("managerMode", true);
        model.addAttribute("editing", id != null);
        model.addAttribute("pageTitle", id == null ? "Đề xuất FAQ mới" : "Sửa bản nháp FAQ");
    }
}
