package vn.edu.hcmute.qaute.controller.student;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.dto.request.student.TicketActionForm;
import vn.edu.hcmute.qaute.dto.request.student.TicketReplyForm;
import vn.edu.hcmute.qaute.dto.request.student.TicketReviewForm;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.student.StudentTicketDetailService;
import vn.edu.hcmute.qaute.service.student.StudentReviewService;
import vn.edu.hcmute.qaute.service.ticket.TicketWorkflowService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho StudentTicketDetail.
@Controller
public class StudentTicketDetailController {
    private final StudentTicketDetailService details;
    private final TicketWorkflowService workflow;
    private final StudentReviewService reviews;

    // Khởi tạo bộ điều khiển chi tiết yêu cầu và các dịch vụ xử lý.
    public StudentTicketDetailController(StudentTicketDetailService details, TicketWorkflowService workflow,
                                         StudentReviewService reviews) {
        this.details = details;
        this.workflow = workflow;
        this.reviews = reviews;
    }

    @GetMapping("/student/tickets/{id}")
    // Hiển thị chi tiết yêu cầu cùng biểu mẫu phản hồi và đánh giá.
    public String detail(@PathVariable Long id, Model model) {
        Long studentId = SecurityUtils.requireUserId();
        model.addAttribute("detail", details.getDetail(studentId, id));
        model.addAttribute("reopenRemaining", details.reopenRemaining(studentId, id));
        if (!model.containsAttribute("replyForm")) model.addAttribute("replyForm", new TicketReplyForm());
        if (!model.containsAttribute("reviewForm")) model.addAttribute("reviewForm", new TicketReviewForm());
        return "student/ticket-detail";
    }

    @PostMapping("/student/tickets/{id}/reply")
    // Kiểm tra và gửi phản hồi cho yêu cầu.
    public String reply(@PathVariable Long id, @Valid @ModelAttribute("replyForm") TicketReplyForm form,
                        BindingResult result, RedirectAttributes flash) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("flashError", result.getAllErrors().getFirst().getDefaultMessage());
            return "redirect:/student/tickets/" + id;
        }
        try {
            details.reply(SecurityUtils.requireUserId(), id, form.getContentHtml(), form.getAssetIds());
            flash.addFlashAttribute("flashSuccess", "Đã gửi phản hồi");
        } catch (RuntimeException exception) {
            flash.addFlashAttribute("flashError", exception.getMessage());
        }
        return "redirect:/student/tickets/" + id;
    }

    @PostMapping("/student/tickets/{id}/cancel")
    // Kiểm tra lý do và hủy yêu cầu của sinh viên.
    public String cancel(@PathVariable Long id, @Valid TicketActionForm form, BindingResult result,
                         RedirectAttributes flash) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("flashError", result.getAllErrors().getFirst().getDefaultMessage());
            return "redirect:/student/tickets/" + id;
        }
        try {
            workflow.cancel(id, SecurityUtils.requireUserId(), form.getReason());
            flash.addFlashAttribute("flashSuccess", "Đã hủy yêu cầu");
        } catch (AppException exception) {
            flash.addFlashAttribute("flashError", exception.getMessage());
        }
        return "redirect:/student/tickets/" + id;
    }

    @PostMapping("/student/tickets/{id}/confirm")
    // Xác nhận yêu cầu đã được giải quyết.
    public String confirm(@PathVariable Long id, RedirectAttributes flash) {
        try {
            workflow.confirmClose(id, SecurityUtils.requireUserId());
            flash.addFlashAttribute("flashSuccess", "Đã xác nhận yêu cầu được giải quyết");
        } catch (AppException exception) {
            flash.addFlashAttribute("flashError", exception.getMessage());
        }
        return "redirect:/student/tickets/" + id;
    }

    @PostMapping("/student/tickets/{id}/reopen")
    // Kiểm tra lý do và yêu cầu mở lại yêu cầu đã đóng.
    public String reopen(@PathVariable Long id, @Valid TicketActionForm form, BindingResult result,
                         RedirectAttributes flash) {
        String reason = form.getReason() == null ? "" : form.getReason().trim();
        if (result.hasErrors() || reason.length() < 10) {
            flash.addFlashAttribute("flashError", "Lý do mở lại phải từ 10 đến 500 ký tự");
            return "redirect:/student/tickets/" + id;
        }
        try {
            workflow.reopen(id, SecurityUtils.requireUserId(), reason);
            flash.addFlashAttribute("flashSuccess", "Đã mở lại yêu cầu");
        } catch (AppException exception) {
            flash.addFlashAttribute("flashError", exception.getMessage());
        }
        return "redirect:/student/tickets/" + id;
    }

    @PostMapping("/student/tickets/{id}/review")
    // Kiểm tra và lưu đánh giá của sinh viên về yêu cầu.
    public String review(@PathVariable Long id, @Valid @ModelAttribute("reviewForm") TicketReviewForm form,
                         BindingResult result, RedirectAttributes flash) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("flashError", result.getAllErrors().getFirst().getDefaultMessage());
            return "redirect:/student/tickets/" + id;
        }
        try {
            reviews.saveReview(SecurityUtils.requireUserId(), id, form);
            flash.addFlashAttribute("flashSuccess", "Đã lưu đánh giá của bạn");
        } catch (AppException exception) {
            flash.addFlashAttribute("flashError", exception.getMessage());
        }
        return "redirect:/student/tickets/" + id;
    }
}
