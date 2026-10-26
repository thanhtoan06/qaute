package vn.edu.hcmute.qaute.service.media;

import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.dto.view.MediaAssetView;

// Xử lý nghiệp vụ Media trong hệ thống.
public interface MediaService {
    // Tải lên tệp công khai và trả về thông tin tài nguyên.
    MediaAssetView uploadPublic(MultipartFile file, MediaUsageType usage, Long ownerId);

    // Tải lên tệp riêng tư và trả về thông tin tài nguyên.
    MediaAssetView uploadPrivate(MultipartFile file, MediaUsageType usage, Long ownerId);

    // Gắn tài nguyên với tham chiếu của người dùng hiện tại.
    void attach(Long assetId, MediaUsageType usage, Long refId);

    // Gắn tài nguyên với tham chiếu và chủ sở hữu chỉ định.
    void attach(Long assetId, MediaUsageType usage, Long refId, Long ownerId);

    // Lấy đường dẫn công khai của tài nguyên.
    String publicUrl(Long assetId);

    // Xóa tài nguyên lưu trữ.
    void delete(Long assetId);
}
