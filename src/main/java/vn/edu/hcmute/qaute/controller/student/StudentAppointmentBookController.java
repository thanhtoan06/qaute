package vn.edu.hcmute.qaute.controller.student;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.dto.request.student.AppointmentBookForm;
import vn.edu.hcmute.qaute.dto.view.AppointmentView;
import vn.edu.hcmute.qaute.dto.view.BookAppointmentCommand;
import vn.edu.hcmute.qaute.dto.view.DepartmentNode;
import vn.edu.hcmute.qaute.dto.view.TicketListItem;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.appointment.AppointmentService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.student.StudentTicketQueryService;
import vn.edu.hcmute.qaute.dto.request.student.TicketFilter;

// Tiếp nhận và điều phối các yêu cầu giao diện cho StudentAppointmentBook.
@Controller
public class StudentAppointmentBookController {

    private final AppointmentService appointmentService;
    private final CategoryQueryService categoryQueryService;
    private final StudentTicketQueryService ticketQueryService;

    // Khởi tạo bộ điều khiển đặt lịch và truy vấn dữ liệu liên quan.
    public StudentAppointmentBookController(AppointmentService appointmentService,
                                            CategoryQueryService categoryQueryService,
                                            StudentTicketQueryService ticketQueryService) {
        this.appointmentService = appointmentService;
        this.categoryQueryService = categoryQueryService;
        this.ticketQueryService = ticketQueryService;
    }

    @GetMapping("/student/appointments/book")
    // Hiển thị biểu mẫu đặt lịch cùng danh mục và yêu cầu đang mở.
    public String bookForm(Model model) {
        Long studentId = SecurityUtils.requireUserId();
        List<DepartmentNode> tree = categoryQueryService.getActiveTree();
        List<TicketListItem> openTickets = ticketQueryService.list(
                studentId, new TicketFilter(), PageRequest.of(0, 50)).getContent();
        model.addAttribute("tree", tree);
        model.addAttribute("openTickets", openTickets);
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new AppointmentBookForm());
        }
        return "student/appointment-book";
    }

    @PostMapping("/student/appointments/book")
    // Kiểm tra biểu mẫu và tạo lịch hẹn cho sinh viên.
    public String book(@Valid @ModelAttribute("form") AppointmentBookForm form,
                       BindingResult result,
                       RedirectAttributes flash) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("form", form);
            flash.addFlashAttribute("flashError",
                    result.getAllErrors().getFirst().getDefaultMessage());
            return "redirect:/student/appointments/book";
        }

        try {
            BookAppointmentCommand cmd = new BookAppointmentCommand(
                    form.getSlotId(),
                    form.getCategoryId(),
                    form.getTopic(),
                    form.getNote(),
                    form.getModality(),
                    form.getTicketId()
            );
            AppointmentView appt = appointmentService.book(SecurityUtils.requireUserId(), cmd);
            flash.addFlashAttribute("flashSuccess",
                    "Đã đặt lịch hẹn thành công lúc " + appt.startAt());
            return "redirect:/student/appointments";
        } catch (AppException e) {
            String msg = e.getCode() == ErrorCode.ERR_SLOT_TAKEN
                    ? "Khung giờ vừa có người đặt, hãy chọn giờ khác"
                    : e.getMessage();
            flash.addFlashAttribute("flashError", msg);
            flash.addFlashAttribute("form", form);
            return "redirect:/student/appointments/book";
        }
    }
}
