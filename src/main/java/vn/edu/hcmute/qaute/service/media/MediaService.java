package vn.edu.hcmute.qaute.service.media;

import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.dto.view.MediaAssetView;

public interface MediaService {
    MediaAssetView uploadPublic(MultipartFile file, MediaUsageType usage, Long ownerId);
    MediaAssetView uploadPrivate(MultipartFile file, MediaUsageType usage, Long ownerId);
    void attach(Long assetId, MediaUsageType usage, Long refId);
    String publicUrl(Long assetId);
    void delete(Long assetId);
}
