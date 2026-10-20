package vn.edu.hcmute.qaute.repository.report;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.qaute.common.constant.AppointmentStatus;
import vn.edu.hcmute.qaute.common.constant.ChatSessionStatus;
import vn.edu.hcmute.qaute.common.constant.EmailStatus;
import vn.edu.hcmute.qaute.common.constant.SlaStatus;
import vn.edu.hcmute.qaute.common.constant.TicketStatus;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.entity.appointment.Appointment;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;

@Repository
public class DashboardQueryRepository {
    private final EntityManager em;

    public DashboardQueryRepository(EntityManager em) { this.em = em; }

    public long ticketsCreated(LocalDateTime from, LocalDateTime to) {
        return em.createQuery("select count(t) from Ticket t where t.createdAt >= :from and t.createdAt < :to", Long.class)
                .setParameter("from", from).setParameter("to", to).getSingleResult();
    }

    public long unassigned() {
        return em.createQuery("select count(t) from Ticket t where t.status = :status", Long.class)
                .setParameter("status", TicketStatus.NEW).getSingleResult();
    }

    public long oldUnassigned(LocalDateTime cutoff) {
        return em.createQuery("select count(t) from Ticket t where t.status = :status and t.createdAt < :cutoff", Long.class)
                .setParameter("status", TicketStatus.NEW).setParameter("cutoff", cutoff).getSingleResult();
    }

    public long oldBacklog(LocalDateTime cutoff) {
        return em.createQuery("select count(t) from Ticket t where t.status in :statuses and t.createdAt < :cutoff", Long.class)
                .setParameter("statuses", List.of(TicketStatus.NEW, TicketStatus.ASSIGNED,
                        TicketStatus.IN_PROGRESS, TicketStatus.WAITING_STUDENT))
                .setParameter("cutoff", cutoff).getSingleResult();
    }

    public long chatCount(ChatSessionStatus status) {
        return em.createQuery("select count(c) from ChatSession c where c.status = :status", Long.class)
                .setParameter("status", status).getSingleResult();
    }

    public long appointmentsToday(LocalDateTime from, LocalDateTime to) {
        return em.createQuery("select count(a) from Appointment a, AppointmentSlot s where a.slotId = s.id "
                + "and a.status = :status and s.startAt >= :from and s.startAt < :to", Long.class)
                .setParameter("status", AppointmentStatus.CONFIRMED)
                .setParameter("from", from).setParameter("to", to).getSingleResult();
    }

    public long recentlyActiveUsers(LocalDateTime since) {
        return em.createQuery("select count(u) from User u where u.status = :status and u.lastLoginAt >= :since", Long.class)
                .setParameter("status", UserStatus.ACTIVE).setParameter("since", since).getSingleResult();
    }

    public long failedEmails() {
        return em.createQuery("select count(e) from EmailOutbox e where e.status = :status", Long.class)
                .setParameter("status", EmailStatus.FAILED).getSingleResult();
    }

    public long breachedTickets() {
        return em.createQuery("select count(t) from Ticket t where t.slaStatus = :sla and t.status not in :closed", Long.class)
                .setParameter("sla", SlaStatus.BREACHED)
                .setParameter("closed", List.of(TicketStatus.CLOSED, TicketStatus.CANCELLED)).getSingleResult();
    }

    public List<Ticket> managerRisk(Long managerId, int limit) {
        return em.createQuery("select t from Ticket t where t.currentAssigneeId = :id and t.slaStatus in :risks "
                + "and t.status not in :closed order by case when t.slaStatus = :breached then 0 else 1 end, t.createdAt", Ticket.class)
                .setParameter("id", managerId).setParameter("risks", List.of(SlaStatus.AT_RISK, SlaStatus.BREACHED))
                .setParameter("closed", List.of(TicketStatus.CLOSED, TicketStatus.CANCELLED))
                .setParameter("breached", SlaStatus.BREACHED).setMaxResults(limit).getResultList();
    }

    public List<Ticket> oldUnassignedInScope(List<Long> categoryIds, LocalDateTime cutoff, int limit) {
        if (categoryIds == null || categoryIds.isEmpty()) return List.of();
        return em.createQuery("select t from Ticket t where t.categoryId in :ids and t.status = :status "
                + "and t.createdAt < :cutoff order by t.createdAt", Ticket.class)
                .setParameter("ids", categoryIds).setParameter("status", TicketStatus.NEW)
                .setParameter("cutoff", cutoff).setMaxResults(limit).getResultList();
    }

    public List<Ticket> breachedAlerts(int limit) {
        return em.createQuery("select t from Ticket t where t.slaStatus = :sla and t.status not in :closed "
                + "order by t.createdAt", Ticket.class).setParameter("sla", SlaStatus.BREACHED)
                .setParameter("closed", List.of(TicketStatus.CLOSED, TicketStatus.CANCELLED))
                .setMaxResults(limit).getResultList();
    }

    public List<Appointment> nextAppointments(Long managerId, LocalDateTime now, int limit) {
        return em.createQuery("select a from Appointment a, AppointmentSlot s where a.slotId = s.id "
                + "and a.managerId = :id and a.status = :status and s.startAt >= :now order by s.startAt", Appointment.class)
                .setParameter("id", managerId).setParameter("status", AppointmentStatus.CONFIRMED)
                .setParameter("now", now).setMaxResults(limit).getResultList();
    }

    public List<Object[]> lowRatings(LocalDateTime since, int limit) {
        return em.createQuery("select r.ticketId, t.code, r.rating, r.comment from TicketReview r, Ticket t "
                + "where r.ticketId = t.id and r.rating <= 2 and r.createdAt >= :since "
                + "order by r.createdAt desc", Object[].class)
                .setParameter("since", since).setMaxResults(limit).getResultList();
    }
}
