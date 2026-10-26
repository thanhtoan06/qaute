package vn.edu.hcmute.qaute.service.appointment;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.hcmute.qaute.common.constant.Modality;
import vn.edu.hcmute.qaute.dto.view.AppointmentView;
import vn.edu.hcmute.qaute.dto.view.BookAppointmentCommand;
import vn.edu.hcmute.qaute.dto.view.ManagerOption;
import vn.edu.hcmute.qaute.dto.view.SlotView;

// Xử lý nghiệp vụ Appointment trong hệ thống.
public interface AppointmentService {
    // Tìm tư vấn viên theo chuyên mục.
    List<ManagerOption> findAdvisors(Long categoryId);

    // Tìm khung giờ trống theo chuyên mục, tư vấn viên, ngày và hình thức.
    List<SlotView> findAvailableSlots(Long categoryId, Long managerId, LocalDate date, Modality modality);

    // Đặt lịch hẹn theo thông tin được cung cấp.
    AppointmentView book(Long studentId, BookAppointmentCommand cmd);

    // Liệt kê lịch hẹn của sinh viên theo tab.
    Page<AppointmentView> listMine(Long studentId, String tab, Pageable pageable);

    // Lấy lịch hẹn của sinh viên theo mã lịch.
    AppointmentView getMine(Long studentId, Long appointmentId);

    // Hủy lịch hẹn theo yêu cầu của sinh viên.
    void cancelByStudent(Long studentId, Long appointmentId, String reason);

    // Đổi lịch hẹn sang khung giờ mới.
    AppointmentView reschedule(Long studentId, Long appointmentId, Long newSlotId);

    // Tạo nội dung lịch hẹn theo định dạng iCalendar.
    String buildIcs(Long studentId, Long appointmentId);
}
