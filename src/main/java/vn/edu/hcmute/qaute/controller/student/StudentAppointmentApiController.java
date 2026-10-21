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

@RestController
@PreAuthorize("hasRole('STUDENT')")
public class StudentAppointmentApiController {

    private final AppointmentService appointmentService;

    public StudentAppointmentApiController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/api/appointments/slots")
    public ApiResponse<List<SlotView>> slots(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long managerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Modality modality) {
        return ApiResponse.ok(appointmentService.findAvailableSlots(categoryId, managerId, date, modality));
    }

    @GetMapping("/api/appointments/advisors")
    public ApiResponse<List<ManagerOption>> advisors(@RequestParam(required = false) Long categoryId) {
        return ApiResponse.ok(appointmentService.findAdvisors(categoryId));
    }
}
