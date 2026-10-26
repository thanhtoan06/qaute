package vn.edu.hcmute.qaute.controller.manager;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.web.FormErrors;
import vn.edu.hcmute.qaute.dto.request.manager.AppointmentCancelForm;
import vn.edu.hcmute.qaute.dto.request.manager.AppointmentOutcomeForm;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.manager.ManagerAppointmentService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho ManagerAppointment.
@Controller
@RequestMapping("/manager/appointments")
public class ManagerAppointmentController {
    private final ManagerAppointmentService service;

    // Khởi tạo bộ điều khiển lịch hẹn của quản lý.
    public ManagerAppointmentController(ManagerAppointmentService service) {
        this.service = service;
    }

    @GetMapping
    // Hiển thị danh sách lịch hẹn theo tab và trang.
    public String list(@RequestParam(defaultValue = "today") String tab,
            @RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("tab", tab);
        model.addAttribute("result", service.list(SecurityUtils.requireUserId(), tab,
                PageRequest.of(Math.max(0, page), 15)));
        return "manager/appointments";
    }

    @GetMapping("/{id}")
    // Hiển thị chi tiết lịch hẹn.
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("appointment", service.detail(id, SecurityUtils.requireUserId()));
        if (!model.containsAttribute("outcomeForm")) {
            model.addAttribute("outcomeForm", new AppointmentOutcomeForm());
        }
        return "manager/appointment-detail";
    }

    @PostMapping("/{id}/complete")
    // Xác nhận hoàn tất lịch hẹn và lưu kết quả.
    public String complete(@PathVariable Long id,
            @Valid @ModelAttribute("outcomeForm") AppointmentOutcomeForm form,
            BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            redirect.addFlashAttribute("outcomeForm", form);
            redirect.addFlashAttribute("outcomeErrors", FormErrors.of(result));
            return detailRedirect(id);
        }
        try {
            service.complete(id, SecurityUtils.requireUserId(), form);
            redirect.addFlashAttribute("flashSuccess", "Đã hoàn tất lịch hẹn");
        } catch (AppException exception) {
            redirect.addFlashAttribute("flashError", exception.getMessage());
        }
        return detailRedirect(id);
    }

    @PostMapping("/{id}/no-show")
    // Đánh dấu sinh viên vắng mặt trong lịch hẹn.
    public String noShow(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.markNoShow(id, SecurityUtils.requireUserId());
            redirect.addFlashAttribute("flashSuccess", "Đã đánh dấu sinh viên vắng mặt");
        } catch (AppException exception) {
            redirect.addFlashAttribute("flashError", exception.getMessage());
        }
        return detailRedirect(id);
    }

    @PostMapping("/{id}/cancel")
    // Hủy lịch hẹn và thông báo kết quả.
    public String cancel(@PathVariable Long id,
            @Valid @ModelAttribute AppointmentCancelForm form, BindingResult result,
            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            redirect.addFlashAttribute("flashError", "Lý do hủy phải dài 10–500 ký tự");
            return detailRedirect(id);
        }
        try {
            service.cancel(id, SecurityUtils.requireUserId(), form.getReason());
            redirect.addFlashAttribute("flashSuccess", "Đã hủy lịch và thông báo cho sinh viên");
        } catch (AppException exception) {
            redirect.addFlashAttribute("flashError", exception.getMessage());
        }
        return detailRedirect(id);
    }

    // Tạo đường dẫn quay lại trang chi tiết lịch hẹn.
    private String detailRedirect(Long id) {
        return "redirect:/manager/appointments/" + id;
    }
}
