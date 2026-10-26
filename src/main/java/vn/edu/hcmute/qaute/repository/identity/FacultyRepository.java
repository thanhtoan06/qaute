package vn.edu.hcmute.qaute.repository.identity;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.identity.Faculty;

// Khai báo truy vấn lưu trữ dữ liệu Faculty.
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    // Lấy các khoa đang hoạt động theo tên.
    List<Faculty> findByActiveTrueOrderByNameAsc();

    // Tìm khoa theo mã không phân biệt hoa thường.
    Optional<Faculty> findByCodeIgnoreCase(String code);
}
