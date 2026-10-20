package vn.edu.hcmute.qaute.service.manager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.common.constant.Modality;
import vn.edu.hcmute.qaute.common.constant.SlotStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.manager.AvailabilityBlockForm;
import vn.edu.hcmute.qaute.dto.request.manager.AvailabilityRuleForm;
import vn.edu.hcmute.qaute.dto.view.ManagerScheduleSlotView;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.appointment.Appointment;
import vn.edu.hcmute.qaute.entity.appointment.AppointmentSlot;
import vn.edu.hcmute.qaute.entity.appointment.AvailabilityBlock;
import vn.edu.hcmute.qaute.entity.appointment.AvailabilityRule;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentRepository;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentSlotRepository;
import vn.edu.hcmute.qaute.repository.appointment.AvailabilityBlockRepository;
import vn.edu.hcmute.qaute.repository.appointment.AvailabilityRuleRepository;
import vn.edu.hcmute.qaute.service.appointment.SlotGenerationService;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;

@Service
@Transactional
public class ManagerScheduleService {
    private final AvailabilityRuleRepository rules;
    private final AvailabilityBlockRepository blocks;
    private final AppointmentSlotRepository slots;
    private final AppointmentRepository appointments;
    private final SlotGenerationService generation;
    private final UserLookupService users;

    public ManagerScheduleService(AvailabilityRuleRepository rules, AvailabilityBlockRepository blocks,
            AppointmentSlotRepository slots, AppointmentRepository appointments,
            SlotGenerationService generation, UserLookupService users) {
        this.rules = rules;
        this.blocks = blocks;
        this.slots = slots;
        this.appointments = appointments;
        this.generation = generation;
        this.users = users;
    }

    @Transactional(readOnly = true)
    public List<AvailabilityRule> rules(Long managerId) {
        return rules.findByManagerIdOrderByDayOfWeekAscStartTimeAsc(managerId).stream()
                .filter(AvailabilityRule::isActive).toList();
    }

    @Transactional(readOnly = true)
    public List<AvailabilityBlock> blocks(Long managerId) {
        return blocks.findByManagerIdAndEndAtAfterOrderByStartAtAsc(managerId, LocalDateTime.now());
    }

    public AvailabilityRule saveRule(AvailabilityRuleForm form, Long managerId) {
        validateRule(form);
        for (AvailabilityRule other : rules(managerId)) {
            if (other.getDayOfWeek() == form.getDayOfWeek()
                    && other.getStartTime().isBefore(form.getEndTime())
                    && other.getEndTime().isAfter(form.getStartTime())
                    && dateRangesOverlap(other.getValidFrom(), other.getValidTo(),
                            form.getValidFrom(), form.getValidTo(), form.getDayOfWeek())) {
                throw new BadRequestException("Khung giờ trùng quy tắc đã có cùng ngày và thời gian hiệu lực");
            }
        }
        AvailabilityRule rule = new AvailabilityRule();
        rule.setManagerId(managerId);
        rule.setDayOfWeek(form.getDayOfWeek());
        rule.setStartTime(form.getStartTime());
        rule.setEndTime(form.getEndTime());
        rule.setSlotMinutes(form.getSlotMinutes());
        rule.setModality(form.getModality());
        rule.setLocation(trimToNull(form.getLocation()));
        rule.setMeetingLink(trimToNull(form.getMeetingLink()));
        rule.setValidFrom(form.getValidFrom());
        rule.setValidTo(form.getValidTo());
        rule.setActive(true);
        rules.saveAndFlush(rule);
        generation.generateForManager(managerId);
        return rule;
    }

