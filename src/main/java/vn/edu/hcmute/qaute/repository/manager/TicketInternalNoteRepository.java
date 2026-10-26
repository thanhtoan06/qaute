package vn.edu.hcmute.qaute.repository.manager;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.manager.TicketInternalNote;

// Khai báo truy vấn lưu trữ dữ liệu TicketInternalNote.
public interface TicketInternalNoteRepository extends JpaRepository<TicketInternalNote, Long> {
    // Lấy ghi chú nội bộ theo mã vé, ưu tiên ghim và mới nhất.
    List<TicketInternalNote> findByTicketIdOrderByPinnedDescCreatedAtDesc(Long ticketId);
}
