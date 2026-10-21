package vn.edu.hcmute.qaute.config.seed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.edu.hcmute.qaute.common.constant.AnnouncementAudience;
import vn.edu.hcmute.qaute.common.constant.AnnouncementStatus;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.common.constant.ChatSenderType;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.common.constant.Modality;
import vn.edu.hcmute.qaute.common.constant.SlotStatus;
import vn.edu.hcmute.qaute.entity.appointment.Appointment;
import vn.edu.hcmute.qaute.entity.appointment.AppointmentSlot;
import vn.edu.hcmute.qaute.entity.appointment.AvailabilityRule;
import vn.edu.hcmute.qaute.entity.chat.ChatMessage;
import vn.edu.hcmute.qaute.entity.chat.ChatSession;
import vn.edu.hcmute.qaute.entity.governance.Announcement;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.organization.Category;
import vn.edu.hcmute.qaute.entity.organization.Department;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentRepository;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentSlotRepository;
import vn.edu.hcmute.qaute.repository.appointment.AvailabilityRuleRepository;
import vn.edu.hcmute.qaute.repository.chat.ChatMessageRepository;
import vn.edu.hcmute.qaute.repository.chat.ChatSessionRepository;
import vn.edu.hcmute.qaute.repository.governance.AnnouncementRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.repository.organization.CategoryRepository;
import vn.edu.hcmute.qaute.repository.organization.DepartmentRepository;
import vn.edu.hcmute.qaute.service.appointment.SlotGenerationService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;

class Tv2SeedersTest {
    private final UserRepository users = mock(UserRepository.class);
    private final CategoryRepository categories = mock(CategoryRepository.class);
    private final ManagerScopeService scopes = mock(ManagerScopeService.class);
    private final EntityManager entityManager = mock(EntityManager.class);
    private final Map<String, User> demoUsers = new HashMap<>();

    @BeforeEach
    void demoAccountsAndCategories() {
        addUser("admin@qaute.edu.vn", 99L);
        for (int index = 1; index <= 4; index++) addUser("manager" + index + "@qaute.edu.vn", (long) index);
        for (int index = 0; index < 10; index++) {
            addUser((24162101 + index) + "@student.hcmute.edu.vn", 101L + index);
        }
        when(users.findByEmailIgnoreCase(anyString())).thenAnswer(call -> Optional.ofNullable(demoUsers.get(call.getArgument(0))));
        String[] codes = {"DT_DKHP", "CT_HOCBONG", "TC_HOCPHI", "KTX_DANGKY"};
        for (int index = 0; index < codes.length; index++) {
            Category category = new Category();
            category.setId(11L + index);
            category.setDepartmentId(21L + index);
            category.setCode(codes[index]);
            when(categories.findByCode(codes[index])).thenReturn(Optional.of(category));
        }
        when(scopes.isInScope(anyLong(), anyLong())).thenAnswer(call ->
                ((Long) call.getArgument(0)) + 10 == (Long) call.getArgument(1));
        Query timestamps = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(timestamps);
        when(timestamps.setParameter(anyString(), any())).thenReturn(timestamps);
    }

