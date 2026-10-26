package vn.edu.hcmute.qaute.repository.identity;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.identity.LoginHistory;

// Khai báo truy vấn lưu trữ dữ liệu LoginHistory.
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

    // Lấy lịch sử đăng nhập mới nhất của người dùng theo trang.
    List<LoginHistory> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
