package vn.edu.hcmute.qaute.service.organization;

import java.util.Collection;
import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import vn.edu.hcmute.qaute.common.constant.TicketStatus;

/**
 * Chắn việc tắt/xóa chuyên mục và phòng ban khi vẫn còn yêu cầu đang xử lý.
 * Truy vấn trực tiếp bằng EntityManager để không phụ thuộc repository của TV1;
 * entity Ticket chưa có trong dự án thì coi như không có ticket nào.
 */
@Component
public class TicketUsageGuard {

    /** Ticket chưa kết thúc thì tính là "đang xử lý". */
    private static final Set<TicketStatus> OPEN_STATUSES = Set.of(
            TicketStatus.NEW,
            TicketStatus.ASSIGNED,
            TicketStatus.IN_PROGRESS,
            TicketStatus.WAITING_STUDENT,
            TicketStatus.RESOLVED);

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Số ticket chưa kết thúc thuộc các chuyên mục cho trước.
     * Danh sách rỗng hoặc entity Ticket chưa có thì trả 0.
     */
    public long countOpenTicketsByCategoryIds(Collection<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return 0;
        }
        try {
            Long count = entityManager.createQuery(
                    "select count(t) from Ticket t"
                            + " where t.categoryId in :ids and t.status not in :done",
                    Long.class)
                    .setParameter("ids", categoryIds)
                    .setParameter("done", Set.of(TicketStatus.CLOSED, TicketStatus.CANCELLED))
                    .getSingleResult();
            return count == null ? 0 : count;
        } catch (IllegalArgumentException e) {
            // Entity Ticket của TV1 chưa có trong dự án.
            return 0;
        }
    }

    /**
     * Số ticket chưa kết thúc do Manager đang phụ trách.
     * Entity Ticket chưa có trong dự án thì trả 0.
     */
    public long countOpenTicketsByAssignee(Long managerId) {
        if (managerId == null) {
            return 0;
        }
        try {
            Long count = entityManager.createQuery(
                    "select count(t) from Ticket t"
                            + " where t.currentAssigneeId = :id and t.status in :st",
                    Long.class)
                    .setParameter("id", managerId)
                    .setParameter("st", OPEN_STATUSES)
                    .getSingleResult();
            return count == null ? 0 : count;
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }
}
