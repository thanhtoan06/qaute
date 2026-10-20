package vn.edu.hcmute.qaute.service.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.constant.SlotStatus;
import vn.edu.hcmute.qaute.entity.appointment.AppointmentSlot;
import vn.edu.hcmute.qaute.entity.appointment.AvailabilityBlock;
import vn.edu.hcmute.qaute.entity.appointment.AvailabilityRule;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentSlotRepository;
import vn.edu.hcmute.qaute.repository.appointment.AvailabilityBlockRepository;
import vn.edu.hcmute.qaute.repository.appointment.AvailabilityRuleRepository;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@Service
public class SlotGenerationService {
    private final AvailabilityRuleRepository rules;
    private final AvailabilityBlockRepository blocks;
    private final AppointmentSlotRepository slots;
    private final SettingsService settings;

    public SlotGenerationService(AvailabilityRuleRepository rules, AvailabilityBlockRepository blocks,
            AppointmentSlotRepository slots, SettingsService settings) {
        this.rules = rules;
        this.blocks = blocks;
        this.slots = slots;
        this.settings = settings;
    }

    @Transactional
    public void generateForManager(Long managerId) {
        generateForManager(managerId, LocalDateTime.now());
    }

    @Transactional
    public void generateAll() {
        LocalDateTime now = LocalDateTime.now();
        for (Long managerId : rules.findActiveManagerIds()) {
            generateForManager(managerId, now);
        }
    }

    @Transactional
    public int expirePast() {
        return slots.expirePast(LocalDateTime.now());
    }

    private void generateForManager(Long managerId, LocalDateTime now) {
        int days = settings.getInt(SettingKeys.APPOINTMENT_GENERATE_DAYS);
        int defaultMinutes = settings.getInt(SettingKeys.APPOINTMENT_SLOT_MINUTES);
        LocalDate today = now.toLocalDate();
        LocalDate lastDay = today.plusDays(days);
        List<AvailabilityBlock> blocked = blocks.findByManagerIdAndEndAtAfterOrderByStartAtAsc(managerId, now);
        Set<LocalDateTime> existing = new HashSet<>();
        slots.findByManagerIdAndStartAtBetweenOrderByStartAtAsc(
                managerId, today.atStartOfDay(), lastDay.plusDays(1).atStartOfDay())
                .forEach(slot -> existing.add(slot.getStartAt()));
        List<AppointmentSlot> generated = new ArrayList<>();

        for (AvailabilityRule rule : rules.findByManagerIdOrderByDayOfWeekAscStartTimeAsc(managerId)) {
            if (!rule.isActive() || rule.getValidFrom() == null || rule.getStartTime() == null
                    || rule.getEndTime() == null || !rule.getStartTime().isBefore(rule.getEndTime())) {
                continue;
            }
            int minutes = rule.getSlotMinutes() > 0 ? rule.getSlotMinutes() : defaultMinutes;
            if (minutes <= 0) {
                throw new IllegalStateException("Thời lượng khung giờ hẹn phải lớn hơn 0");
            }
            LocalDate firstDay = today.isAfter(rule.getValidFrom()) ? today : rule.getValidFrom();
            LocalDate endDay = rule.getValidTo() != null && rule.getValidTo().isBefore(lastDay)
                    ? rule.getValidTo() : lastDay;
            for (LocalDate day = firstDay; !day.isAfter(endDay); day = day.plusDays(1)) {
                if (day.getDayOfWeek().getValue() != rule.getDayOfWeek()) {
                    continue;
                }
                LocalDateTime ruleEnd = day.atTime(rule.getEndTime());
                for (LocalDateTime start = day.atTime(rule.getStartTime());
                        !start.plusMinutes(minutes).isAfter(ruleEnd); start = start.plusMinutes(minutes)) {
                    LocalDateTime end = start.plusMinutes(minutes);
                    if (!start.isAfter(now) || existing.contains(start) || overlaps(blocked, start, end)) {
                        continue;
                    }
                    existing.add(start);
                    AppointmentSlot slot = new AppointmentSlot();
                    slot.setManagerId(managerId);
                    slot.setStartAt(start);
                    slot.setEndAt(end);
                    slot.setModality(rule.getModality());
                    slot.setLocation(rule.getLocation());
                    slot.setMeetingLink(rule.getMeetingLink());
                    slot.setStatus(SlotStatus.AVAILABLE);
                    generated.add(slot);
                }
            }
        }
        slots.saveAll(generated);
    }

    private boolean overlaps(List<AvailabilityBlock> blocks, LocalDateTime start, LocalDateTime end) {
        return blocks.stream().anyMatch(block -> block.getStartAt().isBefore(end)
                && block.getEndAt().isAfter(start));
    }
}
