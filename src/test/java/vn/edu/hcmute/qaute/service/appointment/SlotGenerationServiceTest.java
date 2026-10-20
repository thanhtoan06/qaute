package vn.edu.hcmute.qaute.service.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmute.qaute.common.constant.Modality;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.constant.SlotStatus;
import vn.edu.hcmute.qaute.entity.appointment.AppointmentSlot;
import vn.edu.hcmute.qaute.entity.appointment.AvailabilityBlock;
import vn.edu.hcmute.qaute.entity.appointment.AvailabilityRule;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentSlotRepository;
import vn.edu.hcmute.qaute.repository.appointment.AvailabilityBlockRepository;
import vn.edu.hcmute.qaute.repository.appointment.AvailabilityRuleRepository;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@ExtendWith(MockitoExtension.class)
class SlotGenerationServiceTest {
    @Mock AvailabilityRuleRepository rules;
    @Mock AvailabilityBlockRepository blocks;
    @Mock AppointmentSlotRepository slots;
    @Mock SettingsService settings;
    @InjectMocks SlotGenerationService service;

    @Test
    void generatesOnlyFutureUnblockedMissingSlots() {
        LocalDate day = LocalDate.now().plusDays(1);
        AvailabilityRule rule = new AvailabilityRule();
        rule.setId(10L);
        rule.setManagerId(7L);
        rule.setDayOfWeek(day.getDayOfWeek().getValue());
        rule.setStartTime(LocalTime.of(9, 0));
        rule.setEndTime(LocalTime.of(10, 30));
        rule.setSlotMinutes(30);
        rule.setValidFrom(day);
        rule.setValidTo(day);
        rule.setModality(Modality.ONLINE);
        rule.setActive(true);
        AvailabilityBlock block = new AvailabilityBlock();
        block.setStartAt(day.atTime(9, 45));
        block.setEndAt(day.atTime(10, 0));
        AppointmentSlot existing = new AppointmentSlot();
        existing.setStartAt(day.atTime(9, 0));

        when(settings.getInt(SettingKeys.APPOINTMENT_GENERATE_DAYS)).thenReturn(7);
        when(settings.getInt(SettingKeys.APPOINTMENT_SLOT_MINUTES)).thenReturn(30);
        when(rules.findByManagerIdOrderByDayOfWeekAscStartTimeAsc(7L)).thenReturn(List.of(rule));
        when(blocks.findByManagerIdAndEndAtAfterOrderByStartAtAsc(eq(7L), org.mockito.ArgumentMatchers.any(LocalDateTime.class)))
                .thenReturn(List.of(block));
        when(slots.findByManagerIdAndStartAtBetweenOrderByStartAtAsc(
                eq(7L), org.mockito.ArgumentMatchers.any(LocalDateTime.class),
                org.mockito.ArgumentMatchers.any(LocalDateTime.class))).thenReturn(List.of(existing));

        service.generateForManager(7L);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<AppointmentSlot>> captor = ArgumentCaptor.forClass(List.class);
        verify(slots).saveAll(captor.capture());
        List<AppointmentSlot> result = captor.getValue();
        assertEquals(1, result.size());
        assertEquals(day.atTime(10, 0), result.get(0).getStartAt());
        assertEquals(SlotStatus.AVAILABLE, result.get(0).getStatus());
        assertEquals(Modality.ONLINE, result.get(0).getModality());
        assertEquals(10L, result.get(0).getAvailabilityRuleId());
    }

    @Test
    void expiresPastSlots() {
        when(slots.expirePast(org.mockito.ArgumentMatchers.any(LocalDateTime.class))).thenReturn(3);
        assertEquals(3, service.expirePast());
    }
}
