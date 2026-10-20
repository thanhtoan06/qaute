package vn.edu.hcmute.qaute.service.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.constant.Modality;
import vn.edu.hcmute.qaute.common.constant.SlotStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.dto.request.manager.AvailabilityBlockForm;
import vn.edu.hcmute.qaute.dto.request.manager.AvailabilityRuleForm;
import vn.edu.hcmute.qaute.entity.appointment.AppointmentSlot;
import vn.edu.hcmute.qaute.entity.appointment.Appointment;
import vn.edu.hcmute.qaute.entity.appointment.AvailabilityRule;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentRepository;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentSlotRepository;
import vn.edu.hcmute.qaute.repository.appointment.AvailabilityBlockRepository;
import vn.edu.hcmute.qaute.repository.appointment.AvailabilityRuleRepository;
import vn.edu.hcmute.qaute.service.appointment.SlotGenerationService;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;

@ExtendWith(MockitoExtension.class)
class ManagerScheduleServiceTest {
    @Mock AvailabilityRuleRepository rules;
    @Mock AvailabilityBlockRepository blocks;
    @Mock AppointmentSlotRepository slots;
    @Mock AppointmentRepository appointments;
    @Mock SlotGenerationService generation;
    @Mock UserLookupService users;
    @InjectMocks ManagerScheduleService service;

    @Test
    void rejectsOverlappingRuleDuringIntersectingValidity() {
        LocalDate day = LocalDate.now().plusDays(1);
        AvailabilityRule existing = new AvailabilityRule();
        existing.setActive(true);
        existing.setDayOfWeek(day.getDayOfWeek().getValue());
        existing.setStartTime(LocalTime.of(9, 0));
        existing.setEndTime(LocalTime.of(10, 0));
        existing.setValidFrom(day);
        when(rules.findByManagerIdOrderByDayOfWeekAscStartTimeAsc(7L)).thenReturn(List.of(existing));

        AvailabilityRuleForm form = new AvailabilityRuleForm();
        form.setDayOfWeek(day.getDayOfWeek().getValue());
        form.setStartTime(LocalTime.of(9, 30));
        form.setEndTime(LocalTime.of(10, 30));
        form.setSlotMinutes(30);
        form.setModality(Modality.ONLINE);
        form.setMeetingLink("https://example.org/meet");
        form.setValidFrom(day);

        assertThrows(BadRequestException.class, () -> service.saveRule(form, 7L));
        verify(rules, never()).saveAndFlush(any());
        verify(generation, never()).generateForManager(any());
    }

    @Test
    void blockRejectsBookedSlotWithoutSavingBlock() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        AvailabilityBlockForm form = new AvailabilityBlockForm();
        form.setStartAt(start);
        form.setEndAt(start.plusHours(1));
        AppointmentSlot slot = new AppointmentSlot();
        slot.setStatus(SlotStatus.BOOKED);
        when(slots.findOverlapping(eq(7L), eq(start), eq(start.plusHours(1))))
                .thenReturn(List.of(slot));

        assertEquals("Hãy hủy các lịch hẹn đã đặt trước",
                assertThrows(BadRequestException.class, () -> service.addBlock(form, 7L)).getMessage());
        verify(blocks, never()).save(any());
    }

    @Test
    void disablingRuleKeepsAvailableSlotWithAppointmentHistoryBlocked() {
        LocalDate day = LocalDate.now().plusDays(1);
        AvailabilityRule rule = new AvailabilityRule();
        rule.setId(3L);
        rule.setManagerId(7L);
        rule.setActive(true);
        rule.setValidFrom(day);
        rule.setDayOfWeek(day.getDayOfWeek().getValue());
        rule.setStartTime(LocalTime.of(9, 0));
        rule.setEndTime(LocalTime.of(10, 0));
        AppointmentSlot slot = new AppointmentSlot();
        slot.setId(9L);
        slot.setAvailabilityRuleId(3L);
        slot.setManagerId(7L);
        slot.setStartAt(day.atTime(9, 0));
        slot.setEndAt(day.atTime(9, 30));
        slot.setStatus(SlotStatus.AVAILABLE);
        Appointment cancelled = new Appointment();
        cancelled.setSlotId(9L);
        when(rules.findById(3L)).thenReturn(Optional.of(rule));
        when(slots.findByManagerIdAndStartAtBetweenOrderByStartAtAsc(
                eq(7L), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(slot));
        when(appointments.findBySlotIdIn(List.of(9L))).thenReturn(List.of(cancelled));

        assertEquals(0, service.deleteRule(3L, 7L));
        assertEquals(SlotStatus.BLOCKED, slot.getStatus());
        verify(slots).deleteAll(List.of());
    }
}
