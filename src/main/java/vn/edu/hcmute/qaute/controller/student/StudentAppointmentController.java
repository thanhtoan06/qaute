package vn.edu.hcmute.qaute.controller.student;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.dto.view.AppointmentView;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.appointment.AppointmentService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho StudentAppointment.
@Controller
public class StudentAppointmentController {
    private static final DateTimeFormatter APPOINTMENT_TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    private final AppointmentService appointmentService;

    // Khởi tạo bộ điều khiển quản lý lịch hẹn của sinh viên.
    public StudentAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/student/appointments")
    // Hiển thị danh sách lịch hẹn theo trạng thái và trang.
    public String list(@RequestParam(defaultValue = "upcoming") String tab,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model,
                       RedirectAttributes flash) {
        try {
            Long studentId = SecurityUtils.requireUserId();
            String currentTab = tab;
            if (!"upcoming".equals(currentTab) && !"past".equals(currentTab) && !"cancelled".equals(currentTab)) {
                currentTab = "upcoming";
            }
            Page<AppointmentView> result = appointmentService.listMine(
                    studentId, currentTab, PageRequest.of(Math.max(0, page), Math.min(100, Math.max(1, size))));
            model.addAttribute("appointments", result);
            model.addAttribute("tab", currentTab);
            return "student/appointment-list";
        } catch (AppException e) {
            flash.addFlashAttribute("flashError", e.getMessage());
            return "redirect:/student/dashboard";
        }
    }

    @GetMapping("/student/appointments/{id}")
    // Hiển thị thông tin chi tiết lịch hẹn.
    public String detail(@PathVariable Long id, Model model, RedirectAttributes flash) {
        try {
            Long studentId = SecurityUtils.requireUserId();
            AppointmentView appointment = appointmentService.getMine(studentId, id);
            model.addAttribute("appointment", appointment);
            return "student/appointment-detail";
        } catch (AppException e) {
            flash.addFlashAttribute("flashError", e.getMessage());
            return "redirect:/student/appointments";
        }
    }

    @PostMapping("/student/appointments/{id}/cancel")
    // Hủy lịch hẹn sau khi kiểm tra lý do và chuyển hướng an toàn.
    public String cancel(@PathVariable Long id,
                         @RequestParam(required = false) String reason,
                         @RequestParam(required = false) String redirectUrl,
                         RedirectAttributes flash) {
        String target = "redirect:" + safeRedirectUrl(redirectUrl, id);
        if (reason == null || reason.trim().length() < 10 || reason.trim().length() > 500) {
            flash.addFlashAttribute("flashError", "Lý do hủy phải từ 10 đến 500 ký tự");
            return target;
        }
        try {
            appointmentService.cancelByStudent(SecurityUtils.requireUserId(), id, reason.trim());
            flash.addFlashAttribute("flashSuccess", "Đã hủy lịch hẹn thành công");
        } catch (AppException e) {
            flash.addFlashAttribute("flashError", e.getMessage());
        }
        return target;
    }

    @GetMapping("/student/appointments/{id}/reschedule")
    // Hiển thị biểu mẫu đổi lịch nếu lịch hẹn cho phép.
    public String rescheduleForm(@PathVariable Long id, Model model, RedirectAttributes flash) {
        try {
            Long studentId = SecurityUtils.requireUserId();
            AppointmentView appointment = appointmentService.getMine(studentId, id);
            if (!appointment.canReschedule()) {
                flash.addFlashAttribute("flashError", "Lịch hẹn này không thể đổi lịch");
                return "redirect:/student/appointments/" + id;
            }
            model.addAttribute("appointment", appointment);
            return "student/appointment-reschedule";
        } catch (AppException e) {
            flash.addFlashAttribute("flashError", e.getMessage());
            return "redirect:/student/appointments";
        }
    }

    @PostMapping("/student/appointments/{id}/reschedule")
    // Đổi lịch hẹn sang khung giờ mới đã chọn.
    public String reschedule(@PathVariable Long id,
                             @RequestParam(required = false) Long newSlotId,
                             RedirectAttributes flash) {
        if (newSlotId == null) {
            flash.addFlashAttribute("flashError", "Vui lòng chọn khung giờ mới");
            return "redirect:/student/appointments/" + id + "/reschedule";
        }
        try {
            AppointmentView updated = appointmentService.reschedule(SecurityUtils.requireUserId(), id, newSlotId);
            flash.addFlashAttribute("flashSuccess", "Đã đổi lịch hẹn thành công sang "
                    + updated.startAt().format(APPOINTMENT_TIME_FORMAT));
            return "redirect:/student/appointments/" + id;
        } catch (AppException e) {
            String msg = e.getCode() == ErrorCode.ERR_SLOT_TAKEN
                    ? "Khung giờ vừa có người đặt, hãy chọn giờ khác"
                    : e.getMessage();
            flash.addFlashAttribute("flashError", msg);
            return "redirect:/student/appointments/" + id + "/reschedule";
        }
    }

    @GetMapping("/student/appointments/{id}/ics")
    // Tạo tệp lịch ICS hoặc chuyển về trang chi tiết khi thất bại.
    public Object downloadIcs(@PathVariable Long id, RedirectAttributes flash) {
        try {
            String ics = appointmentService.buildIcs(SecurityUtils.requireUserId(), id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/calendar; charset=UTF-8")
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"lich-hen-" + id + ".ics\"")
                    .body(ics.getBytes(StandardCharsets.UTF_8));
        } catch (AppException e) {
            flash.addFlashAttribute("flashError", e.getMessage());
            return "redirect:/student/appointments/" + id;
        }
    }

    /**
     * Forms on both the list and detail page may choose their return location.  Do
     * not pass an arbitrary client supplied URL to a redirect, otherwise a cancel
     * request can be used as an open-redirect endpoint.
     */
    // Chỉ chấp nhận đường dẫn quay lại danh sách lịch hẹn nội bộ.
    private String safeRedirectUrl(String redirectUrl, Long appointmentId) {
        if ("/student/appointments".equals(redirectUrl)) {
            return redirectUrl;
        }
        return "/student/appointments/" + appointmentId;
    }
}