    @Test
    void appointmentSeedIsRepeatableAndKeepsSlotStatesConsistent() {
        AvailabilityRuleRepository rules = mock(AvailabilityRuleRepository.class);
        AppointmentSlotRepository slots = mock(AppointmentSlotRepository.class);
        AppointmentRepository appointments = mock(AppointmentRepository.class);
        DepartmentRepository departments = mock(DepartmentRepository.class);
        SlotGenerationService generation = mock(SlotGenerationService.class);
        List<AvailabilityRule> savedRules = new ArrayList<>();
        List<AppointmentSlot> savedSlots = new ArrayList<>();
        List<Appointment> savedAppointments = new ArrayList<>();
        AppointmentSlot existingOnline = new AppointmentSlot();
        existingOnline.setId(1L);
        existingOnline.setManagerId(1L);
        existingOnline.setStartAt(LocalDateTime.now().toLocalDate().plusDays(3).atTime(9, 0));
        existingOnline.setEndAt(existingOnline.getStartAt().plusMinutes(30));
        existingOnline.setModality(Modality.ONLINE);
        existingOnline.setStatus(SlotStatus.AVAILABLE);
        savedSlots.add(existingOnline);
        for (long id = 21; id <= 24; id++) {
            Department department = Department.builder().name("Phòng " + id).location("Tòa A").build();
            when(departments.findById(id)).thenReturn(Optional.of(department));
        }
        when(rules.findByManagerIdOrderByDayOfWeekAscStartTimeAsc(anyLong())).thenAnswer(call ->
                savedRules.stream().filter(rule -> rule.getManagerId().equals(call.getArgument(0))).toList());
        when(rules.saveAll(any())).thenAnswer(call -> {
            List<AvailabilityRule> batch = call.getArgument(0);
            savedRules.addAll(batch);
            return batch;
        });
        doAnswer(call -> {
            Long managerId = call.getArgument(0);
            if (savedSlots.stream().noneMatch(slot -> managerId.equals(slot.getManagerId()) && slot.getModality() == Modality.BOTH)) {
                for (int halfHour = 0; halfHour < 2; halfHour++) {
                    AppointmentSlot slot = new AppointmentSlot();
                    slot.setId((long) savedSlots.size() + 1);
                    slot.setManagerId(managerId);
                    slot.setStartAt(LocalDateTime.now().toLocalDate().plusDays(4).atTime(9, 0).plusMinutes(halfHour * 30L));
                    slot.setEndAt(slot.getStartAt().plusMinutes(30));
                    slot.setModality(Modality.BOTH);
                    slot.setStatus(SlotStatus.AVAILABLE);
                    savedSlots.add(slot);
                }
            }
            return null;
        }).when(generation).generateForManager(anyLong());
        when(slots.findByManagerIdAndStartAtBetweenOrderByStartAtAsc(anyLong(), any(), any())).thenAnswer(call ->
                savedSlots.stream().filter(slot -> slot.getManagerId().equals(call.getArgument(0)))
                        .filter(slot -> !slot.getStartAt().isBefore(call.getArgument(1)))
                        .filter(slot -> !slot.getStartAt().isAfter(call.getArgument(2)))
                        .sorted(Comparator.comparing(AppointmentSlot::getStartAt)).toList());
        when(slots.findByManagerIdAndStartAt(anyLong(), any())).thenAnswer(call -> savedSlots.stream()
                .filter(slot -> slot.getManagerId().equals(call.getArgument(0)) && slot.getStartAt().equals(call.getArgument(1))).findFirst());
        when(slots.save(any())).thenAnswer(call -> {
            AppointmentSlot slot = call.getArgument(0);
            if (slot.getId() == null) {
                slot.setId((long) savedSlots.size() + 1);
                savedSlots.add(slot);
            }
            return slot;
        });
        when(appointments.existsByStudentIdAndTopic(anyLong(), anyString())).thenAnswer(call -> savedAppointments.stream()
                .anyMatch(appointment -> appointment.getStudentId().equals(call.getArgument(0)) && appointment.getTopic().equals(call.getArgument(1))));
        when(appointments.findBySlotIdIn(any())).thenAnswer(call -> {
            List<Long> ids = call.getArgument(0);
            return savedAppointments.stream().filter(appointment -> ids.contains(appointment.getSlotId())).toList();
        });
        when(appointments.saveAndFlush(any())).thenAnswer(call -> {
            Appointment appointment = call.getArgument(0);
            appointment.setId((long) savedAppointments.size() + 1);
            savedAppointments.add(appointment);
            return appointment;
        });
        AppointmentSeeder seeder = new AppointmentSeeder(users, categories, departments, rules,
                slots, appointments, generation, scopes, entityManager);

        seeder.seed();
        seeder.seed();

        assertEquals(50, seeder.order());
        assertEquals(40, savedRules.size());
        assertEquals(10, savedAppointments.size());
        assertEquals(SlotStatus.AVAILABLE, existingOnline.getStatus());
        assertTrue(savedAppointments.stream().noneMatch(a -> a.getSlotId().equals(existingOnline.getId())));
        assertEquals(4, savedAppointments.stream().filter(a -> a.getStatus() == AppointmentStatus.CONFIRMED).count());
        assertEquals(2, savedAppointments.stream().filter(a -> a.getStatus() == AppointmentStatus.COMPLETED).count());
        assertEquals(2, savedAppointments.stream().filter(a -> a.getStatus() == AppointmentStatus.NO_SHOW).count());
        assertEquals(2, savedAppointments.stream().filter(a -> a.getStatus() == AppointmentStatus.CANCELLED).count());
        for (AvailabilityRule rule : savedRules) {
            assertTrue(rule.getDayOfWeek() >= 1 && rule.getDayOfWeek() <= 5);
            assertTrue(rule.getStartTime().equals(LocalTime.of(9, 0)) || rule.getStartTime().equals(LocalTime.of(14, 0)));
            assertEquals(120, Duration.between(rule.getStartTime(), rule.getEndTime()).toMinutes());
            assertEquals(30, rule.getSlotMinutes());
            assertEquals(Modality.BOTH, rule.getModality());
            assertTrue(rule.getMeetingLink().startsWith("https://meet.example.com/qaute-"));
        }
        for (Appointment appointment : savedAppointments) {
            AppointmentSlot slot = savedSlots.stream().filter(s -> s.getId().equals(appointment.getSlotId())).findFirst().orElseThrow();
            assertEquals(appointment.getManagerId(), slot.getManagerId());
            assertEquals(appointment.getManagerId() + 10, appointment.getCategoryId());
            assertEquals(appointment.getStatus() == AppointmentStatus.CANCELLED ? SlotStatus.AVAILABLE : SlotStatus.BOOKED, slot.getStatus());
            if (appointment.getStatus() == AppointmentStatus.CONFIRMED) assertTrue(slot.getStartAt().isAfter(LocalDateTime.now()));
            if (appointment.getStatus() == AppointmentStatus.COMPLETED || appointment.getStatus() == AppointmentStatus.NO_SHOW)
                assertTrue(slot.getStartAt().isBefore(LocalDateTime.now()));
            if (appointment.getStatus() == AppointmentStatus.COMPLETED) assertFalse(appointment.getOutcomeSummary().isBlank());
        }
    }

