package vn.edu.hcmute.qaute.service.manager;

import vn.edu.hcmute.qaute.service.ticket.TicketAccessService;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.NotificationType;
import vn.edu.hcmute.qaute.common.exception.InvalidTransitionException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.entity.appointment.Appointment;
import vn.edu.hcmute.qaute.entity.appointment.AppointmentSlot;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentRepository;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentSlotRepository;
import vn.edu.hcmute.qaute.repository.ticket.TicketRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.notification.NotificationService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@ExtendWith(MockitoExtension.class)
class ManagerAppointmentServiceTest {
    @Mock AppointmentRepository appointments;
    @Mock AppointmentSlotRepository slots;
    @Mock TicketRepository tickets;
    @Mock TicketAccessService ticketAccess;
    @Mock UserLookupService users;
    @Mock CategoryQueryService categories;
    @Mock NotificationService notifications;
    @Mock AuditService audit;
    @InjectMocks ManagerAppointmentService service;

    @Test
    void anotherManagerCannotViewOrCancelAppointment() {
        Appointment appointment = appointment();
        when(appointments.findById(1L)).thenReturn(Optional.of(appointment));
        assertThrows(NotFoundException.class, () -> service.detail(1L, 8L));
        assertThrows(NotFoundException.class, () -> service.cancel(1L, 8L, "Lý do hủy hợp lệ"));
        verify(appointments, never()).saveAndFlush(any());
    }

    @Test
    void cancelFutureAppointmentReleasesSlotAndNotifiesStudent() {
        Appointment appointment = appointment();
        AppointmentSlot slot = slot(LocalDateTime.now().plusHours(2));
        when(appointments.findById(1L)).thenReturn(Optional.of(appointment));
        when(slots.findById(2L)).thenReturn(Optional.of(slot));
        service.cancel(1L, 7L, "Tư vấn viên bận đột xuất");
        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
        assertEquals(7L, appointment.getCancelledBy());
        verify(slots).releaseBooked(org.mockito.ArgumentMatchers.eq(2L), any());
        verify(notifications).notifyUser(org.mockito.ArgumentMatchers.eq(3L),
                org.mockito.ArgumentMatchers.eq(NotificationType.APPOINTMENT_CANCELLED), any(), any(),
                any(), any(), org.mockito.ArgumentMatchers.eq(1L));
        verify(audit).log(org.mockito.ArgumentMatchers.eq(AuditActions.APPOINTMENT_CANCELLED_BY_MANAGER),
                any(), org.mockito.ArgumentMatchers.eq(1L), any(), any());
    }

    @Test
    void cannotFinishBeforeAppointmentStarts() {
        Appointment appointment = appointment();
        when(appointments.findById(1L)).thenReturn(Optional.of(appointment));
        when(slots.findById(2L)).thenReturn(Optional.of(slot(LocalDateTime.now().plusHours(1))));
        assertThrows(InvalidTransitionException.class, () -> service.markNoShow(1L, 7L));
        verify(appointments, never()).saveAndFlush(any());
    }

    @Test
    void ownedAppointmentDoesNotExposeLinkedTicketOutsideScope() {
        Appointment appointment = appointment(); appointment.setTicketId(99L);
        when(appointments.findById(1L)).thenReturn(Optional.of(appointment));
        when(slots.findById(2L)).thenReturn(Optional.of(slot(LocalDateTime.now().plusHours(2))));
        var view = service.detail(1L, 7L);
        assertNull(view.ticketId());
        assertNull(view.ticketCode());
        verify(tickets, never()).findById(99L);
    }

    private Appointment appointment() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setManagerId(7L);
        appointment.setStudentId(3L);
        appointment.setSlotId(2L);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setTopic("Tư vấn học tập");
        return appointment;
    }

    private AppointmentSlot slot(LocalDateTime start) {
        AppointmentSlot slot = new AppointmentSlot();
        slot.setId(2L);
        slot.setStartAt(start);
        return slot;
    }
}
