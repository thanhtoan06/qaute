package vn.edu.hcmute.qaute.repository.governance;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import vn.edu.hcmute.qaute.entity.governance.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {

    /**
     * Xóa các bản ghi tạo trước mốc thời gian, trả về số bản ghi đã xóa.
     * Cần @Transactional vì phương thức xóa tự sinh không tự mở giao dịch; job dọn dữ liệu gọi trực tiếp.
     */
    @Transactional
    long deleteByCreatedAtBefore(LocalDateTime threshold);
}
