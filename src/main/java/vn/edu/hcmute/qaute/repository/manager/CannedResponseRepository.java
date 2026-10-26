package vn.edu.hcmute.qaute.repository.manager;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmute.qaute.common.constant.CannedResponseScope;
import vn.edu.hcmute.qaute.entity.manager.CannedResponse;

// Khai báo truy vấn lưu trữ dữ liệu CannedResponse.
public interface CannedResponseRepository extends JpaRepository<CannedResponse, Long>, JpaSpecificationExecutor<CannedResponse> {
    // Tìm phản hồi của chủ sở hữu theo phạm vi và sắp xếp theo tiêu đề.
    List<CannedResponse> findByOwnerIdAndScopeOrderByTitleAsc(Long ownerId, CannedResponseScope scope);

    // Tìm phản hồi theo phạm vi và danh sách danh mục.
    List<CannedResponse> findByScopeAndCategoryIdInOrderByTitleAsc(
            CannedResponseScope scope, Collection<Long> categoryIds);

    // Tìm phản hồi không thuộc danh mục theo phạm vi.
    List<CannedResponse> findByScopeAndCategoryIdIsNullOrderByTitleAsc(CannedResponseScope scope);

    // Tăng số lượt sử dụng của phản hồi.
    @Modifying
    @Query("UPDATE CannedResponse c SET c.usageCount = c.usageCount + 1 WHERE c.id = :id")
    int incrementUsageCount(@Param("id") Long id);
}