    public int deleteRule(Long id, Long managerId) {
        AvailabilityRule rule = rules.findById(id)
                .filter(item -> Objects.equals(item.getManagerId(), managerId))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy quy tắc lịch làm việc"));
        if (!rule.isActive()) return 0;
        rule.setActive(false);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = rule.getValidFrom().atStartOfDay();
        if (from.isBefore(now)) from = now;
        LocalDateTime to = rule.getValidTo() == null ? now.plusYears(10)
                : rule.getValidTo().plusDays(1).atStartOfDay();
        if (!from.isBefore(to)) return 0;
        List<AppointmentSlot> matched = slots.findByManagerIdAndStartAtBetweenOrderByStartAtAsc(managerId, from, to)
                .stream().filter(slot -> Objects.equals(slot.getAvailabilityRuleId(), id)
                        || slot.getAvailabilityRuleId() == null && matchesRule(rule, slot)).toList();
        int booked = (int) matched.stream().filter(slot -> slot.getStatus() == SlotStatus.BOOKED).count();
        List<AppointmentSlot> available = matched.stream()
                .filter(slot -> slot.getStatus() == SlotStatus.AVAILABLE).toList();
        Set<Long> referenced = available.isEmpty() ? Set.of()
                : appointments.findBySlotIdIn(available.stream().map(AppointmentSlot::getId).toList())
                        .stream().map(Appointment::getSlotId).collect(Collectors.toSet());
        available.stream().filter(slot -> referenced.contains(slot.getId()))
                .forEach(slot -> slot.setStatus(SlotStatus.BLOCKED));
        slots.deleteAll(available.stream().filter(slot -> !referenced.contains(slot.getId())).toList());
        return booked;
    }

    public AvailabilityBlock addBlock(AvailabilityBlockForm form, Long managerId) {
        if (form == null || form.getStartAt() == null || form.getEndAt() == null
                || !form.getStartAt().isBefore(form.getEndAt())) {
            throw new BadRequestException("Khoảng chặn phải có giờ bắt đầu trước giờ kết thúc");
        }
        if (form.getReason() != null && form.getReason().length() > 200) {
            throw new BadRequestException("Lý do tối đa 200 ký tự");
        }
        List<AppointmentSlot> affected = slots.findOverlapping(managerId, form.getStartAt(), form.getEndAt());
        if (affected.stream().anyMatch(slot -> slot.getStatus() == SlotStatus.BOOKED)) {
            throw new BadRequestException("Hãy hủy các lịch hẹn đã đặt trước");
        }
        AvailabilityBlock block = new AvailabilityBlock();
        block.setManagerId(managerId);
        block.setStartAt(form.getStartAt());
        block.setEndAt(form.getEndAt());
        block.setReason(trimToNull(form.getReason()));
        blocks.save(block);
        affected.stream().filter(slot -> slot.getStatus() == SlotStatus.AVAILABLE)
                .forEach(slot -> slot.setStatus(SlotStatus.BLOCKED));
        return block;
    }

    public void deleteBlock(Long id, Long managerId) {
        AvailabilityBlock block = blocks.findById(id)
                .filter(item -> Objects.equals(item.getManagerId(), managerId))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khung chặn"));
        blocks.delete(block);
        blocks.flush();
        LocalDateTime now = LocalDateTime.now();
        List<AvailabilityBlock> remaining = blocks.findByManagerIdAndStartAtLessThanAndEndAtGreaterThan(
                managerId, block.getEndAt(), block.getStartAt());
        List<AvailabilityRule> activeRules = rules(managerId);
        slots.findOverlapping(managerId, block.getStartAt(), block.getEndAt()).stream()
                .filter(slot -> slot.getStatus() == SlotStatus.BLOCKED && slot.getStartAt().isAfter(now))
                .filter(slot -> remaining.stream().noneMatch(other -> overlaps(other, slot)))
                .filter(slot -> activeRules.stream().anyMatch(rule -> Objects.equals(slot.getAvailabilityRuleId(), rule.getId())
                        || slot.getAvailabilityRuleId() == null && matchesRule(rule, slot)))
                .forEach(slot -> slot.setStatus(SlotStatus.AVAILABLE));
    }

