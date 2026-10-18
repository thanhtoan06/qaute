package vn.edu.hcmute.qaute.controller.manager;

import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.dto.request.manager.CategoryChangeForm;
import vn.edu.hcmute.qaute.dto.request.manager.PriorityForm;
import vn.edu.hcmute.qaute.dto.request.manager.ReplyForm;
import vn.edu.hcmute.qaute.dto.request.manager.RequestInfoForm;
import vn.edu.hcmute.qaute.dto.request.manager.ResolveForm;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.manager.ManagerTicketDetailService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.ticket.TicketWorkflowService;

@Controller
public class ManagerTicketController {
    private final ManagerTicketDetailService details;
    private final TicketWorkflowService workflow;
    private final CategoryQueryService categories;

    public ManagerTicketController(ManagerTicketDetailService details,
            TicketWorkflowService workflow, CategoryQueryService categories) {
        this.details = details;
        this.workflow = workflow;
        this.categories = categories;
    }

    @GetMapping("/manager/tickets/{id}")
    public String detail(@PathVariable Long id, Model model) {
        var detail = details.getDetail(SecurityUtils.requireUserId(), id);
        model.addAttribute("detail", detail);
        model.addAttribute("priorities", TicketPriority.values());
        if (detail.canChangeCategory()) {
            Map<Long, String> categoryOptions = new LinkedHashMap<>();
            categories.findAllActiveLeaf().forEach(category -> categoryOptions.put(
                    category.id(), categories.getDisplayPath(category.id())));
            model.addAttribute("categoryOptions", categoryOptions);
        }
        model.addAttribute("pageTitle", "Không gian xử lý yêu cầu");
        return "manager/ticket-detail";
    }

    @PostMapping("/manager/tickets/{id}/reply")
    public String reply(@PathVariable Long id, @Valid @ModelAttribute ReplyForm form,
            BindingResult result, RedirectAttributes flash) {
        return run(id, result, flash, () -> workflow.addAdvisorReply(id,
                SecurityUtils.requireUserId(), form.getContentHtml(), form.getAssetIds()),
                "Đã gửi phản hồi");
    }

    @PostMapping("/manager/tickets/{id}/request-info")
    public String requestInfo(@PathVariable Long id, @Valid @ModelAttribute RequestInfoForm form,
            BindingResult result, RedirectAttributes flash) {
        return run(id, result, flash, () -> workflow.requestMoreInfo(id,
                SecurityUtils.requireUserId(), form.getMessageHtml(), form.getAssetIds()),
                "Đã yêu cầu sinh viên bổ sung thông tin");
    }

    @PostMapping("/manager/tickets/{id}/resolve")
    public String resolve(@PathVariable Long id, @Valid @ModelAttribute ResolveForm form,
            BindingResult result, RedirectAttributes flash) {
        return run(id, result, flash, () -> workflow.resolve(id,
                SecurityUtils.requireUserId(), form.getResolutionNote()),
                "Đã đánh dấu yêu cầu được giải quyết");
    }

    @PostMapping("/manager/tickets/{id}/priority")
    public String priority(@PathVariable Long id, @Valid @ModelAttribute PriorityForm form,
            BindingResult result, RedirectAttributes flash) {
        return run(id, result, flash, () -> workflow.changePriority(id,
                SecurityUtils.requireUserId(), form.getPriority(), form.getReason()),
                "Đã đổi mức ưu tiên");
    }

    @PostMapping("/manager/tickets/{id}/category")
    public String category(@PathVariable Long id, @Valid @ModelAttribute CategoryChangeForm form,
            BindingResult result, RedirectAttributes flash) {
        return run(id, result, flash, () -> workflow.changeCategory(id,
                SecurityUtils.requireUserId(), form.getNewCategoryId(), form.getReason()),
                "Đã đổi chuyên mục");
    }

    private String run(Long id, BindingResult result, RedirectAttributes flash,
            Runnable action, String success) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("flashError", result.getAllErrors().getFirst().getDefaultMessage());
            return redirect(id);
        }
        try {
            action.run();
            flash.addFlashAttribute("flashSuccess", success);
        } catch (AppException exception) {
            flash.addFlashAttribute("flashError", exception.getMessage());
        }
        return redirect(id);
    }

    private static String redirect(Long id) {
        return "redirect:/manager/tickets/" + id;
    }
}
