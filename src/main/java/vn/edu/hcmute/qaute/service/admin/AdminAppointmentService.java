package vn.edu.hcmute.qaute.service.admin;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.util.BoundedPages;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.dto.request.admin.AdminAppointmentFilter;
import vn.edu.hcmute.qaute.dto.view.AdminAppointmentListItem;
import vn.edu.hcmute.qaute.dto.view.UserSummary;
import vn.edu.hcmute.qaute.entity.appointment.Appointment;
import vn.edu.hcmute.qaute.entity.appointment.AppointmentSlot;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentRepository;
import vn.edu.hcmute.qaute.repository.appointment.AppointmentSlotRepository;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.organization.CategoryQueryService;

@Service
@Transactional(readOnly = true)
public class AdminAppointmentService {
    private final AppointmentRepository appointments;
    private final AppointmentSlotRepository slots;
    private final UserLookupService users;
    private final CategoryQueryService categories;

    public AdminAppointmentService(AppointmentRepository appointments, AppointmentSlotRepository slots,
            UserLookupService users, CategoryQueryService categories) {
        this.appointments = appointments;
        this.slots = slots;
        this.users = users;
        this.categories = categories;
    }

    public Page<AdminAppointmentListItem> search(AdminAppointmentFilter filter, Pageable pageable) {
        pageable = BoundedPages.of(pageable);
        AdminAppointmentFilter f = filter == null ? new AdminAppointmentFilter() : filter;
        Specification<Appointment> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (f.getQ() != null && !f.getQ().isBlank()) {
                String needle = "%" + TextUtil.escapeLike(f.getQ().trim().toLowerCase(Locale.ROOT)) + "%";
                predicates.add(cb.like(cb.lower(root.get("topic")), needle, '\\'));
            }
            if (f.getStatus() != null) predicates.add(cb.equal(root.get("status"), f.getStatus()));
            if (f.getModality() != null) predicates.add(cb.equal(root.get("modality"), f.getModality()));
            if (f.getManagerId() != null) predicates.add(cb.equal(root.get("managerId"), f.getManagerId()));
            if (f.getFrom() != null || f.getTo() != null) {
                Subquery<Long> slotIds = query.subquery(Long.class);
                Root<AppointmentSlot> slot = slotIds.from(AppointmentSlot.class);
                List<Predicate> slotPredicates = new ArrayList<>();
                slotPredicates.add(cb.equal(slot.get("id"), root.get("slotId")));
                if (f.getFrom() != null) {
                    slotPredicates.add(cb.greaterThanOrEqualTo(
                            slot.get("startAt"), f.getFrom().atStartOfDay()));
                }
                if (f.getTo() != null) {
                    slotPredicates.add(cb.lessThan(
                            slot.get("startAt"), f.getTo().plusDays(1).atStartOfDay()));
                }
                slotIds.select(slot.get("id")).where(slotPredicates.toArray(Predicate[]::new));
                predicates.add(cb.exists(slotIds));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };

        Page<Appointment> page = appointments.findAll(specification, pageable);
        Set<Long> userIds = new LinkedHashSet<>();
        Set<Long> slotIds = new LinkedHashSet<>();
        Set<Long> categoryIds = new LinkedHashSet<>();
        page.forEach(item -> {
            userIds.add(item.getStudentId());
            userIds.add(item.getManagerId());
            slotIds.add(item.getSlotId());
            if (item.getCategoryId() != null) categoryIds.add(item.getCategoryId());
        });
        Map<Long, UserSummary> userSummaries = users.getSummaries(userIds);
        Map<Long, AppointmentSlot> appointmentSlots = new HashMap<>();
        slots.findAllById(slotIds).forEach(slot -> appointmentSlots.put(slot.getId(), slot));
        Map<Long, String> categoryPaths = new HashMap<>();
        categoryIds.forEach(id -> categoryPaths.put(id, categories.getDisplayPath(id)));

        return page.map(item -> {
            AppointmentSlot slot = appointmentSlots.get(item.getSlotId());
            if (slot == null) {
                throw new IllegalStateException("Không tìm thấy khung giờ của lịch hẹn " + item.getId());
            }
            UserSummary student = userSummaries.get(item.getStudentId());
            UserSummary manager = userSummaries.get(item.getManagerId());
            return new AdminAppointmentListItem(item.getId(),
                    student == null ? "Không rõ" : student.fullName(),
                    student == null ? "" : student.email(),
                    manager == null ? "Không rõ" : manager.fullName(),
                    manager == null ? "" : manager.email(),
                    item.getTopic(), categoryPaths.getOrDefault(item.getCategoryId(), ""),
                    item.getModality(), item.getStatus(), slot.getStartAt(), slot.getEndAt(),
                    slot.getLocation());
        });
    }
}
