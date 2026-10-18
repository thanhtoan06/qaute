package vn.edu.hcmute.qaute.service.media;

import com.cloudinary.Cloudinary;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Map;
import java.util.Set;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmute.qaute.common.constant.MediaDeliveryType;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;
import vn.edu.hcmute.qaute.security.SecurityUtils;

@Service
public class MediaService {
    private static final long MAX_FAQ_IMAGE_BYTES = 5L * 1024 * 1024;
    private static final Set<String> IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp", "image/gif");
    private final MediaAssetRepository assets;
    private final String cloudinaryUrl;

    public MediaService(MediaAssetRepository assets, @Value("${cloudinary.url:}") String cloudinaryUrl) {
        this.assets = assets;
        this.cloudinaryUrl = cloudinaryUrl;
    }

    @Transactional
    public UploadResult upload(MultipartFile file, MediaUsageType purpose, Long actorId) {
        if (purpose != MediaUsageType.FAQ_IMAGE
                || !(SecurityUtils.hasRole(RoleCode.ADMIN) || SecurityUtils.hasRole(RoleCode.MANAGER))) {
            throw new BadRequestException("Không có quyền tải ảnh FAQ");
        }
        if (file == null || file.isEmpty() || file.getSize() > MAX_FAQ_IMAGE_BYTES) {
            throw new BadRequestException("Ảnh phải có dung lượng từ 1 byte đến 5 MB");
        }
        if (cloudinaryUrl.isBlank()) {
            throw new BadRequestException("Chưa cấu hình dịch vụ lưu ảnh");
        }
        try {
            byte[] bytes = file.getBytes();
            String mime = new Tika().detect(bytes);
            if (!IMAGE_TYPES.contains(mime)) {
                throw new BadRequestException("Chỉ hỗ trợ ảnh JPEG, PNG, WebP hoặc GIF");
            }
            Map<?, ?> uploaded = new Cloudinary(cloudinaryUrl).uploader().upload(bytes,
                    Map.of("folder", "qaute/faq", "resource_type", "image"));
            String publicId = String.valueOf(uploaded.get("public_id"));
            String publicUrl = String.valueOf(uploaded.get("secure_url"));
            if (publicId.length() > 255 || !publicUrl.startsWith("https://res.cloudinary.com/")) {
                throw new BadRequestException("Phản hồi lưu ảnh không hợp lệ");
            }
            MediaAsset asset = MediaAsset.builder().publicId(publicId).resourceType("image")
                    .deliveryType(MediaDeliveryType.UPLOAD).mimeType(mime).sizeBytes((long) bytes.length)
                    .width(number(uploaded.get("width"))).height(number(uploaded.get("height")))
                    .originalFilename(filename(file.getOriginalFilename()))
                    .checksumSha256(HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes)))
                    .folder("qaute/faq").ownerId(actorId).usageType(MediaUsageType.FAQ_IMAGE)
                    .secureUrl(publicUrl).status(MediaStatus.ORPHAN).build();
            assets.save(asset);
            return new UploadResult(asset.getId(), publicUrl);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new BadRequestException("Không thể tải ảnh lên, vui lòng thử lại");
        }
    }

    @Transactional
    public void attach(Long assetId, MediaUsageType usageType, Long usageRefId) {
        MediaAsset asset = assets.findById(assetId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ảnh"));
        if (usageType != MediaUsageType.FAQ_IMAGE || usageRefId == null
                || asset.getUsageType() != usageType || asset.getStatus() == MediaStatus.DELETED
                || (asset.getUsageRefId() == null
                    && !SecurityUtils.requireUserId().equals(asset.getOwnerId()))
                || (asset.getUsageRefId() != null && !usageRefId.equals(asset.getUsageRefId()))) {
            throw new BadRequestException("Ảnh không hợp lệ hoặc không thuộc tài khoản hiện tại");
        }
        asset.setUsageRefId(usageRefId);
        asset.setStatus(MediaStatus.ACTIVE);
        assets.save(asset);
    }

    private static Integer number(Object value) {
        return value instanceof Number number ? number.intValue() : null;
    }

    private static String filename(String name) {
        if (name == null || name.isBlank()) return "image";
        String safe = name.replace('\\', '/');
        safe = safe.substring(safe.lastIndexOf('/') + 1);
        return safe.length() > 255 ? safe.substring(0, 255) : safe;
    }

    public record UploadResult(Long assetId, String publicUrl) {
    }
}
