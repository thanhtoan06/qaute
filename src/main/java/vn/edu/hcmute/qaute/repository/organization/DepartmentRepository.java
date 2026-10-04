package vn.edu.hcmute.qaute.repository.organization;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmute.qaute.entity.organization.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    List<Department> findAllByOrderBySortOrderAscNameAsc();

    List<Department> findByActiveTrueOrderBySortOrderAscNameAsc();
}
