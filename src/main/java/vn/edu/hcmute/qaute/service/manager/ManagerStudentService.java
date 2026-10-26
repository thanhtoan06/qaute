package vn.edu.hcmute.qaute.service.manager;

import vn.edu.hcmute.qaute.common.util.BoundedPages;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.util.TextUtil;
import vn.edu.hcmute.qaute.dto.view.ManagerStudentDetailView;
import vn.edu.hcmute.qaute.dto.view.ManagerStudentListItem;
import vn.edu.hcmute.qaute.dto.view.StudentSummary;
import vn.edu.hcmute.qaute.entity.appointment.Appointment;
import vn.edu.hcmute.qaute.entity.chat.ChatSession;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.identity.UserLookupService;
import vn.edu.hcmute.qaute.service.organization.ManagerScopeService;

// Xử lý nghiệp vụ ManagerStudent trong hệ thống.
@Service
@Transactional(readOnly = true)
public class ManagerStudentService {
    private final EntityManager entityManager;
    private final ManagerScopeService scopes;
    private final UserLookupService users;
    private final AuditService audit;

    // Khởi tạo dịch vụ tra cứu thông tin sinh viên trong phạm vi phụ trách.
    public ManagerStudentService(EntityManager entityManager, ManagerScopeService scopes,
                                 UserLookupService users, AuditService audit) {
        this.entityManager = entityManager;
        this.scopes = scopes;
        this.users = users;
        this.audit = audit;
    }

    // Tìm kiếm và phân trang sinh viên có liên hệ với tư vấn viên.
    public Page<ManagerStudentListItem> search(Long managerId, String q, Pageable pageable) {
        pageable = BoundedPages.of(pageable);
        List<Long> categoryIds = scopes.getScopeCategoryIds(managerId);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);
        query.select(user).distinct(true).where(visible(cb, query, user, managerId, categoryIds, q));
        query.orderBy(cb.asc(user.get("fullName")), cb.asc(user.get("id")));