    @Test
    void chatSeedIsRepeatableWithScopedParticipantsAndHistoricalMessages() {
        ChatSessionRepository sessions = mock(ChatSessionRepository.class);
        ChatMessageRepository messages = mock(ChatMessageRepository.class);
        List<ChatSession> savedSessions = new ArrayList<>();
        List<ChatMessage> savedMessages = new ArrayList<>();
        when(sessions.existsByStudentIdAndInitialQuestion(anyLong(), anyString())).thenAnswer(call -> savedSessions.stream()
                .anyMatch(session -> session.getStudentId().equals(call.getArgument(0)) && session.getInitialQuestion().equals(call.getArgument(1))));
        when(sessions.saveAndFlush(any())).thenAnswer(call -> {
            ChatSession session = call.getArgument(0);
            session.setId((long) savedSessions.size() + 1);
            savedSessions.add(session);
            return session;
        });
        when(messages.saveAllAndFlush(any())).thenAnswer(call -> {
            List<ChatMessage> batch = call.getArgument(0);
            savedMessages.addAll(batch);
            return batch;
        });
        ChatSeeder seeder = new ChatSeeder(users, categories, sessions, messages, scopes, entityManager);

        seeder.seed();
        seeder.seed();

        assertEquals(60, seeder.order());
        assertEquals(10, savedSessions.size());
        assertEquals(8, savedSessions.stream().filter(s -> s.getStatus() == ChatSessionStatus.ENDED).count());
        assertEquals(2, savedSessions.stream().filter(s -> s.getStatus() == ChatSessionStatus.EXPIRED).count());
        assertEquals(6, savedSessions.stream().filter(s -> s.getRating() != null).count());
        for (ChatSession session : savedSessions) {
            assertTrue(session.getStartedAt().isAfter(LocalDateTime.now().minusDays(30)));
            assertTrue(session.getEndedAt().isBefore(LocalDateTime.now()));
            assertTrue(session.getStartedAt().isBefore(session.getEndedAt()));
            List<ChatMessage> conversation = savedMessages.stream().filter(m -> m.getSessionId().equals(session.getId())).toList();
            if (session.getStatus() == ChatSessionStatus.ENDED) {
                assertEquals(session.getManagerId() + 10, session.getCategoryId());
                assertTrue(conversation.size() >= 4 && conversation.size() <= 10);
                assertNotNull(session.getAcceptedAt());
                for (int index = 0; index < conversation.size(); index++) {
                    ChatMessage message = conversation.get(index);
                    assertEquals(index % 2 == 0 ? ChatSenderType.STUDENT : ChatSenderType.MANAGER, message.getSenderType());
                    assertEquals(index % 2 == 0 ? session.getStudentId() : session.getManagerId(), message.getSenderId());
                    assertFalse(message.getSentAt().isBefore(session.getStartedAt()));
                    assertFalse(message.getSentAt().isAfter(session.getEndedAt()));
                }
            }
        }
        assertTrue(savedSessions.stream().filter(s -> s.getAcceptedAt() != null)
                .map(s -> Duration.between(s.getStartedAt(), s.getAcceptedAt()).getSeconds()).distinct().count() > 1);
    }

    @Test
    void publicAnnouncementsAreRepeatableAndReadyForTheUpdatedGuestPage() {
        AnnouncementRepository announcements = mock(AnnouncementRepository.class);
        List<Announcement> saved = new ArrayList<>();
        when(announcements.count()).thenAnswer(call -> (long) saved.size());
        when(announcements.save(any())).thenAnswer(call -> {
            Announcement announcement = call.getArgument(0);
            saved.add(announcement);
            return announcement;
        });
        AnnouncementSeeder seeder = new AnnouncementSeeder(announcements, users);

        seeder.seed();
        int firstRunCount = saved.size();
        seeder.seed();

        assertTrue(firstRunCount > 0);
        assertEquals(firstRunCount, saved.size());
        assertTrue(saved.stream().anyMatch(Announcement::isPinned));
        assertTrue(saved.stream().allMatch(a -> a.getAudience() == AnnouncementAudience.ALL
                && a.getStatus() == AnnouncementStatus.PUBLISHED
                && a.getPublishAt() != null && !a.getPublishAt().isAfter(LocalDateTime.now())
                && a.getTitle() != null && !a.getTitle().isBlank()
                && a.getBodyHtml() != null && !a.getBodyHtml().isBlank()));
        assertTrue(saved.stream().allMatch(a -> !a.isNotifyOnPublish() && a.getCreatedBy() == 99L));
    }

    private void addUser(String email, Long id) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        demoUsers.put(email, user);
    }
}
