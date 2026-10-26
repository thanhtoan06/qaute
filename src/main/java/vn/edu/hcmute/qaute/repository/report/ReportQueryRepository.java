package vn.edu.hcmute.qaute.repository.report;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.qaute.dto.view.report.ReportRange;
import vn.edu.hcmute.qaute.dto.view.report.ReportScope;
import vn.edu.hcmute.qaute.entity.ticket.Ticket;

/** Shared parameterized scope predicate for every ticket metric. */
@Repository
public class ReportQueryRepository {
    private final EntityManager em;

    // Khởi tạo kho truy vấn báo cáo.
    public ReportQueryRepository(EntityManager em) { this.em = em; }

    // Thống kê số phiếu được tạo theo ngày.
    public List<Object[]> createdPerDay(ReportScope scope, ReportRange range) {
        return rows("select function('date', t.createdAt), count(t) from Ticket t where "
                + scopeClause(scope) + " and t.createdAt >= :from and t.createdAt < :to "
                + "group by function('date', t.createdAt) order by function('date', t.createdAt)",
                scope, range, Map.of());
    }

    // Thống kê số phiếu được tạo theo giờ.
    public List<Object[]> createdPerHour(ReportScope scope, ReportRange range) {
        return rows("select function('hour', t.createdAt), count(t) from Ticket t where "
                + scopeClause(scope) + " and t.createdAt >= :from and t.createdAt < :to "
                + "group by function('hour', t.createdAt) order by function('hour', t.createdAt)",
                scope, range, Map.of());
    }

    // Thống kê số phiếu theo danh mục.
    public List<Object[]> createdByCategory(ReportScope scope, ReportRange range) {
        return rows("select t.categoryId, count(t) from Ticket t where " + scopeClause(scope)
                + " and t.createdAt >= :from and t.createdAt < :to group by t.categoryId order by count(t) desc",
                scope, range, Map.of());
    }

    // Thống kê số phiếu theo trạng thái.
    public List<Object[]> createdByStatus(ReportScope scope, ReportRange range) {
        return rows("select t.status, count(t) from Ticket t where " + scopeClause(scope)
                + " and t.createdAt >= :from and t.createdAt < :to group by t.status order by count(t) desc",
                scope, range, Map.of());
    }

    // Lấy các phiếu mới nhất trong phạm vi báo cáo.
    public List<Ticket> latestCreated(ReportScope scope, ReportRange range, int limit) {
        String jpql = "select t from Ticket t where " + scopeClause(scope)
                + " and t.createdAt >= :from and t.createdAt < :to order by t.createdAt desc, t.id desc";
        TypedQuery<Ticket> query = em.createQuery(jpql, Ticket.class);
        bind(query, scope, range, Map.of());
        return query.setMaxResults(limit).getResultList();
    }

    // Tổng hợp số liệu tuân thủ SLA.
    public long[] slaCounts(ReportScope scope, ReportRange range) {
        String jpql = "select count(t), sum(case when "
                + "(t.slaFirstResponseDueAt is null or (t.firstResponseAt is not null and t.firstResponseAt <= t.slaFirstResponseDueAt)) "
                + "and (t.resolvedAt is null or t.slaResolutionDueAt is null or t.resolvedAt <= t.slaResolutionDueAt) "
                + "then 1 else 0 end) from Ticket t where " + scopeClause(scope)
                + " and t.createdAt >= :from and t.createdAt < :to "
                + "and (t.slaFirstResponseDueAt is not null or t.slaResolutionDueAt is not null)";
        Object[] row = rows(jpql, scope, range, Map.of()).get(0);
        return new long[]{number(row[0]), number(row[1])};
    }

    // Tổng hợp số phiếu tồn đọng theo thời gian.
    public long[] backlog(ReportScope scope, LocalDateTime now) {
        String jpql = "select count(t), "
                + "sum(case when t.createdAt > :d1 then 1 else 0 end), "
                + "sum(case when t.createdAt <= :d1 and t.createdAt > :d3 then 1 else 0 end), "
                + "sum(case when t.createdAt <= :d3 then 1 else 0 end) "
                + "from Ticket t where " + scopeClause(scope)
                + " and t.status not in :finished";
        Object[] row = rows(jpql, scope, null, Map.of("d1", now.minusHours(24), "d3", now.minusDays(3),
                "finished", List.of(vn.edu.hcmute.qaute.common.constant.TicketStatus.RESOLVED,
                        vn.edu.hcmute.qaute.common.constant.TicketStatus.CLOSED,
                        vn.edu.hcmute.qaute.common.constant.TicketStatus.CANCELLED))).get(0);
        return new long[]{number(row[0]), number(row[1]), number(row[2]), number(row[3])};
    }

