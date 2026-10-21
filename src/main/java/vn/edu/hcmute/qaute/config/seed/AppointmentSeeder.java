package vn.edu.hcmute.qaute.config.seed;

import jakarta.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.common.constant.Modality;
import vn.edu.hcmute.qaute.common.constant.SlotStatus;
import vn.edu.hcmute.qaute.entity.appointment.Appointment;
import vn.edu.hcmute.qaute.entity.appointment.AppointmentSlot;
import vn.edu.hcmute.qaute.entity.appointment.AvailabilityRule;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.organization.Category;
import vn.edu.hcmute.qaute.entity.organization.Department;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentRepository;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentSlotRepository;
import vn.edu.hcmute.qaute.repository.appointment.AvailabilityRuleRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.service.appointment.SlotGenerationService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;

@Component
@Profile("demo")
@RequiredArgsConstructor
public class AppointmentSeeder implements DemoSeeder {
    private static final String[] CATEGORY_CODES = {"DT_DKHP", "CT_HOCBONG", "TC_HOCPHI", "KTX_DANGKY"};
    private static final String[] TOPICS = {
        "Tư vấn kế hoạch đăng ký học phần học kỳ mới",
        "Tư vấn hồ sơ học bổng khuyến khích học tập",
        "Tư vấn thời hạn và phương thức đóng học phí",
        "Tư vấn đăng ký chỗ ở ký túc xá",
        "Rà soát điều kiện đăng ký học phần chuyên ngành",
        "Hướng dẫn bổ sung hồ sơ xét học bổng",
        "Trao đổi về chứng từ thanh toán học phí",
        "Trao đổi về nội quy và hồ sơ nội trú",
        "Điều chỉnh kế hoạch học tập cá nhân",
        "Tìm hiểu chính sách hỗ trợ sinh viên"
    };

    private final UserRepository users;
    private final CategoryRepository categories;
    private final DepartmentRepository departments;
    private final AvailabilityRuleRepository rules;
    private final AppointmentSlotRepository slots;
    private final AppointmentRepository appointments;
    private final SlotGenerationService generation;
    private final ManagerScopeService scopes;
    private final EntityManager entityManager;

    @Override
    public int order() {
        return 50;
    }

    @Override
    @Transactional
    public void seed() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        List<ManagerData> managers = new ArrayList<>();
        for (int number = 1; number <= 4; number++) {
            User manager = user("manager" + number + "@qaute.edu.vn");
            Category category = categories.findByCode(CATEGORY_CODES[number - 1])
                    .orElseThrow(() -> new IllegalStateException("Chưa có chuyên mục lịch hẹn demo"));
            if (!scopes.isInScope(manager.getId(), category.getId())) {
                throw new IllegalStateException("Manager demo chưa được gán chuyên mục lịch hẹn");
            }
            Department department = departments.findById(category.getDepartmentId())
                    .orElseThrow(() -> new IllegalStateException("Chưa có phòng ban lịch hẹn demo"));
            String location = department.getName() + " – " + department.getLocation();
            ManagerData data = new ManagerData(manager, category, location, number);
            managers.add(data);
            seedRules(data, now.toLocalDate());
            generation.generateForManager(manager.getId());
        }

