package vn.edu.hcmute.qaute.repository.organization;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmute.qaute.entity.organization.Department;

// Khai báo truy vấn lưu trữ dữ liệu Department.
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // Tìm đơn vị theo mã.
    Optional<Department> findByCode(String code);

    // Kiểm tra mã đơn vị đã tồn tại.
    boolean existsByCode(String code);

    // Kiểm tra mã đơn vị đã tồn tại ở mục khác.
    boolean existsByCodeAndIdNot(String code, Long id);

    // Lấy toàn bộ đơn vị theo thứ tự sắp xếp và tên.
    List<Department> findAllByOrderBySortOrderAscNameAsc();

    // Lấy đơn vị đang hoạt động theo thứ tự sắp xếp và tên.
    List<Department> findByActiveTrueOrderBySortOrderAscNameAsc();
}