    // Đếm phiếu đã xử lý và được mở lại.
    public long[] resolvedAndReopened(ReportScope scope, ReportRange range) {
        String jpql = "select count(t), sum(case when t.reopenCount > 0 then 1 else 0 end) "
                + "from Ticket t where " + scopeClause(scope)
                + " and t.resolvedAt >= :from and t.resolvedAt < :to";
        Object[] row = rows(jpql, scope, range, Map.of()).get(0);
        return new long[]{number(row[0]), number(row[1])};
    }

    // Lấy đánh giá của các phiếu trong phạm vi báo cáo.
    public List<Object[]> reviews(ReportScope scope, ReportRange range) {
        String jpql = "select r.rating, r.reasonCodes from TicketReview r, Ticket t "
                + "where r.ticketId = t.id and " + scopeClause(scope)
                + " and r.createdAt >= :from and r.createdAt < :to";
        return rows(jpql, scope, range, Map.of());
    }

    @SuppressWarnings("unchecked")
    // Lấy danh sách ứng viên quản lý.
    public List<Object[]> managerCandidates() {
        return em.createNativeQuery("select u.id, u.full_name, coalesce(p.max_concurrent_tickets, 10) "
                        + "from users u join roles r on r.id = u.role_id "
                        + "left join manager_profiles p on p.user_id = u.id "
                        + "where r.code = :role and u.status = :status order by u.full_name")
                .setParameter("role", "MANAGER").setParameter("status", "ACTIVE").getResultList();
    }

    // Tổng hợp khối lượng công việc của quản lý.
    public long[] managerWorkload(Long managerId, ReportScope scope, ReportRange range) {
        Object[] row = rows("select "
                + "sum(case when t.status not in :finished then 1 else 0 end), "
                + "sum(case when t.resolvedAt >= :from and t.resolvedAt < :to then 1 else 0 end) "
                + "from Ticket t where t.currentAssigneeId = :managerId"
                + " and " + scopeClause(scope), scope, range,
                Map.of("finished", List.of(vn.edu.hcmute.qaute.common.constant.TicketStatus.RESOLVED,
                        vn.edu.hcmute.qaute.common.constant.TicketStatus.CLOSED,
                        vn.edu.hcmute.qaute.common.constant.TicketStatus.CANCELLED),
                        "managerId", managerId)).get(0);
        return new long[]{number(row[0]), number(row[1])};
    }

    // Thực thi truy vấn báo cáo và trả về các hàng kết quả.
    private List<Object[]> rows(String jpql, ReportScope scope, ReportRange range, Map<String, Object> extra) {
        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        bind(query, scope, range, extra);
        return query.getResultList();
    }

    // Gán tham số phạm vi, thời gian và tham số bổ sung.
    private void bind(jakarta.persistence.Query query, ReportScope scope, ReportRange range, Map<String, Object> extra) {
        if (scope != null) {
            if (scope.categoryIds() != null && !scope.categoryIds().isEmpty()) query.setParameter("categoryIds", scope.categoryIds());
            if (scope.managerId() != null) query.setParameter("scopeManagerId", scope.managerId());
        }
        if (range != null) {
            query.setParameter("from", range.from().atStartOfDay());
            query.setParameter("to", range.to().plusDays(1).atStartOfDay());
        }
        extra.forEach(query::setParameter);
    }

    // Tạo điều kiện lọc theo phạm vi báo cáo.
    private String scopeClause(ReportScope scope) {
        if (scope == null || (scope.managerId() == null && scope.categoryIds() == null)) return "1 = 1";
        boolean categories = scope.categoryIds() != null && !scope.categoryIds().isEmpty();
        boolean manager = scope.managerId() != null;
        if (categories && manager) return "(t.categoryId in :categoryIds or t.currentAssigneeId = :scopeManagerId)";
        if (categories) return "t.categoryId in :categoryIds";
        if (manager) return "t.currentAssigneeId = :scopeManagerId";
        return "1 = 0";
    }

    // Chuyển giá trị số thành long, mặc định bằng không nếu null.
    private long number(Object value) { return value == null ? 0 : ((Number) value).longValue(); }
}
