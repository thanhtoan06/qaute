package vn.edu.hcmute.qaute.repository.manager;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.manager.TicketDraft;

// Khai báo truy vấn lưu trữ dữ liệu TicketDraft.
public interface TicketDraftRepository extends JpaRepository<TicketDraft, Long> {
    // Tìm bản nháp theo mã vé và mã quản lý.
    Optional<TicketDraft> findByTicketIdAndManagerId(Long ticketId, Long managerId);

    // Xóa bản nháp theo mã vé và mã quản lý.
    void deleteByTicketIdAndManagerId(Long ticketId, Long managerId);
}