        TypedQuery<User> result = entityManager.createQuery(query);
        result.setFirstResult((int) Math.min(Integer.MAX_VALUE, pageable.getOffset()));
        result.setMaxResults(pageable.getPageSize());
        List<ManagerStudentListItem> items = result.getResultList().stream().map(student -> {
            StudentSummary summary = users.getStudentSummary(student.getId());
            return new ManagerStudentListItem(student.getId(), student.getFullName(),
                    student.getMssv(), student.getEmail(), summary.facultyName(), summary.classCode());
        }).toList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> countUser = countQuery.from(User.class);
        countQuery.select(cb.countDistinct(countUser.get("id")))
                .where(visible(cb, countQuery, countUser, managerId, categoryIds, q));
        long total = entityManager.createQuery(countQuery).getSingleResult();
        return new PageImpl<>(items, pageable, total);
    }

    // Lấy trang thông tin sinh viên với kích thước mặc định.
    public ManagerStudentDetailView detail(Long managerId, Long studentId) {
        return detail(managerId, studentId, BoundedPages.of(0, 15));
    }

    // Lấy chi tiết sinh viên cùng các yêu cầu, lịch hẹn và phiên trò chuyện.
    public ManagerStudentDetailView detail(Long managerId, Long studentId, Pageable requested) {
        Pageable page = BoundedPages.of(requested);
        List<Long> categoryIds = scopes.getScopeCategoryIds(managerId);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> accessQuery = cb.createQuery(Long.class);
        Root<User> user = accessQuery.from(User.class);
        accessQuery.select(cb.count(user.get("id"))).where(cb.and(
                cb.equal(user.get("id"), studentId),
                visible(cb, accessQuery, user, managerId, categoryIds, null)));
        if (entityManager.createQuery(accessQuery).getSingleResult() == 0) {
            throw new NotFoundException("Không tìm thấy sinh viên trong phạm vi phụ trách");
        }

        StudentSummary student = users.getStudentSummary(studentId);
        String ticketScope = "t.studentId = :studentId and (t.currentAssigneeId = :managerId"
                + (categoryIds.isEmpty() ? "" : " or t.categoryId in :categoryIds") + ")";
        TypedQuery<Ticket> ticketQuery = entityManager.createQuery("select t from Ticket t where "
                + ticketScope + " order by t.createdAt desc, t.id desc", Ticket.class);
        TypedQuery<Long> ticketCount = entityManager.createQuery("select count(t) from Ticket t where "
                + ticketScope, Long.class);
        for (var query : List.of(ticketQuery, ticketCount)) {
            query.setParameter("studentId", studentId).setParameter("managerId", managerId);
            if (!categoryIds.isEmpty()) query.setParameter("categoryIds", categoryIds);
        }
        Page<ManagerStudentDetailView.TicketItem> tickets = new PageImpl<>(ticketQuery
                .setFirstResult((int) page.getOffset()).setMaxResults(page.getPageSize()).getResultList()
                .stream().map(t -> new ManagerStudentDetailView.TicketItem(t.getId(), t.getCode(),
                        t.getTitle(), t.getStatus(), t.getCreatedAt())).toList(), page, ticketCount.getSingleResult());
        String appointmentWhere = " from Appointment a, AppointmentSlot s where a.slotId = s.id "
                + "and a.studentId = :studentId and a.managerId = :managerId";
        List<ManagerStudentDetailView.AppointmentItem> appointmentRows = entityManager.createQuery(
                "select a, s.startAt" + appointmentWhere + " order by s.startAt desc, a.id desc", Object[].class)
                .setParameter("studentId", studentId).setParameter("managerId", managerId)
                .setFirstResult((int) page.getOffset()).setMaxResults(page.getPageSize()).getResultList()
                .stream().map(row -> {
                    Appointment a = (Appointment) row[0];
                    return new ManagerStudentDetailView.AppointmentItem(a.getId(), a.getTopic(),
                            a.getStatus(), (LocalDateTime) row[1]);
                }).toList();
        long appointmentCount = entityManager.createQuery("select count(a)" + appointmentWhere, Long.class)
                .setParameter("studentId", studentId).setParameter("managerId", managerId).getSingleResult();
        Page<ManagerStudentDetailView.AppointmentItem> appointments = new PageImpl<>(appointmentRows, page, appointmentCount);
        String chatWhere = " from ChatSession s where s.studentId = :studentId and s.managerId = :managerId";
        List<ManagerStudentDetailView.ChatItem> chatRows = entityManager.createQuery("select s" + chatWhere
                + " order by s.startedAt desc, s.id desc", ChatSession.class)
                .setParameter("studentId", studentId).setParameter("managerId", managerId)
                .setFirstResult((int) page.getOffset()).setMaxResults(page.getPageSize()).getResultList()
                .stream().map(chat -> new ManagerStudentDetailView.ChatItem(chat.getId(), chat.getInitialQuestion(),
                        chat.getStatus(), chat.getStartedAt())).toList();
        long chatCount = entityManager.createQuery("select count(s)" + chatWhere, Long.class)
                .setParameter("studentId", studentId).setParameter("managerId", managerId).getSingleResult();
        Page<ManagerStudentDetailView.ChatItem> chats = new PageImpl<>(chatRows, page, chatCount);
        audit.log(AuditActions.VIEW_STUDENT_PROFILE, "user", studentId, null, null);
        return new ManagerStudentDetailView(student, tickets, appointments, chats);
    }

    // Tạo điều kiện xác định sinh viên thuộc phạm vi có thể xem.
    private Predicate visible(CriteriaBuilder cb, CriteriaQuery<?> query, Root<User> user,
                              Long managerId, List<Long> categoryIds, String q) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(user.get("role").get("code"), RoleCode.STUDENT));

        Subquery<Long> ticket = query.subquery(Long.class);
        Root<Ticket> t = ticket.from(Ticket.class);
        Predicate ticketAccess = cb.equal(t.get("currentAssigneeId"), managerId);
        if (categoryIds != null && !categoryIds.isEmpty()) {
            ticketAccess = cb.or(ticketAccess, t.get("categoryId").in(categoryIds));
        }
        ticket.select(t.get("id")).where(cb.equal(t.get("studentId"), user.get("id")), ticketAccess);

        Subquery<Long> appointment = query.subquery(Long.class);
        Root<Appointment> a = appointment.from(Appointment.class);
        appointment.select(a.get("id")).where(cb.equal(a.get("studentId"), user.get("id")),
                cb.equal(a.get("managerId"), managerId));

        Subquery<Long> chat = query.subquery(Long.class);
        Root<ChatSession> s = chat.from(ChatSession.class);
        chat.select(s.get("id")).where(cb.equal(s.get("studentId"), user.get("id")),
                cb.equal(s.get("managerId"), managerId));
        predicates.add(cb.or(cb.exists(ticket), cb.exists(appointment), cb.exists(chat)));

        if (q != null && !q.isBlank()) {
            String pattern = "%" + TextUtil.escapeLike(q.trim().toLowerCase(Locale.ROOT)) + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(user.get("fullName")), pattern, '\\'),
                    cb.like(cb.lower(user.get("mssv")), pattern, '\\'),
                    cb.like(cb.lower(user.get("email")), pattern, '\\')));
        }
        return cb.and(predicates.toArray(Predicate[]::new));
    }
}
