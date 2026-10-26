package vn.edu.hcmute.qaute.controller.manager;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.dto.request.admin.TagNameForm;
import vn.edu.hcmute.qaute.dto.request.manager.TicketNoteForm;
import vn.edu.hcmute.qaute.dto.view.TicketDraftView;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.manager.TicketNoteService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho ManagerTicketNote.
@Controller
public class ManagerTicketNoteController {
    private final TicketNoteService service;

    // Khởi tạo bộ điều khiển ghi chú nội bộ của yêu cầu.
    public ManagerTicketNoteController(TicketNoteService service) {
        this.service = service;
    }

    @PostMapping("/manager/tickets/{id}/notes")
    // Thêm ghi chú nội bộ vào yêu cầu.
    public String addNote(@PathVariable Long id, @Valid @ModelAttribute TicketNoteForm form, BindingResult errors,
            RedirectAttributes flash) {
        if (errors.hasErrors()) return invalid(id, errors, flash);
        return run(id, flash, () -> service.addNote(id, SecurityUtils.requireUserId(), form.getContentHtml()),
                "Đã thêm ghi chú nội bộ");
    }

    @PostMapping("/manager/notes/{noteId}/pin")
    // Ghim hoặc bỏ ghim ghi chú.
    public String pin(@PathVariable Long noteId, RedirectAttributes flash) {
        try {
            Long ticketId = service.togglePin(noteId, SecurityUtils.requireUserId());
            flash.addFlashAttribute("flashSuccess", "Đã cập nhật ghi chú ghim");
            return redirect(ticketId);
        } catch (AppException exception) {
            flash.addFlashAttribute("flashError", exception.getMessage());
            return "redirect:/manager/queue";
        }
    }

    @PostMapping("/manager/notes/{noteId}/delete")
    // Xóa ghi chú nội bộ được chọn.
    public String deleteNote(@PathVariable Long noteId, RedirectAttributes flash) {
        try {
            Long ticketId = service.deleteNote(noteId, SecurityUtils.requireUserId());
            flash.addFlashAttribute("flashSuccess", "Đã xóa ghi chú nội bộ");
            return redirect(ticketId);
        } catch (AppException exception) {
            flash.addFlashAttribute("flashError", exception.getMessage());
            return "redirect:/manager/queue";
        }
    }

    @PostMapping("/manager/tickets/{id}/tags")
    // Thêm nhãn cho yêu cầu.
    public String addTag(@PathVariable Long id, @Valid @ModelAttribute TagNameForm form, BindingResult errors,
            RedirectAttributes flash) {
        if (errors.hasErrors()) return invalid(id, errors, flash);
        return run(id, flash, () -> service.addTag(id, SecurityUtils.requireUserId(), form.getName()),
                "Đã thêm tag");
    }

    @PostMapping("/manager/tickets/{id}/tags/{tagId}/remove")
    // Gỡ nhãn khỏi yêu cầu.
    public String removeTag(@PathVariable Long id, @PathVariable Long tagId,
            RedirectAttributes flash) {
        return run(id, flash, () -> service.removeTag(id, SecurityUtils.requireUserId(), tagId),
                "Đã xóa tag khỏi yêu cầu");
    }

    @GetMapping("/api/manager/tickets/{id}/draft")
    @ResponseBody
    // Lấy bản nháp phản hồi của yêu cầu.
    public ApiResponse<TicketDraftView> getDraft(@PathVariable Long id) {
        return ApiResponse.ok(service.getDraft(id, SecurityUtils.requireUserId()));
    }

    @PutMapping("/api/manager/tickets/{id}/draft")
    @ResponseBody
    // Lưu bản nháp phản hồi của yêu cầu.
    public ApiResponse<TicketDraftView> saveDraft(@PathVariable Long id,
            @Valid @RequestBody DraftRequest request) {
        return ApiResponse.ok(service.saveDraft(id, SecurityUtils.requireUserId(), request.contentHtml()));
    }

    public record DraftRequest(@Size(max = 20000, message = "Bản nháp không quá 20000 ký tự") String contentHtml) { }

    // Hiển thị lỗi biểu mẫu và quay lại chi tiết yêu cầu.
    private String invalid(Long id, BindingResult errors, RedirectAttributes flash) {
        flash.addFlashAttribute("flashError", errors.getAllErrors().getFirst().getDefaultMessage());
        return redirect(id);
    }

    // Thực hiện thao tác ghi chú hoặc nhãn và đặt thông báo kết quả.
    private String run(Long ticketId, RedirectAttributes flash, Runnable action, String success) {
        try {
            action.run();
            flash.addFlashAttribute("flashSuccess", success);
        } catch (AppException exception) {
            flash.addFlashAttribute("flashError", exception.getMessage());
        }
        return redirect(ticketId);
    }

    // Tạo đường dẫn quay lại chi tiết yêu cầu.
    private static String redirect(Long ticketId) {
        return "redirect:/manager/tickets/" + ticketId;
    }
}