    @Transactional(readOnly = true)
    public List<ManagerScheduleSlotView> weekView(Long managerId, LocalDate weekStart) {
        LocalDate monday = weekStart.minusDays(weekStart.getDayOfWeek().getValue() - 1L);
        List<AppointmentSlot> weekSlots = slots.findByManagerIdAndStartAtBetweenOrderByStartAtAsc(
                managerId, monday.atStartOfDay(), monday.plusDays(7).atStartOfDay());
        if (weekSlots.isEmpty()) return List.of();
        List<Long> ids = weekSlots.stream().map(AppointmentSlot::getId).toList();
        Map<Long, Appointment> bookings = appointments.findBySlotIdInAndStatusIn(ids,
                        List.of(AppointmentStatus.CONFIRMED, AppointmentStatus.COMPLETED, AppointmentStatus.NO_SHOW))
                .stream().collect(Collectors.toMap(Appointment::getSlotId, item -> item, (a, b) -> a));
        Set<Long> studentIds = bookings.values().stream().map(Appointment::getStudentId).collect(Collectors.toSet());
        Map<Long, UserSummary> summaries = studentIds.isEmpty() ? Map.of() : users.getSummaries(studentIds);
        LocalDateTime now = LocalDateTime.now();
        List<ManagerScheduleSlotView> result = new ArrayList<>();
        for (AppointmentSlot slot : weekSlots) {
            Appointment booking = bookings.get(slot.getId());
            UserSummary student = booking == null ? null : summaries.get(booking.getStudentId());
            result.add(new ManagerScheduleSlotView(slot.getId(), slot.getStartAt(), slot.getEndAt(),
                    slot.getModality(), slot.getStatus(), booking == null ? null : booking.getId(),
                    student == null ? null : student.fullName(), slot.getStartAt().isBefore(now)));
        }
        return result;
    }

    private void validateRule(AvailabilityRuleForm form) {
        if (form == null || form.getDayOfWeek() == null || form.getDayOfWeek() < 1 || form.getDayOfWeek() > 7
                || form.getStartTime() == null || form.getEndTime() == null
                || !form.getStartTime().isBefore(form.getEndTime())
                || form.getSlotMinutes() == null || !Set.of(15, 30, 45, 60).contains(form.getSlotMinutes())
                || form.getModality() == null || form.getValidFrom() == null
                || form.getValidFrom().isBefore(LocalDate.now())
                || form.getValidTo() != null && form.getValidTo().isBefore(form.getValidFrom())) {
            throw new BadRequestException("Quy tắc lịch làm việc không hợp lệ");
        }
        if (form.getLocation() != null && form.getLocation().length() > 200
                || form.getMeetingLink() != null && form.getMeetingLink().length() > 300) {
            throw new BadRequestException("Địa điểm hoặc liên kết quá dài");
        }
        Modality modality = form.getModality();
        if ((modality == Modality.IN_PERSON || modality == Modality.BOTH)
                && trimToNull(form.getLocation()) == null) {
            throw new BadRequestException("Cần nhập địa điểm cho lịch gặp trực tiếp");
        }
        if ((modality == Modality.ONLINE || modality == Modality.BOTH)
                && (trimToNull(form.getMeetingLink()) == null
                    || !form.getMeetingLink().matches("^https://[^\\s]+$"))) {
            throw new BadRequestException("Cần liên kết họp HTTPS hợp lệ");
        }
    }

    private static boolean dateRangesOverlap(LocalDate firstFrom, LocalDate firstTo,
            LocalDate secondFrom, LocalDate secondTo, int dayOfWeek) {
        LocalDate start = firstFrom.isAfter(secondFrom) ? firstFrom : secondFrom;
        LocalDate end = firstTo == null ? secondTo
                : secondTo == null || firstTo.isBefore(secondTo) ? firstTo : secondTo;
        LocalDate occurrence = start.plusDays((dayOfWeek - start.getDayOfWeek().getValue() + 7) % 7);
        return end == null || !occurrence.isAfter(end);
    }

    private static boolean overlaps(AvailabilityBlock block, AppointmentSlot slot) {
        return block.getStartAt().isBefore(slot.getEndAt()) && block.getEndAt().isAfter(slot.getStartAt());
    }

    private static boolean matchesRule(AvailabilityRule rule, AppointmentSlot slot) {
        LocalDate day = slot.getStartAt().toLocalDate();
        return day.getDayOfWeek().getValue() == rule.getDayOfWeek()
                && !day.isBefore(rule.getValidFrom())
                && (rule.getValidTo() == null || !day.isAfter(rule.getValidTo()))
                && !slot.getStartAt().toLocalTime().isBefore(rule.getStartTime())
                && !slot.getEndAt().toLocalTime().isAfter(rule.getEndTime())
                && slot.getModality() == rule.getModality();
    }

    private static String trimToNull(String text) {
        return text == null || text.isBlank() ? null : text.trim();
    }
}