        for (int index = 0; index < TOPICS.length; index++) {
            User student = user((24162101 + index) + "@student.hcmute.edu.vn");
            if (appointments.existsByStudentIdAndTopic(student.getId(), TOPICS[index])) continue;
            ManagerData manager = managers.get(index % managers.size());
            AppointmentStatus status = index < 4 ? AppointmentStatus.CONFIRMED
                    : index < 6 ? AppointmentStatus.COMPLETED
                    : index < 8 ? AppointmentStatus.NO_SHOW : AppointmentStatus.CANCELLED;
            boolean past = status == AppointmentStatus.COMPLETED || status == AppointmentStatus.NO_SHOW;
            AppointmentSlot slot = past ? historicalSlot(manager, now.toLocalDate().minusDays(index + 2))
                    : futureSlot(manager, now);
            if (status != AppointmentStatus.CANCELLED) {
                slot.setStatus(SlotStatus.BOOKED);
                slots.save(slot);
            }
            Appointment appointment = new Appointment();
            appointment.setSlotId(slot.getId());
            appointment.setStudentId(student.getId());
            appointment.setManagerId(manager.user().getId());
            appointment.setCategoryId(manager.category().getId());
            appointment.setTopic(TOPICS[index]);
            appointment.setNote("Em muốn được tư vấn cụ thể về hồ sơ và các bước thực hiện.");
            appointment.setModality(index % 2 == 0 ? Modality.ONLINE : Modality.IN_PERSON);
            appointment.setStatus(status);
            appointment.setReminder24hSent(past);
            appointment.setReminder1hSent(past);
            if (status == AppointmentStatus.COMPLETED) {
                appointment.setOutcomeSummary("Đã kiểm tra hồ sơ và hướng dẫn sinh viên hoàn thiện các bước theo quy định của phòng ban.");
                appointment.setStudentVisibleSummary("Em vui lòng bổ sung hồ sơ theo hướng dẫn và theo dõi kết quả trên cổng sinh viên.");
            } else if (status == AppointmentStatus.CANCELLED) {
                appointment.setCancelledBy(student.getId());
                appointment.setCancelReason("Em có lịch học trùng nên xin hủy và sẽ đặt lại lịch phù hợp.");
            }
            appointments.saveAndFlush(appointment);
            LocalDateTime created = past ? slot.getStartAt().minusDays(2) : now.minusDays(2);
            LocalDateTime updated = past ? slot.getEndAt() : now.minusHours(1);
            // CreationTimestamp tự đặt thời gian hiện tại khi insert; cập nhật lại để báo cáo có lịch sử.
            entityManager.createQuery("update Appointment a set a.createdAt = :created, a.updatedAt = :updated where a.id = :id")
                    .setParameter("created", created).setParameter("updated", updated)
                    .setParameter("id", appointment.getId()).executeUpdate();
        }
    }

    private void seedRules(ManagerData manager, LocalDate today) {
        List<AvailabilityRule> existing = rules.findByManagerIdOrderByDayOfWeekAscStartTimeAsc(manager.user().getId());
        List<AvailabilityRule> missing = new ArrayList<>();
        for (int day = 1; day <= 5; day++) {
            for (int hour : new int[]{9, 14}) {
                int weekday = day;
                LocalTime start = LocalTime.of(hour, 0);
                LocalTime end = start.plusHours(2);
                if (existing.stream().anyMatch(rule -> rule.getDayOfWeek() == weekday
                        && start.equals(rule.getStartTime()) && end.equals(rule.getEndTime()))) continue;
                AvailabilityRule rule = new AvailabilityRule();
                rule.setManagerId(manager.user().getId());
                rule.setDayOfWeek(day);
                rule.setStartTime(start);
                rule.setEndTime(end);
                rule.setSlotMinutes(30);
                rule.setModality(Modality.BOTH);
                rule.setLocation(manager.location());
                rule.setMeetingLink(meetingLink(manager));
                rule.setValidFrom(today);
                rule.setActive(true);
                missing.add(rule);
            }
        }
        rules.saveAll(missing);
    }

    private AppointmentSlot futureSlot(ManagerData manager, LocalDateTime now) {
        return slots.findByManagerIdAndStartAtBetweenOrderByStartAtAsc(
                        manager.user().getId(), now.plusDays(2), now.plusDays(91)).stream()
                .filter(slot -> slot.getStatus() == SlotStatus.AVAILABLE)
                .filter(slot -> slot.getModality() == Modality.BOTH
                        && Duration.between(slot.getStartAt(), slot.getEndAt()).toMinutes() == 30)
                .filter(slot -> appointments.findBySlotIdIn(List.of(slot.getId())).isEmpty())
                .findFirst().orElseThrow(() -> new IllegalStateException("Không có khung giờ tương lai để tạo lịch hẹn demo"));
    }

    private AppointmentSlot historicalSlot(ManagerData manager, LocalDate day) {
        for (int attempt = 0; attempt < 30; attempt++) {
            LocalDate candidate = day.minusDays(attempt);
            if (candidate.getDayOfWeek() == DayOfWeek.SATURDAY || candidate.getDayOfWeek() == DayOfWeek.SUNDAY) continue;
            LocalDateTime start = candidate.atTime(9, 0);
            AppointmentSlot slot = slots.findByManagerIdAndStartAt(manager.user().getId(), start).orElse(null);
            if (slot != null) continue;
            slot = new AppointmentSlot();
            slot.setManagerId(manager.user().getId());
            slot.setStartAt(start);
            slot.setEndAt(start.plusMinutes(30));
            slot.setModality(Modality.BOTH);
            slot.setLocation(manager.location());
            slot.setMeetingLink(meetingLink(manager));
            slot.setStatus(SlotStatus.BOOKED);
            return slots.save(slot);
        }
        throw new IllegalStateException("Không có khung giờ quá khứ trống để tạo lịch hẹn demo");
    }

    private User user(String email) {
        return users.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException("Thiếu tài khoản demo " + email));
    }

    private String meetingLink(ManagerData manager) {
        return "https://meet.example.com/qaute-" + manager.number();
    }

    private record ManagerData(User user, Category category, String location, int number) {
    }
}
