package vn.edu.hcmute.qaute.controller.student;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmute.qaute.common.constant.Modality;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.dto.view.ManagerOption;
import vn.edu.hcmute.qaute.dto.view.SlotView;
import vn.edu.hcmute.qaute.service.appointment.AppointmentService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho StudentAppointmentApi.
@RestController
@PreAuthorize("hasRole('STUDENT')")
public class StudentAppointmentApiController {

    private final AppointmentService appointmentService;

    // Khởi tạo bộ điều khiển tra cứu lịch hẹn.
    public StudentAppointmentApiController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/api/appointments/slots")
    // Tra cứu các khung giờ còn trống theo bộ lọc.
    public ApiResponse<List<SlotView>> slots(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long managerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Modality modality) {
        return ApiResponse.ok(appointmentService.findAvailableSlots(categoryId, managerId, date, modality));
    }

    @GetMapping("/api/appointments/advisors")
    // Lấy danh sách cố vấn theo danh mục đã chọn.
    public ApiResponse<List<ManagerOption>> advisors(@RequestParam(required = false) Long categoryId) {
        return ApiResponse.ok(appointmentService.findAdvisors(categoryId));
    }
}
