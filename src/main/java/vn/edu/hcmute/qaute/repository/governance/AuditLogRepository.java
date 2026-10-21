package vn.edu.hcmute.qaute.repository.governance;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import vn.edu.hcmute.qaute.entity.governance.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {

    @Query("select distinct a.action from AuditLog a order by a.action")
    List<String> findDistinctActions();

    /**
     * Xóa các bản ghi tạo trước mốc thời gian, trả về số bản ghi đã xóa.
     * Cần @Transactional vì phương thức xóa tự sinh không tự mở giao dịch; job dọn dữ liệu gọi trực tiếp.
     */
    @Transactional
    long deleteByCreatedAtBefore(LocalDateTime threshold);
}
