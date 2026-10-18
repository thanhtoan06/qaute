package vn.edu.hcmute.qaute.controller.manager;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.dto.request.manager.AssignForm;
import vn.edu.hcmute.qaute.dto.request.manager.ReturnQueueForm;
import vn.edu.hcmute.qaute.dto.view.ManagerOption;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.ticket.TicketAccessService;
import vn.edu.hcmute.qaute.service.ticket.TicketWorkflowService;

@Controller
public class ManagerTicketActionController {
    private final TicketWorkflowService workflow;
    private final TicketAccessService access;
    private final ManagerScopeService scopes;

    public ManagerTicketActionController(TicketWorkflowService workflow, TicketAccessService access,
                                         ManagerScopeService scopes) {
        this.workflow = workflow;
        this.access = access;
        this.scopes = scopes;
    }

    @PostMapping("/manager/tickets/{id}/claim")
    public String claim(@PathVariable Long id, @RequestParam(required = false) String redirect,
                        RedirectAttributes flash) {
        return run(redirect, flash, () -> workflow.claim(id, SecurityUtils.requireUserId()),
                "Đã nhận yêu cầu", true);
    }

    @PostMapping("/manager/tickets/{id}/assign")
    public String assign(@PathVariable Long id, @Valid @ModelAttribute AssignForm form,
                         BindingResult result, @RequestParam(required = false) String redirect,
                         RedirectAttributes flash) {
        if (result.hasErrors() || !validReason(form.getReason())) {
            flash.addFlashAttribute("flashError", "Vui lòng chọn tư vấn viên và nhập lý do từ 10 đến 500 ký tự");
            return redirectTo(redirect);
        }
        return run(redirect, flash, () -> workflow.assign(id, form.getToManagerId(),
                SecurityUtils.requireUserId(), form.getReason().trim()),
                "Đã chuyển yêu cầu cho tư vấn viên", false);
    }

    @PostMapping("/manager/tickets/{id}/return")
    public String returnToQueue(@PathVariable Long id, @Valid @ModelAttribute ReturnQueueForm form,
                                BindingResult result, @RequestParam(required = false) String redirect,
                                RedirectAttributes flash) {
        if (result.hasErrors() || !validReason(form.getReason())) {
            flash.addFlashAttribute("flashError", "Lý do trả về hàng đợi phải từ 10 đến 500 ký tự");
            return redirectTo(redirect);
        }
        return run(redirect, flash, () -> workflow.returnToQueue(id,
                SecurityUtils.requireUserId(), form.getReason().trim()),
                "Đã trả yêu cầu về hàng đợi", false);
    }

    @GetMapping("/manager/tickets/{id}/advisors")
    @ResponseBody
    public ApiResponse<List<ManagerOption>> advisors(@PathVariable Long id) {
        Ticket ticket = access.requireViewable(id, SecurityUtils.requireUserId(), RoleCode.MANAGER);
        return ApiResponse.ok(scopes.findAdvisors(ticket.getCategoryId()));
    }

    private String run(String redirect, RedirectAttributes flash, Runnable action,
                       String success, boolean claim) {
        try {
            action.run();
            flash.addFlashAttribute("flashSuccess", success);
        } catch (AppException exception) {
            flash.addFlashAttribute("flashError", errorMessage(exception, claim));
        }
        return redirectTo(redirect);
    }

    private static String errorMessage(AppException exception, boolean claim) {
        ErrorCode code = exception.getCode();
        if (code == ErrorCode.ERR_CONFLICT) {
            return claim ? "Yêu cầu vừa được người khác nhận. Vui lòng tải lại hàng đợi"
                    : "Yêu cầu vừa được người khác cập nhật. Vui lòng tải lại hàng đợi";
        }
        if (code == ErrorCode.ERR_NOT_FOUND) {
            return "Không tìm thấy yêu cầu hoặc bạn không có quyền xem";
        }
        if (code == ErrorCode.ERR_FORBIDDEN) {
            return "Bạn không có quyền thực hiện thao tác này";
        }
        return exception.getMessage() == null || exception.getMessage().isBlank()
                ? code.getDefaultMessage() : exception.getMessage();
    }

    private static boolean validReason(String reason) {
        return reason != null && reason.trim().length() >= 10 && reason.trim().length() <= 500;
    }

    static String redirectTo(String requested) {
        if (requested == null || requested.length() > 2048) return "redirect:/manager/queue";
        try {
            URI uri = URI.create(requested);
            String path = uri.getPath();
            if (uri.isAbsolute() || uri.getRawAuthority() != null || path == null
                    || !path.startsWith("/manager/") || path.contains("\\")
                    || path.contains("//") || path.contains("/../") || path.endsWith("/..")
                    || path.contains("/./") || path.endsWith("/.")
                    || path.chars().anyMatch(Character::isISOControl)) {
                return "redirect:/manager/queue";
            }
            return "redirect:" + requested;
        } catch (IllegalArgumentException exception) {
            return "redirect:/manager/queue";
        }
    }
}
