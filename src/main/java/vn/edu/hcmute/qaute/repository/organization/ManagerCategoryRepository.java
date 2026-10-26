package vn.edu.hcmute.qaute.repository.organization;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmute.qaute.entity.organization.ManagerCategory;

// Khai báo truy vấn lưu trữ dữ liệu ManagerCategory.
public interface ManagerCategoryRepository extends JpaRepository<ManagerCategory, Long> {

    // Lấy danh mục được gán cho quản lý.
    List<ManagerCategory> findByManagerId(Long managerId);

    // Lấy danh mục của danh sách quản lý.
    List<ManagerCategory> findByManagerIdIn(Collection<Long> managerIds);

    // Lấy các lượt gán theo danh sách danh mục.
    List<ManagerCategory> findByCategoryIdIn(Collection<Long> categoryIds);

    // Xóa các lượt gán của quản lý.
    void deleteByManagerId(Long managerId);
}
