package vn.edu.hcmute.qaute.repository.report;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Repository;
import vn.edu.hcmute.qaute.dto.view.report.ReportRange;
import vn.edu.hcmute.qaute.dto.view.report.ReportScope;

// Khai báo truy vấn lưu trữ dữ liệu ServiceReportQuery.
@Repository
public class ServiceReportQueryRepository {
    private final EntityManager em;

    // Khởi tạo kho truy vấn báo cáo dịch vụ.
    public ServiceReportQueryRepository(EntityManager em) { this.em = em; }

    // Tổng hợp số liệu các phiên trò chuyện.
    public Object[] chatTotals(ReportScope scope, ReportRange range) {
        Query q = em.createNativeQuery("select count(*), "
                + "avg(case when c.accepted_at is not null then timestampdiff(second, c.started_at, c.accepted_at) end), "
                + "avg(case when c.accepted_at is not null and c.ended_at is not null "
                + "then timestampdiff(second, c.accepted_at, c.ended_at) / 60.0 end), "
                + "sum(case when c.status = :expired or (c.status = :ended and c.end_reason = :studentCancel "
                + "and c.accepted_at is null) then 1 else 0 end) "
                + "from chat_sessions c where " + scopeClause(scope, "c")
                + " and c.started_at >= :from and c.started_at < :to");
        bind(q, scope, range);
        q.setParameter("expired", "EXPIRED").setParameter("ended", "ENDED")
                .setParameter("studentCancel", "STUDENT_CANCEL");
        return (Object[]) q.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    // Thống kê số phiên trò chuyện theo giờ.
    public List<Object[]> chatsPerHour(ReportScope scope, ReportRange range) {
        Query q = em.createNativeQuery("select hour(c.started_at), count(*) from chat_sessions c where "
                + scopeClause(scope, "c") + " and c.started_at >= :from and c.started_at < :to "
                + "group by hour(c.started_at) order by hour(c.started_at)");
        bind(q, scope, range);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    // Thống kê số phiên trò chuyện theo quản lý.
    public List<Object[]> chatsPerManager(ReportScope scope, ReportRange range) {
        Query q = em.createNativeQuery("select u.full_name, count(*) from chat_sessions c "
                + "join users u on u.id = c.manager_id where " + scopeClause(scope, "c")
                + " and c.started_at >= :from and c.started_at < :to "
                + "group by c.manager_id, u.full_name order by count(*) desc, u.full_name");
        bind(q, scope, range);
        return q.getResultList();
    }

    // Tổng hợp số liệu lịch hẹn.
    public Object[] appointmentTotals(ReportScope scope, ReportRange range) {
        Query q = em.createNativeQuery("select count(*), "
                + "sum(case when a.status = :completed then 1 else 0 end), "
                + "sum(case when a.status = :noShow then 1 else 0 end), "
                + "sum(case when a.status = :cancelled then 1 else 0 end) "
                + "from appointments a where " + scopeClause(scope, "a")
                + " and a.created_at >= :from and a.created_at < :to");
        bind(q, scope, range);
        q.setParameter("completed", "COMPLETED").setParameter("noShow", "NO_SHOW")
                .setParameter("cancelled", "CANCELLED");
        return (Object[]) q.getSingleResult();
    }

    // Tổng hợp số liệu khung giờ và lượt đặt.
    public long[] slotTotals(ReportRange range, Set<Long> managerIds) {
        if (managerIds != null && managerIds.isEmpty()) return new long[]{0, 0};
        Query q = em.createNativeQuery("select count(*), sum(case when s.status = :booked then 1 else 0 end) "
                + "from appointment_slots s where s.start_at >= :from and s.start_at < :to "
                + "and s.status <> :blocked" + (managerIds == null ? "" : " and s.manager_id in (:managerIds)"));
        bindRange(q, range);
        q.setParameter("booked", "BOOKED").setParameter("blocked", "BLOCKED");
        if (managerIds != null) q.setParameter("managerIds", managerIds);
        Object[] row = (Object[]) q.getSingleResult();
        return new long[]{number(row[0]), number(row[1])};
    }

    // Đếm lượt xem câu hỏi thường gặp.
    public long faqViews(ReportRange range) {
        Query q = em.createQuery("select count(v) from FaqView v where v.lastViewedAt >= :from "
                + "and v.lastViewedAt < :to");
        bindRange(q, range);
        return number(q.getSingleResult());
    }

    // Tổng hợp phản hồi về câu hỏi thường gặp.
    public long[] feedback(ReportRange range) {
        Query q = em.createQuery("select count(f), sum(case when f.helpful = true then 1 else 0 end) "
                + "from FaqFeedback f where f.updatedAt >= :from and f.updatedAt < :to");
        bindRange(q, range);
        Object[] row = (Object[]) q.getSingleResult();
        return new long[]{number(row[0]), number(row[1])};
    }

    // Tổng hợp số liệu tìm kiếm.
    public long[] searches(ReportRange range) {
        Query q = em.createQuery("select "
                + "sum(case when s.resultCount is not null then 1 else 0 end), "
                + "sum(case when s.resultCount = 0 then 1 else 0 end), "
                + "sum(case when s.clickedFaqId is not null then 1 else 0 end) "
                + "from SearchLog s where s.createdAt >= :from and s.createdAt < :to");
        bindRange(q, range);
        Object[] row = (Object[]) q.getSingleResult();
        return new long[]{number(row[0]), number(row[1]), number(row[2])};
    }

    @SuppressWarnings("unchecked")
    // Lấy các truy vấn tìm kiếm không có kết quả phổ biến nhất.
    public List<Object[]> topNoResultQueries(ReportRange range) {
        Query q = em.createQuery("select s.queryNorm, count(s) from SearchLog s "
                + "where s.createdAt >= :from and s.createdAt < :to and s.resultCount = 0 "
                + "group by s.queryNorm order by count(s) desc, s.queryNorm");
        bindRange(q, range);
        return q.setMaxResults(30).getResultList();
    }

    @SuppressWarnings("unchecked")
    // Lấy các câu hỏi thường gặp có tỷ lệ hữu ích thấp.
    public List<Object[]> lowHelpfulFaqs() {
        return em.createQuery("select f.title, f.helpfulCount, f.notHelpfulCount from Faq f "
                        + "where f.helpfulCount + f.notHelpfulCount >= :minimum "
                        + "and f.helpfulCount * 2 < f.helpfulCount + f.notHelpfulCount "
                        + "order by f.helpfulCount asc, f.notHelpfulCount desc")
                .setParameter("minimum", 5).setMaxResults(30).getResultList();
    }

    // Tổng hợp số liệu chuyển hướng từ tìm kiếm sang câu hỏi thường gặp.
    public long[] deflection(ReportRange range) {
        Query q = em.createNativeQuery("select count(*), "
                + "sum(case when s.user_id is not null and not exists (select 1 from tickets t "
                + "where t.student_id = s.user_id and t.created_at >= s.created_at "
                + "and t.created_at < date_add(s.created_at, interval 30 minute)) then 1 else 0 end) "
                + "from search_logs s where s.source = :source and s.clicked_faq_id is not null "
                + "and s.created_at >= :from and s.created_at < :to");
        bindRange(q, range);
        q.setParameter("source", "TICKET_FORM");
        Object[] row = (Object[]) q.getSingleResult();
        return new long[]{number(row[0]), number(row[1])};
    }

    // Đếm phiên trò chuyện đã được quản lý tiếp nhận.
    public long managerChats(Long managerId, ReportRange range) {
        Query q = em.createQuery("select count(c) from ChatSession c where c.managerId = :managerId "
                + "and c.acceptedAt >= :from and c.acceptedAt < :to");
        bindRange(q, range);
        q.setParameter("managerId", managerId);
        return number(q.getSingleResult());
    }

    // Tạo điều kiện lọc theo phạm vi báo cáo.
    private String scopeClause(ReportScope scope, String alias) {
        if (scope == null || (scope.managerId() == null && scope.categoryIds() == null)) return "1 = 1";
        boolean categories = scope.categoryIds() != null && !scope.categoryIds().isEmpty();
        boolean manager = scope.managerId() != null;
        if (categories && manager) return "(" + alias + ".manager_id = :scopeManagerId or "
                + alias + ".category_id in (:categoryIds))";
        if (categories) return alias + ".category_id in (:categoryIds)";
        if (manager) return alias + ".manager_id = :scopeManagerId";
        return "1 = 0";
    }

    // Gán tham số phạm vi và thời gian cho truy vấn.
    private void bind(Query q, ReportScope scope, ReportRange range) {
        bindRange(q, range);
        if (scope == null) return;
        if (scope.managerId() != null) q.setParameter("scopeManagerId", scope.managerId());
        if (scope.categoryIds() != null && !scope.categoryIds().isEmpty()) q.setParameter("categoryIds", scope.categoryIds());
    }

    // Gán khoảng thời gian cho truy vấn.
    private void bindRange(Query q, ReportRange range) {
        LocalDateTime from = range.from().atStartOfDay();
        LocalDateTime to = range.to().plusDays(1).atStartOfDay();
        q.setParameter("from", from).setParameter("to", to);
    }

    // Chuyển giá trị số thành long, mặc định bằng không nếu null.
    private long number(Object value) { return value == null ? 0 : ((Number) value).longValue(); }
}
