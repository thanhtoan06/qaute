package vn.edu.hcmute.qaute.service.organization;

import java.util.List;

import vn.edu.hcmute.qaute.dto.view.ManagerOption;

/**
 * Phạm vi chuyên mục của Manager: kiểm tra quyền, tra cứu tư vấn viên phù hợp,
 * giới hạn số yêu cầu đồng thời. Mọi truy vấn Manager ở tầng Service đều đi qua đây.
 * Gán ở chuyên mục cha thì ngầm bao gồm cả chuyên mục con.
 */
public interface ManagerScopeService {

    /**
     * Manager có được giao chuyên mục không: có dòng gán trỏ tới chuyên mục
     * hoặc tổ tiên của nó. Chuyên mục không tồn tại thì trả false.
     */
    boolean isInScope(Long managerId, Long categoryId);

    /**
     * Mọi chuyên mục thuộc phạm vi: các chuyên mục được giao và toàn bộ hậu duệ.
     * Không được giao gì thì trả danh sách rỗng.
     */
    List<Long> getScopeCategoryIds(Long managerId);

    /**
     * Id các Manager (vai trò MANAGER, trạng thái ACTIVE) được giao chuyên mục
     * hoặc tổ tiên của nó. Không ai thì trả danh sách rỗng.
     */
    List<Long> findManagerIdsInScope(Long categoryId);

    /**
     * Danh sách tư vấn viên phù hợp để giao ticket hoặc đặt lịch hẹn.
     * Chỉ Manager ACTIVE; thiếu hồ sơ thì trạng thái trực coi như OFFLINE.
     * Sắp trạng thái ONLINE trước rồi theo tên.
     */
    List<ManagerOption> findAdvisors(Long categoryId);

    /**
     * Manager ACTIVE và số ticket đang phụ trách còn dưới giới hạn đồng thời.
     * Thiếu hồ sơ thì giới hạn mặc định là 10.
     */
    boolean isAvailableForAssignment(Long managerId);

    /**
     * Số ticket Manager đang phụ trách (ASSIGNED, IN_PROGRESS, WAITING_STUDENT).
     * Entity Ticket của TV1 chưa có trong dự án thì trả 0.
     */
    int currentOpenTicketCount(Long managerId);
}
