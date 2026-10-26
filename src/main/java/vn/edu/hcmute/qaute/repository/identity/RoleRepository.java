package vn.edu.hcmute.qaute.repository.identity;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.entity.identity.Role;

// Khai báo truy vấn lưu trữ dữ liệu Role.
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Tìm vai trò theo mã.
    Optional<Role> findByCode(RoleCode code);
}
