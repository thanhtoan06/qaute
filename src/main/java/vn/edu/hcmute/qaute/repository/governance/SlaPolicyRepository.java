package vn.edu.hcmute.qaute.repository.governance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmute.qaute.common.constant.TicketPriority;
import vn.edu.hcmute.qaute.entity.governance.SlaPolicy;

// Khai báo truy vấn lưu trữ dữ liệu SlaPolicy.
public interface SlaPolicyRepository extends JpaRepository<SlaPolicy, Long> {

    // Tìm chính sách đang hoạt động theo danh mục và độ ưu tiên.
    Optional<SlaPolicy> findFirstByCategoryIdAndPriorityAndActiveTrue(Long categoryId, TicketPriority priority);

    // Tìm chính sách mặc định đang hoạt động theo độ ưu tiên.
    Optional<SlaPolicy> findFirstByCategoryIdIsNullAndPriorityAndActiveTrue(TicketPriority priority);

    // Tìm chính sách mặc định đầu tiên theo mã.
    Optional<SlaPolicy> findFirstByCategoryIdIsNullAndPriorityOrderByIdAsc(TicketPriority priority);

    // Lấy toàn bộ chính sách theo danh mục và độ ưu tiên.
    List<SlaPolicy> findAllByOrderByCategoryIdAscPriorityAsc();

    // Tìm chính sách theo danh mục và độ ưu tiên.
    Optional<SlaPolicy> findByCategoryIdAndPriority(Long categoryId, TicketPriority priority);

    // Lấy chính sách theo mã danh mục.
    List<SlaPolicy> findByCategoryId(Long categoryId);
}
