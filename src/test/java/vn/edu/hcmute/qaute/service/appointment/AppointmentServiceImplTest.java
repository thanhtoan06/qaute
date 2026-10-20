package vn.edu.hcmute.qaute.service.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.common.constant.Modality;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.constant.SlotStatus;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.dto.view.BookAppointmentCommand;
import vn.edu.hcmute.qaute.entity.appointment.Appointment;
import vn.edu.hcmute.qaute.entity.appointment.AppointmentSlot;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentRepository;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentSlotRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.notification.NotificationService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {
    @Mock AppointmentRepository appointments;
    @Mock AppointmentSlotRepository slots;
    @Mock TicketRepository tickets;
    @Mock ManagerScopeService scopes;
    @Mock CategoryQueryService categories;
    @Mock UserLookupService users;
    @Mock SettingsService settings;
    @Mock NotificationService notifications;
    @Mock EntityManager entityManager;
    @InjectMocks AppointmentServiceImpl service;

    @Test
    void simultaneousClaimFailureReturnsSlotTakenWithoutCreatingAppointment() {
        AppointmentSlot slot = new AppointmentSlot();
        slot.setId(9L);
        slot.setManagerId(7L);
        slot.setStatus(SlotStatus.AVAILABLE);
        slot.setModality(Modality.ONLINE);
        slot.setStartAt(LocalDateTime.now().plusDays(2));
        slot.setEndAt(slot.getStartAt().plusMinutes(30));
        when(entityManager.find(User.class, 4L, LockModeType.PESSIMISTIC_WRITE)).thenReturn(new User());
        when(slots.findById(9L)).thenReturn(Optional.of(slot));
        when(scopes.findManagerIdsInScope(2L)).thenReturn(List.of(7L));
        when(settings.getInt(SettingKeys.APPOINTMENT_LEAD_HOURS)).thenReturn(0);
        when(settings.getInt(SettingKeys.APPOINTMENT_MAX_UPCOMING)).thenReturn(3);
        when(slots.markBooked(9L)).thenReturn(0);

        AppException error = assertThrows(AppException.class, () -> service.book(4L,
                new BookAppointmentCommand(9L, 2L, "Chủ đề cần tư vấn", null, Modality.ONLINE, null)));

        assertEquals(ErrorCode.ERR_SLOT_TAKEN, error.getCode());
        verify(appointments, never()).saveAndFlush(any());
    }

    @Test
    void icsEscapesTextAndUsesRequiredTimezoneAndCrlf() {
        Appointment appointment = new Appointment();
        appointment.setId(5L);
        appointment.setStudentId(4L);
        appointment.setSlotId(9L);
        appointment.setTopic("A,B;C\\D");
        appointment.setNote("Dòng 1\nDòng 2");
        AppointmentSlot slot = new AppointmentSlot();
        slot.setStartAt(LocalDateTime.of(2026, 11, 1, 9, 0));
        slot.setEndAt(LocalDateTime.of(2026, 11, 1, 9, 30));
        when(appointments.findById(5L)).thenReturn(Optional.of(appointment));
        when(slots.findById(9L)).thenReturn(Optional.of(slot));

        String ics = service.buildIcs(4L, 5L);

        assertTrue(ics.contains("DTSTART;TZID=Asia/Ho_Chi_Minh:20261101T090000\r\n"));
        assertTrue(ics.contains("SUMMARY:Tư vấn QAUTE: A\\,B\\;C\\\\D\r\n"));
        assertTrue(ics.contains("DESCRIPTION:Dòng 1\\nDòng 2\r\n"));
        assertTrue(ics.endsWith("END:VCALENDAR\r\n"));
    }

    @Test
    void cancellationAfterDeadlineDoesNotReleaseBookedSlot() {
        Appointment appointment = new Appointment();
        appointment.setId(5L);
        appointment.setStudentId(4L);
        appointment.setSlotId(9L);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        AppointmentSlot slot = new AppointmentSlot();
        slot.setId(9L);
        slot.setStartAt(LocalDateTime.now().plusHours(1));
        when(entityManager.find(User.class, 4L, LockModeType.PESSIMISTIC_WRITE)).thenReturn(new User());
        when(appointments.findById(5L)).thenReturn(Optional.of(appointment));
        when(slots.findById(9L)).thenReturn(Optional.of(slot));
        when(settings.getInt(SettingKeys.APPOINTMENT_CANCEL_BEFORE_HOURS)).thenReturn(2);

        assertThrows(BadRequestException.class,
                () -> service.cancelByStudent(4L, 5L, "Tôi không thể tham dự buổi hẹn"));
        verify(slots, never()).releaseBooked(any(), any());
    }
}
