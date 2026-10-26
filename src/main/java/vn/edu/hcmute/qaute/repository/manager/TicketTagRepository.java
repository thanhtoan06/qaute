package vn.edu.hcmute.qaute.repository.manager;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.manager.TicketTag;

// Khai báo truy vấn lưu trữ dữ liệu TicketTag.
public interface TicketTagRepository extends JpaRepository<TicketTag, Long> {
    // Tìm thẻ theo tên, không phân biệt chữ hoa chữ thường.
    Optional<TicketTag> findByNameIgnoreCase(String name);

    // Lấy toàn bộ thẻ theo thứ tự tên tăng dần.
    List<TicketTag> findAllByOrderByNameAsc();
}
