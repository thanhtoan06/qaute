package vn.edu.hcmute.qaute.repository.manager;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.manager.TicketTagMap;

// Khai báo truy vấn lưu trữ dữ liệu TicketTagMap.
public interface TicketTagMapRepository extends JpaRepository<TicketTagMap, Long> {
    // Tìm các thẻ được gắn với vé.
    List<TicketTagMap> findByTicketId(Long ticketId);

    // Xóa liên kết thẻ với vé.
    void deleteByTicketIdAndTagId(Long ticketId, Long tagId);

    // Kiểm tra thẻ đã được gắn với vé hay chưa.
    boolean existsByTicketIdAndTagId(Long ticketId, Long tagId);

    // Đếm số thẻ được gắn với vé.
    long countByTicketId(Long ticketId);
}
