package vn.edu.hcmute.qaute.repository.organization;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmute.qaute.entity.organization.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    List<Category> findByDepartmentIdOrderBySortOrderAscNameAsc(Long departmentId);

    List<Category> findByParentIdOrderBySortOrderAscNameAsc(Long parentId);

    List<Category> findByActiveTrueOrderBySortOrderAscNameAsc();

    List<Category> findByParentId(Long parentId);

    long countByDepartmentId(Long departmentId);
}
