package vn.edu.hcmute.qaute.repository.organization;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmute.qaute.entity.organization.Category;

// Khai báo truy vấn lưu trữ dữ liệu Category.
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Tìm danh mục theo mã.
    Optional<Category> findByCode(String code);

    // Kiểm tra mã danh mục đã tồn tại.
    boolean existsByCode(String code);

    // Kiểm tra mã danh mục đã tồn tại ở mục khác.
    boolean existsByCodeAndIdNot(String code, Long id);

    // Lấy danh mục của đơn vị theo thứ tự sắp xếp và tên.
    List<Category> findByDepartmentIdOrderBySortOrderAscNameAsc(Long departmentId);

    // Lấy danh mục con theo thứ tự sắp xếp và tên.
    List<Category> findByParentIdOrderBySortOrderAscNameAsc(Long parentId);

    // Lấy danh mục đang hoạt động theo thứ tự sắp xếp và tên.
    List<Category> findByActiveTrueOrderBySortOrderAscNameAsc();

    // Lấy danh mục theo mã danh mục cha.
    List<Category> findByParentId(Long parentId);

    // Đếm danh mục của đơn vị.
    long countByDepartmentId(Long departmentId);
}
