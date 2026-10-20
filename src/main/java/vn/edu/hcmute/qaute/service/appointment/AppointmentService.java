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

public interface AppointmentService {
    List<ManagerOption> findAdvisors(Long categoryId);
    List<SlotView> findAvailableSlots(Long categoryId, Long managerId, LocalDate date, Modality modality);
    AppointmentView book(Long studentId, BookAppointmentCommand cmd);
    Page<AppointmentView> listMine(Long studentId, String tab, Pageable pageable);
    AppointmentView getMine(Long studentId, Long appointmentId);
    void cancelByStudent(Long studentId, Long appointmentId, String reason);
    AppointmentView reschedule(Long studentId, Long appointmentId, Long newSlotId);
    String buildIcs(Long studentId, Long appointmentId);
}
