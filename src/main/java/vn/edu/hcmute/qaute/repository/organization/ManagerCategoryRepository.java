package vn.edu.hcmute.qaute.repository.organization;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmute.qaute.entity.organization.ManagerCategory;

public interface ManagerCategoryRepository extends JpaRepository<ManagerCategory, Long> {

    List<ManagerCategory> findByManagerId(Long managerId);

    List<ManagerCategory> findByCategoryIdIn(Collection<Long> categoryIds);

    void deleteByManagerId(Long managerId);
}
