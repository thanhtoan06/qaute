package vn.edu.hcmute.qaute.repository.system;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.system.SystemSetting;

// Khai báo truy vấn lưu trữ dữ liệu SystemSetting.
public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {

    // Tìm cấu hình theo khóa.
    Optional<SystemSetting> findBySettingKey(String key);

    // Lấy danh sách cấu hình theo nhóm và khóa.
    List<SystemSetting> findAllByOrderByGroupNameAscSettingKeyAsc();
}
