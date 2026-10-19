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
import vn.edu.hcmute.qaute.dto.request.student.TicketReplyForm;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.student.StudentTicketDetailService;

@Controller
public class StudentTicketDetailController {
    private final StudentTicketDetailService details;

    public StudentTicketDetailController(StudentTicketDetailService details) {
        this.details = details;
    }

    @GetMapping("/student/tickets/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("detail", details.getDetail(SecurityUtils.requireUserId(), id));
        if (!model.containsAttribute("replyForm")) model.addAttribute("replyForm", new TicketReplyForm());
        return "student/ticket-detail";
    }

    @PostMapping("/student/tickets/{id}/reply")
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
}
