package vn.edu.hcmute.qaute.repository.media;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;

public interface MediaAssetRepository extends JpaRepository<MediaAsset, Long> {

    List<MediaAsset> findByStatusAndCreatedAtBefore(MediaStatus status, LocalDateTime before);

    List<MediaAsset> findByUsageTypeAndUsageRefId(MediaUsageType usageType, Long usageRefId);
}
