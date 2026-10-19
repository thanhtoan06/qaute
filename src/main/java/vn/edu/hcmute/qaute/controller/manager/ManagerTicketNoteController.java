package vn.edu.hcmute.qaute.controller.manager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.dto.view.TicketDraftView;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.manager.TicketNoteService;

@Controller
public class ManagerTicketNoteController {
    private final TicketNoteService service;

    public ManagerTicketNoteController(TicketNoteService service) {
        this.service = service;
    }

    @PostMapping("/manager/tickets/{id}/notes")
    public String addNote(@PathVariable Long id, @RequestParam String contentHtml,
            RedirectAttributes flash) {
        return run(id, flash, () -> service.addNote(id, SecurityUtils.requireUserId(), contentHtml),
                "Đã thêm ghi chú nội bộ");
    }

    @PostMapping("/manager/notes/{noteId}/pin")
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
    public String addTag(@PathVariable Long id, @RequestParam String name,
            RedirectAttributes flash) {
        return run(id, flash, () -> service.addTag(id, SecurityUtils.requireUserId(), name),
                "Đã thêm tag");
    }

    @PostMapping("/manager/tickets/{id}/tags/{tagId}/remove")
    public String removeTag(@PathVariable Long id, @PathVariable Long tagId,
            RedirectAttributes flash) {
        return run(id, flash, () -> service.removeTag(id, SecurityUtils.requireUserId(), tagId),
                "Đã xóa tag khỏi yêu cầu");
    }

    @GetMapping("/api/manager/tickets/{id}/draft")
    @ResponseBody
    public ApiResponse<TicketDraftView> getDraft(@PathVariable Long id) {
        return ApiResponse.ok(service.getDraft(id, SecurityUtils.requireUserId()));
    }

    @PutMapping("/api/manager/tickets/{id}/draft")
    @ResponseBody
    public ApiResponse<TicketDraftView> saveDraft(@PathVariable Long id,
            @RequestBody DraftRequest request) {
        return ApiResponse.ok(service.saveDraft(id, SecurityUtils.requireUserId(), request.contentHtml()));
    }

    public record DraftRequest(String contentHtml) { }

    private String run(Long ticketId, RedirectAttributes flash, Runnable action, String success) {
        try {
            action.run();
            flash.addFlashAttribute("flashSuccess", success);
        } catch (AppException exception) {
            flash.addFlashAttribute("flashError", exception.getMessage());
        }
        return redirect(ticketId);
    }

    private static String redirect(Long ticketId) {
        return "redirect:/manager/tickets/" + ticketId;
    }
}
