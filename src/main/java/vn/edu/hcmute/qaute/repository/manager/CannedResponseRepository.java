package vn.edu.hcmute.qaute.repository.manager;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.common.constant.CannedResponseScope;
import vn.edu.hcmute.qaute.entity.manager.CannedResponse;

public interface CannedResponseRepository extends JpaRepository<CannedResponse, Long> {
    List<CannedResponse> findByOwnerIdAndScopeOrderByTitleAsc(Long ownerId, CannedResponseScope scope);
    List<CannedResponse> findByScopeAndCategoryIdInOrderByTitleAsc(
            CannedResponseScope scope, Collection<Long> categoryIds);
    List<CannedResponse> findByScopeAndCategoryIdIsNullOrderByTitleAsc(CannedResponseScope scope);

    @Modifying
    @Query("UPDATE CannedResponse c SET c.usageCount = c.usageCount + 1 WHERE c.id = :id")
    int incrementUsageCount(@Param("id") Long id);
}
