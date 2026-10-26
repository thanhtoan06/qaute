package vn.edu.hcmute.qaute.repository.media;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;

// Khai báo truy vấn lưu trữ dữ liệu MediaAsset.
public interface MediaAssetRepository extends JpaRepository<MediaAsset, Long> {

    // Lấy các tài nguyên có trạng thái và thời điểm tạo phù hợp.
    List<MediaAsset> findByStatusAndCreatedAtBefore(MediaStatus status, LocalDateTime before);

    // Lấy các tài nguyên theo loại và mã tham chiếu sử dụng.
    List<MediaAsset> findByUsageTypeAndUsageRefId(MediaUsageType usageType, Long usageRefId);
}
