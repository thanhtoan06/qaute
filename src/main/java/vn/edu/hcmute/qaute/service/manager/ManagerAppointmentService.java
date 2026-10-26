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
import vn.edu.hcmute.qaute.common.constant.RoleCode;
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
import vn.edu.hcmute.qaute.service.ticket.TicketAccessService;
import vn.edu.hcmute.qaute.common.util.BoundedPages;

// Xử lý nghiệp vụ ManagerAppointment trong hệ thống.
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
    private final TicketAccessService ticketAccess;

    // Khởi tạo dịch vụ quản lý lịch hẹn của tư vấn viên.
    public ManagerAppointmentService(AppointmentRepository appointments, AppointmentSlotRepository slots,
            TicketRepository tickets, UserLookupService users, CategoryQueryService categories,
            NotificationService notifications, AuditService audit, TicketAccessService ticketAccess) {
        this.appointments = appointments;
        this.slots = slots;
        this.tickets = tickets;
        this.users = users;
        this.categories = categories;
        this.notifications = notifications;
        this.audit = audit;
        this.ticketAccess = ticketAccess;
    }

    // Liệt kê lịch hẹn theo tab và trang được yêu cầu.
    @Transactional(readOnly = true)
    public Page<ManagerAppointmentView> list(Long managerId, String tab, Pageable pageable) {
        pageable = BoundedPages.of(pageable);
        LocalDateTime today = LocalDate.now().atStartOfDay();
        Page<Appointment> result = switch (tab == null ? "today" : tab) {
            case "today" -> appointments.findManagerToday(managerId, today, today.plusDays(1), pageable);
            case "upcoming" -> appointments.findManagerUpcoming(managerId, today.plusDays(1), pageable);
            case "past" -> appointments.findManagerPast(managerId, today, pageable);
            default -> throw new BadRequestException("Tab lịch hẹn không hợp lệ");
        };
        return result.map(this::toView);
    }

    // Lấy chi tiết lịch hẹn thuộc tư vấn viên.
    @Transactional(readOnly = true)
    public ManagerAppointmentView detail(Long id, Long managerId) {
        return toView(requireOwn(id, managerId));
    }

    // Hoàn tất lịch hẹn và lưu kết luận tư vấn.
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
        audit.log(AuditActions.APPOINTMENT_COMPLETED, "Appointment", appointment.getId(),
                "CONFIRMED", "COMPLETED");
    }

    // Đánh dấu lịch hẹn đã xác nhận là sinh viên vắng mặt.
    public void markNoShow(Long id, Long managerId) {
        Appointment appointment = requireOwn(id, managerId);
        requireStarted(appointment, requireSlot(appointment.getSlotId()));
        appointment.setStatus(AppointmentStatus.NO_SHOW);
        appointments.saveAndFlush(appointment);
        audit.log(AuditActions.APPOINTMENT_NO_SHOW, "Appointment", appointment.getId(),
                "CONFIRMED", "NO_SHOW");
    }

    // Hủy lịch hẹn đã xác nhận và thông báo cho sinh viên.
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

    // Lấy lịch hẹn thuộc về tư vấn viên.
    private Appointment requireOwn(Long id, Long managerId) {
        return appointments.findById(id).filter(item -> Objects.equals(item.getManagerId(), managerId))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy lịch hẹn"));
    }

    // Lấy khung giờ của lịch hẹn.
    private AppointmentSlot requireSlot(Long id) {
        return slots.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy khung giờ"));
    }

    // Kiểm tra lịch hẹn đã xác nhận và đã đến giờ bắt đầu.
    private void requireStarted(Appointment appointment, AppointmentSlot slot) {
        if (appointment.getStatus() != AppointmentStatus.CONFIRMED || slot.getStartAt().isAfter(LocalDateTime.now())) {
            throw new InvalidTransitionException("Chỉ có thể kết thúc lịch đã bắt đầu và đang xác nhận");
        }
    }

    // Chuyển lịch hẹn thành dữ liệu hiển thị cho tư vấn viên.
    private ManagerAppointmentView toView(Appointment appointment) {
        AppointmentSlot slot = requireSlot(appointment.getSlotId());
        LocalDateTime now = LocalDateTime.now();
        boolean confirmed = appointment.getStatus() == AppointmentStatus.CONFIRMED;
        boolean started = !slot.getStartAt().isAfter(now);
        Long ticketId = appointment.getTicketId();
        boolean visibleTicket = ticketId != null
                && ticketAccess.canView(ticketId, appointment.getManagerId(), RoleCode.MANAGER);
        String ticketCode = !visibleTicket ? null
                : tickets.findById(ticketId).map(Ticket::getCode).orElse(null);
        return new ManagerAppointmentView(appointment.getId(), slot.getId(), appointment.getStudentId(),
                users.getStudentSummary(appointment.getStudentId()), appointment.getCategoryId(),
                appointment.getCategoryId() == null ? "" : categories.getDisplayPath(appointment.getCategoryId()),
                visibleTicket ? ticketId : null, ticketCode, appointment.getTopic(), appointment.getNote(),
                appointment.getModality(), slot.getLocation(), slot.getMeetingLink(), slot.getStartAt(),
                slot.getEndAt(), appointment.getStatus(), appointment.getOutcomeSummary(),
                appointment.getStudentVisibleSummary(), appointment.getCancelReason(),
                confirmed && started, confirmed && started, confirmed && !started);
    }
}
