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

    /** Ticket ở các trạng thái này là đã kết thúc; mọi trạng thái khác là "đang xử lý". */
    private static final Set<TicketStatus> DONE_STATUSES =
            Set.of(TicketStatus.CLOSED, TicketStatus.CANCELLED);

    @PersistenceContext
    private EntityManager entityManager;

    public long countOpenTicketsByCategoryIds(Collection<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty() || !ticketEntityExists()) {
            return 0;
        }
        return entityManager.createQuery(
                "select count(t) from Ticket t"
                        + " where t.categoryId in :ids and t.status not in :done",
                Long.class)
                .setParameter("ids", categoryIds)
                .setParameter("done", DONE_STATUSES)
                .getSingleResult();
    }

    public long countOpenTicketsByAssignee(Long managerId) {
        if (managerId == null || !ticketEntityExists()) {
            return 0;
        }
        return entityManager.createQuery(
                "select count(t) from Ticket t"
                        + " where t.currentAssigneeId = :id and t.status not in :done",
                Long.class)
                .setParameter("id", managerId)
                .setParameter("done", DONE_STATUSES)
                .getSingleResult();
    }

    /** Kiểm tra bằng metamodel để không phải chạy JPQL lỗi (làm transaction rollback-only). */
    private boolean ticketEntityExists() {
        return entityManager.getMetamodel().getEntities().stream()
                .anyMatch(e -> "Ticket".equals(e.getName()));
    }
}