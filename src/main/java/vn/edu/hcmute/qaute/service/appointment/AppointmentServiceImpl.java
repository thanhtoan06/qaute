package vn.edu.hcmute.qaute.service.appointment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;
import vn.edu.hcmute.qaute.common.constant.Modality;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.constant.SlotStatus;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.InvalidTransitionException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.view.AppointmentView;
import vn.edu.hcmute.qaute.dto.view.BookAppointmentCommand;
import vn.edu.hcmute.qaute.dto.view.ManagerOption;
import vn.edu.hcmute.qaute.dto.view.SlotView;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.appointment.Appointment;
import vn.edu.hcmute.qaute.entity.appointment.AppointmentSlot;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentRepository;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentSlotRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.notification.NotificationService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    private static final DateTimeFormatter ICS_LOCAL = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
    private static final DateTimeFormatter ICS_UTC = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
    private final AppointmentRepository appointments;
    private final AppointmentSlotRepository slots;
    private final TicketRepository tickets;
    private final ManagerScopeService scopes;
    private final CategoryQueryService categories;
    private final UserLookupService users;
    private final SettingsService settings;
    private final NotificationService notifications;
    private final EntityManager entityManager;

    public AppointmentServiceImpl(AppointmentRepository appointments, AppointmentSlotRepository slots,
            TicketRepository tickets, ManagerScopeService scopes, CategoryQueryService categories,
            UserLookupService users, SettingsService settings, NotificationService notifications,
            EntityManager entityManager) {
        this.appointments = appointments;
        this.slots = slots;
        this.tickets = tickets;
        this.scopes = scopes;
        this.categories = categories;
        this.users = users;
        this.settings = settings;
        this.notifications = notifications;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManagerOption> findAdvisors(Long categoryId) {
        if (categoryId == null) return List.of();
        return scopes.findAdvisors(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SlotView> findAvailableSlots(Long categoryId, Long managerId, LocalDate date, Modality modality) {
        if (categoryId == null || date == null) return List.of();
        List<Long> managerIds = scopes.findManagerIdsInScope(categoryId);
        if (managerId != null) {
            if (!managerIds.contains(managerId)) return List.of();
            managerIds = List.of(managerId);
        }
        if (managerIds.isEmpty()) return List.of();
        LocalDateTime earliest = LocalDateTime.now().plusHours(settings.getInt(SettingKeys.APPOINTMENT_LEAD_HOURS));
        LocalDateTime from = date.atStartOfDay().isAfter(earliest) ? date.atStartOfDay() : earliest;
        LocalDateTime to = date.plusDays(1).atStartOfDay();
        if (!from.isBefore(to)) return List.of();
        List<AppointmentSlot> available = slots
                .findByManagerIdInAndStatusAndStartAtGreaterThanEqualAndStartAtLessThanOrderByStartAtAsc(
                        managerIds, SlotStatus.AVAILABLE, from, to).stream()
                .filter(slot -> modality == null || slot.getModality() == Modality.BOTH
                        || slot.getModality() == modality).toList();
        Map<Long, UserSummary> summaries = users.getSummaries(
                available.stream().map(AppointmentSlot::getManagerId).distinct().toList());
        return available.stream().map(slot -> {
            UserSummary manager = summaries.get(slot.getManagerId());
            return new SlotView(slot.getId(), slot.getManagerId(),
                    manager == null ? "" : manager.fullName(), slot.getStartAt(), slot.getEndAt(),
                    slot.getModality(), slot.getLocation());
        }).toList();
    }

    @Override
    public AppointmentView book(Long studentId, BookAppointmentCommand cmd) {
        if (cmd == null || cmd.slotId() == null || cmd.categoryId() == null) {
            throw new BadRequestException("Thiếu khung giờ hoặc chuyên mục");
        }
        validateText(cmd.topic(), cmd.note());
        lockStudent(studentId);
        AppointmentSlot slot = requireBookableSlot(cmd.slotId(), cmd.categoryId(), cmd.modality());
        if (cmd.ticketId() != null) {
            Ticket ticket = tickets.findById(cmd.ticketId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy yêu cầu liên quan"));
            if (!Objects.equals(ticket.getStudentId(), studentId)) {
                throw new NotFoundException("Không tìm thấy yêu cầu liên quan");
            }
        }
        LocalDateTime now = LocalDateTime.now();
        if (appointments.countUpcoming(studentId, now) >= settings.getInt(SettingKeys.APPOINTMENT_MAX_UPCOMING)) {
            throw new AppException(ErrorCode.ERR_LIMIT_EXCEEDED, "Bạn đã đạt giới hạn lịch hẹn sắp tới");
        }
        rejectStudentOverlap(studentId, null, slot);
        if (slots.markBooked(slot.getId()) == 0) throw slotTaken();
        Appointment appointment = new Appointment();
        appointment.setSlotId(slot.getId());
        appointment.setStudentId(studentId);
        appointment.setManagerId(slot.getManagerId());
        appointment.setCategoryId(cmd.categoryId());
        appointment.setTicketId(cmd.ticketId());
        appointment.setTopic(cmd.topic().trim());
        appointment.setNote(trimToNull(cmd.note()));
        appointment.setModality(resolveModality(slot, cmd.modality()));
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        try {
            appointments.saveAndFlush(appointment);
        } catch (DataIntegrityViolationException exception) {
            if (isActiveSlotConflict(exception)) throw slotTaken();
            throw exception;
        }
        notifyBoth(appointment, NotificationType.APPOINTMENT_CONFIRMED, "Lịch hẹn đã được xác nhận");
        return toView(appointment, slot);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentView> listMine(Long studentId, String tab, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        Page<Appointment> result = switch (tab == null ? "upcoming" : tab) {
            case "upcoming" -> appointments.findUpcoming(studentId, now, pageable);
            case "past" -> appointments.findPast(studentId, now, pageable);
            case "cancelled" -> appointments.findCancelled(studentId, pageable);
            default -> throw new BadRequestException("Tab lịch hẹn không hợp lệ");
        };
        return result.map(this::toView);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentView getMine(Long studentId, Long appointmentId) {
        return toView(requireMine(studentId, appointmentId));
    }

    @Override
    public void cancelByStudent(Long studentId, Long appointmentId, String reason) {
        validateReason(reason);
        lockStudent(studentId);
        Appointment appointment = requireMine(studentId, appointmentId);
        AppointmentSlot slot = requireSlot(appointment.getSlotId());
        requireChangeable(appointment, slot);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancelledBy(studentId);
        appointment.setCancelReason(reason.trim());
        appointments.saveAndFlush(appointment);
        slots.releaseBooked(slot.getId(), LocalDateTime.now());
        notifications.notifyUser(appointment.getManagerId(), NotificationType.APPOINTMENT_CANCELLED,
                "Lịch hẹn đã bị hủy", "Sinh viên đã hủy lịch hẹn: " + appointment.getTopic(),
                "/manager/appointments", "APPOINTMENT", appointment.getId());
    }

    @Override
    public AppointmentView reschedule(Long studentId, Long appointmentId, Long newSlotId) {
        if (newSlotId == null) throw new BadRequestException("Thiếu khung giờ mới");
        lockStudent(studentId);
        Appointment appointment = requireMine(studentId, appointmentId);
        AppointmentSlot oldSlot = requireSlot(appointment.getSlotId());
        requireChangeable(appointment, oldSlot);
        if (Objects.equals(newSlotId, oldSlot.getId())) throw new BadRequestException("Khung giờ mới phải khác khung giờ cũ");
        AppointmentSlot newSlot = requireBookableSlot(newSlotId, appointment.getCategoryId(), appointment.getModality());
        rejectStudentOverlap(studentId, appointmentId, newSlot);
        if (slots.markBooked(newSlotId) == 0) throw slotTaken();
        appointment.setSlotId(newSlotId);
        appointment.setManagerId(newSlot.getManagerId());
        appointment.setReminder24hSent(false);
        appointment.setReminder1hSent(false);
        try {
            appointments.saveAndFlush(appointment);
        } catch (DataIntegrityViolationException exception) {
            if (isActiveSlotConflict(exception)) throw slotTaken();
            throw exception;
        }
        slots.releaseBooked(oldSlot.getId(), LocalDateTime.now());
        notifyBoth(appointment, NotificationType.APPOINTMENT_CHANGED, "Lịch hẹn đã thay đổi");
        if (!Objects.equals(oldSlot.getManagerId(), newSlot.getManagerId())) {
            notifications.notifyUser(oldSlot.getManagerId(), NotificationType.APPOINTMENT_CHANGED,
                    "Lịch hẹn đã chuyển", "Lịch hẹn đã chuyển sang tư vấn viên khác",
                    "/manager/appointments", "APPOINTMENT", appointment.getId());
        }
        return toView(appointment, newSlot);
    }

    @Override
    @Transactional(readOnly = true)
    public String buildIcs(Long studentId, Long appointmentId) {
        Appointment appointment = requireMine(studentId, appointmentId);
        AppointmentSlot slot = requireSlot(appointment.getSlotId());
        StringBuilder ics = new StringBuilder();
        ics.append("BEGIN:VCALENDAR\r\nVERSION:2.0\r\nPRODID:-//QAUTE//Appointment//VI\r\n");
        ics.append("BEGIN:VEVENT\r\n");
        ics.append("UID:appointment-").append(appointment.getId()).append("@qaute.hcmute.edu.vn\r\n");
        ics.append("DTSTAMP:").append(LocalDateTime.now(ZoneOffset.UTC).format(ICS_UTC)).append("\r\n");
        ics.append("DTSTART;TZID=Asia/Ho_Chi_Minh:").append(slot.getStartAt().format(ICS_LOCAL)).append("\r\n");
        ics.append("DTEND;TZID=Asia/Ho_Chi_Minh:").append(slot.getEndAt().format(ICS_LOCAL)).append("\r\n");
        appendIcsLine(ics, "SUMMARY:" + escapeIcs("Tư vấn QAUTE: " + appointment.getTopic()));
        if (slot.getLocation() != null) appendIcsLine(ics, "LOCATION:" + escapeIcs(slot.getLocation()));
        if (slot.getMeetingLink() != null) appendIcsLine(ics, "URL:" + escapeIcs(slot.getMeetingLink()));
        appendIcsLine(ics, "DESCRIPTION:" + escapeIcs(appointment.getNote() == null ? "" : appointment.getNote()));
        ics.append("END:VEVENT\r\nEND:VCALENDAR\r\n");
        return ics.toString();
    }

    private AppointmentSlot requireBookableSlot(Long slotId, Long categoryId, Modality requested) {
        AppointmentSlot slot = requireSlot(slotId);
        if (slot.getStatus() != SlotStatus.AVAILABLE) throw slotTaken();
        if (slot.getStartAt().isBefore(LocalDateTime.now().plusHours(
                settings.getInt(SettingKeys.APPOINTMENT_LEAD_HOURS)))) {
            throw new BadRequestException("Đã quá thời hạn đặt lịch cho khung giờ này");
        }
        if (categoryId == null || !scopes.findManagerIdsInScope(categoryId).contains(slot.getManagerId())) {
            throw new BadRequestException("Tư vấn viên không phụ trách chuyên mục đã chọn");
        }
        resolveModality(slot, requested);
        return slot;
    }

    private Modality resolveModality(AppointmentSlot slot, Modality requested) {
        if (slot.getModality() == Modality.BOTH) {
            if (requested != Modality.ONLINE && requested != Modality.IN_PERSON) {
                throw new BadRequestException("Hãy chọn hình thức trực tuyến hoặc trực tiếp");
            }
            return requested;
        }
        if (requested != null && requested != slot.getModality()) {
            throw new BadRequestException("Hình thức không phù hợp với khung giờ");
        }
        return slot.getModality();
    }

    private void rejectStudentOverlap(Long studentId, Long excludeId, AppointmentSlot slot) {
        if (appointments.countOverlapping(studentId, excludeId, slot.getStartAt(), slot.getEndAt()) > 0) {
            throw new BadRequestException("Bạn đã có lịch hẹn trùng giờ");
        }
    }

    private void requireChangeable(Appointment appointment, AppointmentSlot slot) {
        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new InvalidTransitionException("Chỉ lịch đã xác nhận mới được thay đổi");
        }
        if (slot.getStartAt().isBefore(LocalDateTime.now().plusHours(
                settings.getInt(SettingKeys.APPOINTMENT_CANCEL_BEFORE_HOURS)))) {
            throw new BadRequestException("Đã quá thời hạn hủy hoặc đổi lịch");
        }
    }

    private void lockStudent(Long studentId) {
        if (studentId == null || entityManager.find(User.class, studentId, LockModeType.PESSIMISTIC_WRITE) == null) {
            throw new NotFoundException("Không tìm thấy sinh viên");
        }
    }

    private Appointment requireMine(Long studentId, Long appointmentId) {
        if (appointmentId == null) throw new NotFoundException("Không tìm thấy lịch hẹn");
        return appointments.findById(appointmentId)
                .filter(item -> Objects.equals(item.getStudentId(), studentId))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy lịch hẹn"));
    }

    private AppointmentSlot requireSlot(Long slotId) {
        return slots.findById(slotId).orElseThrow(() -> new NotFoundException("Không tìm thấy khung giờ"));
    }

    private AppointmentView toView(Appointment appointment) {
        return toView(appointment, requireSlot(appointment.getSlotId()));
    }

    private AppointmentView toView(Appointment appointment, AppointmentSlot slot) {
        UserSummary manager = users.getSummary(appointment.getManagerId());
        String ticketCode = appointment.getTicketId() == null ? null
                : tickets.findById(appointment.getTicketId()).map(Ticket::getCode).orElse(null);
        boolean changeable = appointment.getStatus() == AppointmentStatus.CONFIRMED
                && !slot.getStartAt().isBefore(LocalDateTime.now().plusHours(
                        settings.getInt(SettingKeys.APPOINTMENT_CANCEL_BEFORE_HOURS)));
        return new AppointmentView(appointment.getId(), slot.getId(), appointment.getManagerId(),
                manager == null ? "" : manager.fullName(), appointment.getCategoryId(),
                appointment.getCategoryId() == null ? "" : categories.getDisplayPath(appointment.getCategoryId()),
                appointment.getTicketId(), ticketCode, appointment.getTopic(), appointment.getNote(),
                appointment.getModality(), slot.getLocation(), slot.getMeetingLink(),
                slot.getStartAt(), slot.getEndAt(), appointment.getStatus(), appointment.getCancelReason(),
                appointment.getStudentVisibleSummary(), changeable, changeable);
    }

    private void notifyBoth(Appointment appointment, NotificationType type, String title) {
        notifications.notifyUser(appointment.getStudentId(), type, title, appointment.getTopic(),
                "/student/appointments/" + appointment.getId(), "APPOINTMENT", appointment.getId());
        notifications.notifyUser(appointment.getManagerId(), type, title, appointment.getTopic(),
                "/manager/appointments", "APPOINTMENT", appointment.getId());
    }

    private static void validateText(String topic, String note) {
        if (topic == null || topic.trim().length() < 10 || topic.trim().length() > 200) {
            throw new BadRequestException("Chủ đề phải từ 10 đến 200 ký tự");
        }
        if (note != null && !note.isBlank() && (note.trim().length() < 10 || note.trim().length() > 500)) {
            throw new BadRequestException("Ghi chú phải từ 10 đến 500 ký tự");
        }
    }

    private static void validateReason(String reason) {
        if (reason == null || reason.trim().length() < 10 || reason.trim().length() > 500) {
            throw new BadRequestException("Lý do phải từ 10 đến 500 ký tự");
        }
    }

    private static String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static AppException slotTaken() {
        return new AppException(ErrorCode.ERR_SLOT_TAKEN);
    }

    private static boolean isActiveSlotConflict(Throwable exception) {
        for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
            if (cause.getMessage() != null && cause.getMessage().contains("uk_appt_active_slot")) return true;
        }
        return false;
    }

    private static String escapeIcs(String text) {
        return text.replace("\\", "\\\\").replace("\r\n", "\n").replace("\r", "\n")
                .replace(";", "\\;").replace(",", "\\,").replace("\n", "\\n");
    }

    private static void appendIcsLine(StringBuilder target, String line) {
        int octets = 0;
        for (int offset = 0; offset < line.length();) {
            int codePoint = line.codePointAt(offset);
            String value = new String(Character.toChars(codePoint));
            int size = value.getBytes(StandardCharsets.UTF_8).length;
            if (octets + size > 75) {
                target.append("\r\n ");
                octets = 1;
            }
            target.append(value);
            octets += size;
            offset += Character.charCount(codePoint);
        }
        target.append("\r\n");
    }
}
