package vn.edu.hcmute.qaute.repository.system;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.system.SystemSetting;

public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {

    Optional<SystemSetting> findBySettingKey(String key);

    List<SystemSetting> findAllByOrderByGroupNameAscSettingKeyAsc();
}
