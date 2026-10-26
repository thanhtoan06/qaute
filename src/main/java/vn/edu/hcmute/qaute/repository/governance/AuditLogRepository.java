package vn.edu.hcmute.qaute.repository.governance;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import vn.edu.hcmute.qaute.entity.governance.AuditLog;

// Khai báo truy vấn lưu trữ dữ liệu AuditLog.
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {

    @Query("select distinct a.action from AuditLog a order by a.action")
    // Lấy danh sách hành động khác nhau.
    List<String> findDistinctActions();

    /**
     * Xóa các bản ghi tạo trước mốc thời gian, trả về số bản ghi đã xóa.
     * Cần @Transactional vì phương thức xóa tự sinh không tự mở giao dịch; job dọn dữ liệu gọi trực tiếp.
     */
    @Transactional
    // Xóa nhật ký trước mốc thời gian và trả về số bản ghi.
    long deleteByCreatedAtBefore(LocalDateTime threshold);
}
