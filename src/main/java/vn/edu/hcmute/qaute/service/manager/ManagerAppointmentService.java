package vn.edu.hcmute.qaute.service.manager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.InvalidTransitionException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.manager.AppointmentOutcomeForm;
import vn.edu.hcmute.qaute.dto.view.ManagerAppointmentView;
import vn.edu.hcmute.qaute.entity.appointment.Appointment;
import vn.edu.hcmute.qaute.entity.appointment.AppointmentSlot;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentRepository;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentSlotRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.notification.NotificationService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@Service
@Transactional
public class ManagerAppointmentService {
    private final AppointmentRepository appointments;
    private final AppointmentSlotRepository slots;
    private final TicketRepository tickets;
    private final UserLookupService users;
    private final CategoryQueryService categories;
    private final NotificationService notifications;
    private final AuditService audit;

    public ManagerAppointmentService(AppointmentRepository appointments, AppointmentSlotRepository slots,
            TicketRepository tickets, UserLookupService users, CategoryQueryService categories,
            NotificationService notifications, AuditService audit) {
        this.appointments = appointments;
        this.slots = slots;
        this.tickets = tickets;
        this.users = users;
        this.categories = categories;
        this.notifications = notifications;
        this.audit = audit;
    }

    @Transactional(readOnly = true)
    public Page<ManagerAppointmentView> list(Long managerId, String tab, Pageable pageable) {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        Page<Appointment> result = switch (tab == null ? "today" : tab) {
            case "today" -> appointments.findManagerToday(managerId, today, today.plusDays(1), pageable);
            case "upcoming" -> appointments.findManagerUpcoming(managerId, today.plusDays(1), pageable);
            case "past" -> appointments.findManagerPast(managerId, today, pageable);
            default -> throw new BadRequestException("Tab lịch hẹn không hợp lệ");
        };
        return result.map(this::toView);
    }

    @Transactional(readOnly = true)
    public ManagerAppointmentView detail(Long id, Long managerId) {
        return toView(requireOwn(id, managerId));
    }

    public void complete(Long id, Long managerId, AppointmentOutcomeForm form) {
        Appointment appointment = requireOwn(id, managerId);
        AppointmentSlot slot = requireSlot(appointment.getSlotId());
        requireStarted(appointment, slot);
        String outcome = form == null ? null : form.getOutcomeSummary();
        String visible = form == null ? null : form.getStudentVisibleSummary();
        if (outcome == null || outcome.trim().length() < 10 || outcome.trim().length() > 2000
                || visible != null && visible.trim().length() > 1000) {
            throw new BadRequestException("Kết luận phải dài 10–2000 ký tự; nội dung cho sinh viên tối đa 1000 ký tự");
        }
        appointment.setOutcomeSummary(outcome.trim());
        appointment.setStudentVisibleSummary(visible == null || visible.isBlank() ? null : visible.trim());
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointments.saveAndFlush(appointment);
    }

    public void markNoShow(Long id, Long managerId) {
        Appointment appointment = requireOwn(id, managerId);
        requireStarted(appointment, requireSlot(appointment.getSlotId()));
        appointment.setStatus(AppointmentStatus.NO_SHOW);
        appointments.saveAndFlush(appointment);
    }

    public void cancel(Long id, Long managerId, String reason) {
        if (reason == null || reason.trim().length() < 10 || reason.trim().length() > 500) {
            throw new BadRequestException("Lý do hủy phải dài 10–500 ký tự");
        }
        Appointment appointment = requireOwn(id, managerId);
        AppointmentSlot slot = requireSlot(appointment.getSlotId());
        if (appointment.getStatus() != AppointmentStatus.CONFIRMED || !slot.getStartAt().isAfter(LocalDateTime.now())) {
            throw new InvalidTransitionException("Chỉ có thể hủy lịch đã xác nhận trước giờ bắt đầu");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancelledBy(managerId);
        appointment.setCancelReason(reason.trim());
        appointments.saveAndFlush(appointment);
        slots.releaseBooked(slot.getId(), LocalDateTime.now());
        notifications.notifyUser(appointment.getStudentId(), NotificationType.APPOINTMENT_CANCELLED,
                "Lịch hẹn đã bị hủy", "Tư vấn viên đã hủy lịch hẹn. Vui lòng đặt lịch mới: " + appointment.getTopic(),
                "/student/appointments", "APPOINTMENT", appointment.getId());
        audit.log(AuditActions.APPOINTMENT_CANCELLED_BY_MANAGER, "Appointment", appointment.getId(),
                "CONFIRMED", "CANCELLED; reason=" + reason.trim());
    }

    private Appointment requireOwn(Long id, Long managerId) {
        return appointments.findById(id).filter(item -> Objects.equals(item.getManagerId(), managerId))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy lịch hẹn"));
    }

    private AppointmentSlot requireSlot(Long id) {
        return slots.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy khung giờ"));
    }

    private void requireStarted(Appointment appointment, AppointmentSlot slot) {
        if (appointment.getStatus() != AppointmentStatus.CONFIRMED || slot.getStartAt().isAfter(LocalDateTime.now())) {
            throw new InvalidTransitionException("Chỉ có thể kết thúc lịch đã bắt đầu và đang xác nhận");
        }
    }

    private ManagerAppointmentView toView(Appointment appointment) {
        AppointmentSlot slot = requireSlot(appointment.getSlotId());
        LocalDateTime now = LocalDateTime.now();
        boolean confirmed = appointment.getStatus() == AppointmentStatus.CONFIRMED;
        boolean started = !slot.getStartAt().isAfter(now);
        String ticketCode = appointment.getTicketId() == null ? null
                : tickets.findById(appointment.getTicketId()).map(Ticket::getCode).orElse(null);
        return new ManagerAppointmentView(appointment.getId(), slot.getId(), appointment.getStudentId(),
                users.getStudentSummary(appointment.getStudentId()), appointment.getCategoryId(),
                appointment.getCategoryId() == null ? "" : categories.getDisplayPath(appointment.getCategoryId()),
                appointment.getTicketId(), ticketCode, appointment.getTopic(), appointment.getNote(),
                appointment.getModality(), slot.getLocation(), slot.getMeetingLink(), slot.getStartAt(),
                slot.getEndAt(), appointment.getStatus(), appointment.getOutcomeSummary(),
                appointment.getStudentVisibleSummary(), appointment.getCancelReason(),
                confirmed && started, confirmed && started, confirmed && !started);
    }
}
