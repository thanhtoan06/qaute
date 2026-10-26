package vn.edu.hcmute.qaute.controller.student;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.student.TicketCreateForm;
import vn.edu.hcmute.qaute.dto.view.DepartmentNode;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.student.StudentTicketService;
import vn.edu.hcmute.qaute.service.ticket.command.CreateTicketCommand;

// Tiếp nhận và điều phối các yêu cầu giao diện cho StudentTicketCreate.
@Controller
public class StudentTicketCreateController {

    private final StudentTicketService studentTicketService;
    private final CategoryQueryService categoryQueryService;
    private final ObjectMapper objectMapper;

    // Khởi tạo bộ điều khiển tạo yêu cầu cùng dịch vụ và bộ chuyển đổi JSON.
    public StudentTicketCreateController(StudentTicketService studentTicketService,
                                        CategoryQueryService categoryQueryService,
                                        ObjectMapper objectMapper) {
        this.studentTicketService = studentTicketService;
        this.categoryQueryService = categoryQueryService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/student/tickets/new")
    // Hiển thị biểu mẫu tạo yêu cầu với dữ liệu danh mục ban đầu.
    public String newTicketForm(@RequestParam(required = false) Long categoryId,
                                @RequestParam(required = false) String q,
                                Model model) {
        List<DepartmentNode> tree = categoryQueryService.getActiveTree();
        model.addAttribute("tree", tree);
        model.addAttribute("categoriesJson", toJson(tree));

        if (!model.containsAttribute("form")) {
            TicketCreateForm form = new TicketCreateForm();
            form.setSubmitToken(UUID.randomUUID().toString());
            if (categoryId != null) {
                form.setCategoryId(categoryId);
            }
            if (q != null && !q.isBlank()) {
                form.setTitle(q.trim());
            }
            model.addAttribute("form", form);
        }
        return "student/ticket-create";
    }

    @PostMapping("/student/tickets")
    // Kiểm tra biểu mẫu và tạo yêu cầu mới cho sinh viên.
    public String createTicket(@Valid @ModelAttribute("form") TicketCreateForm form,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        Long studentId = SecurityUtils.requireUserId();

        if (form.isUrgentRequested()) {
            String reason = form.getUrgentReason() == null ? "" : form.getUrgentReason().trim();
            if (reason.length() < 10 || reason.length() > 500) {
                bindingResult.rejectValue("urgentReason", "Invalid", "Lý do khẩn phải từ 10 đến 500 ký tự");
            }
        }

        if (bindingResult.hasErrors()) {
            return returnWithError(model, FormErrors.of(bindingResult));
        }

        try {
            CreateTicketCommand command = new CreateTicketCommand(
                    form.getCategoryId(),
                    form.getTitle(),
                    form.getDescriptionHtml(),
                    form.isUrgentRequested(),
                    form.getUrgentReason(),
                    form.isAllowPublicFaq(),
                    form.getSubmitToken(),
                    form.getAssetIds() != null ? form.getAssetIds() : List.of()
            );
            Ticket ticket = studentTicketService.createTicket(studentId, command);
            redirectAttributes.addFlashAttribute("flashSuccess", "Đã gửi yêu cầu, mã " + ticket.getCode());
            return "redirect:/student/tickets/" + ticket.getId();
        } catch (BadRequestException e) {
            Map<String, String> errors = new LinkedHashMap<>(FormErrors.of(bindingResult));
            errors.putAll(e.getFieldErrors());
            if (e.getFieldErrors().isEmpty() && e.getMessage() != null) {
                errors.putIfAbsent("global", e.getMessage());
            }
            return returnWithError(model, errors);
        } catch (AppException e) {
            Map<String, String> errors = new LinkedHashMap<>(FormErrors.of(bindingResult));
            errors.putIfAbsent("global", e.getMessage());
            return returnWithError(model, errors);
        }
    }

    // Chuẩn bị lại dữ liệu trang tạo yêu cầu khi có lỗi.
    private String returnWithError(Model model, Map<String, String> errors) {
        List<DepartmentNode> tree = categoryQueryService.getActiveTree();
        model.addAttribute("errors", errors);
        model.addAttribute("tree", tree);
        model.addAttribute("categoriesJson", toJson(tree));
        return "student/ticket-create";
    }

    // Chuyển đổi dữ liệu sang JSON hoặc trả về mảng rỗng nếu không thể chuyển đổi.
    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }
}

