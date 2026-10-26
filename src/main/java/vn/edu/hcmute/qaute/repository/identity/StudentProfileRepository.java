package vn.edu.hcmute.qaute.repository.identity;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.identity.StudentProfile;

// Khai báo truy vấn lưu trữ dữ liệu StudentProfile.
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    // Tìm hồ sơ sinh viên theo mã người dùng.
    Optional<StudentProfile> findByUserId(Long userId);
}
