package vn.edu.hcmute.qaute.service.media;

import com.cloudinary.Cloudinary;
import java.io.InputStream;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;
import vn.edu.hcmute.qaute.common.constant.MediaDeliveryType;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.exception.RateLimitException;
import vn.edu.hcmute.qaute.config.CloudinaryConfig.CloudinaryProperties;
import vn.edu.hcmute.qaute.dto.view.MediaAssetView;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.repository.media.MediaAssetRepository;
import vn.edu.hcmute.qaute.security.RateLimitService;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.system.SettingsService;

// Xử lý nghiệp vụ Media trong hệ thống.
@Service
public class MediaServiceImpl implements MediaService {
    private static final long MB = 1024L * 1024L;
    private static final Set<String> IMAGES = Set.of("jpg", "jpeg", "png", "webp", "gif");
    private static final Set<String> ATTACHMENTS = Set.of("jpg", "jpeg", "png", "webp", "gif", "pdf", "docx", "xlsx", "pptx", "txt");
    private final MediaAssetRepository assets;
    private final Cloudinary cloudinary;
    private final CloudinaryProperties cloudinaryProperties;
    private final SettingsService settings;
    private final RateLimitService rateLimits;
    private final Tika tika = new Tika();

    // Khởi tạo dịch vụ quản lý tệp phương tiện.
    public MediaServiceImpl(MediaAssetRepository assets, Cloudinary cloudinary,
                            CloudinaryProperties cloudinaryProperties, SettingsService settings,
                            RateLimitService rateLimits) {
        this.assets = assets;
        this.cloudinary = cloudinary;
        this.cloudinaryProperties = cloudinaryProperties;
        this.settings = settings;
        this.rateLimits = rateLimits;
    }

    @Override @Transactional
    // Tải lên tài nguyên với đường dẫn công khai.
    public MediaAssetView uploadPublic(MultipartFile file, MediaUsageType usage, Long ownerId) {
        return upload(file, usage, ownerId, true);
    }

    @Override @Transactional
    // Tải lên tài nguyên với quyền truy cập riêng tư.
    public MediaAssetView uploadPrivate(MultipartFile file, MediaUsageType usage, Long ownerId) {
        return upload(file, usage, ownerId, false);
    }

    // Kiểm tra và lưu tệp tải lên theo cấu hình tài nguyên.
    private MediaAssetView upload(MultipartFile file, MediaUsageType usage, Long ownerId, boolean publicDelivery) {
        if (!cloudinaryProperties.isConfigured()) throw rejected("Chưa cấu hình lưu trữ tệp");
        if (file == null || file.isEmpty()) throw rejected("Tệp tải lên không được để trống");
        if (usage == null || ownerId == null) throw rejected("Thông tin tải tệp không hợp lệ");
        if (!rateLimits.tryConsume("upload:" + ownerId, 20, Duration.ofMinutes(10))) {
            throw new RateLimitException((int) rateLimits.secondsToWait("upload:" + ownerId), "Bạn đã tải tệp quá nhiều, vui lòng thử lại sau");
        }
        String name = normalizedName(file.getOriginalFilename());
        String extension = extension(name);
        validateExtension(usage, extension);
        long maxBytes = (usage == MediaUsageType.AVATAR ? 2L : settings.getLong(SettingKeys.FILE_MAX_SIZE_MB)) * MB;
        if (file.getSize() > maxBytes) throw rejected("Tệp vượt quá dung lượng cho phép " + maxBytes / MB + " MB");
        try {
            byte[] bytes = file.getBytes();
            String mime;
            try (InputStream input = file.getInputStream()) { mime = tika.detect(input, name).toLowerCase(Locale.ROOT); }
            validateMime(extension, mime);
            String resourceType = IMAGES.contains(extension) ? "image" : "raw";
            String delivery = publicDelivery ? "upload" : "authenticated";
            Map<?, ?> uploaded = cloudinary.uploader().upload(bytes, Map.of("folder", folder(usage),
                    "resource_type", resourceType, "type", delivery));
            String publicId = value(uploaded.get("public_id"));
            String secureUrl = publicDelivery ? value(uploaded.get("secure_url")) : null;
            if (publicId.isBlank() || publicId.length() > 255 || (publicDelivery && secureUrl.isBlank())) {
                throw rejected("Phản hồi từ dịch vụ lưu trữ tệp không hợp lệ");
            }
            MediaAsset asset = MediaAsset.builder().publicId(publicId).resourceType(resourceType)
                    .deliveryType(publicDelivery ? MediaDeliveryType.UPLOAD : MediaDeliveryType.AUTHENTICATED)
                    .mimeType(mime).sizeBytes((long) bytes.length).width(number(uploaded.get("width")))
                    .height(number(uploaded.get("height"))).originalFilename(name).checksumSha256(sha256(bytes))
                    .folder(folder(usage)).ownerId(ownerId).usageType(usage).secureUrl(secureUrl)
                    .status(MediaStatus.ORPHAN).build();
            assets.save(asset);
            return view(asset);
        } catch (AppException exception) {
            throw exception;
        } catch (Exception exception) {
            throw rejected("Không thể tải tệp lên, vui lòng thử lại");
        }
    }

    @Override @Transactional
    // Gắn tài nguyên với người dùng hiện tại.
    public void attach(Long assetId, MediaUsageType usage, Long refId) {
        attach(assetId, usage, refId, SecurityUtils.requireUserId());
    }

    @Override @Transactional
    // Kiểm tra quyền sở hữu rồi gắn tài nguyên với tham chiếu.
    public void attach(Long assetId, MediaUsageType usage, Long refId, Long ownerId) {
        if (assetId == null || usage == null || refId == null || ownerId == null) throw rejected("Thông tin đính kèm tệp không hợp lệ");
        MediaAsset asset = assets.findById(assetId).orElseThrow(() -> new NotFoundException("Không tìm thấy tệp"));
        if (asset.getUsageType() != usage || asset.getStatus() == MediaStatus.DELETED
                || (asset.getUsageRefId() == null && !ownerId.equals(asset.getOwnerId()))
                || (asset.getUsageRefId() != null && !refId.equals(asset.getUsageRefId()))) {
            throw rejected("Tệp không phù hợp để đính kèm");
        }
        asset.setUsageRefId(refId);
        asset.setStatus(MediaStatus.ACTIVE);
        assets.save(asset);
    }

    @Override @Transactional(readOnly = true)
    // Trả về đường dẫn công khai hợp lệ của tài nguyên.
    public String publicUrl(Long assetId) {
        MediaAsset asset = assets.findById(assetId).orElseThrow(() -> new NotFoundException("Không tìm thấy tệp"));
        if (asset.getDeliveryType() != MediaDeliveryType.UPLOAD || asset.getStatus() == MediaStatus.DELETED || asset.getSecureUrl() == null || asset.getSecureUrl().isBlank()) throw rejected("Tệp không có đường dẫn công khai");
        return asset.getSecureUrl();
    }

    @Override @Transactional
    // Xóa tài nguyên trên dịch vụ lưu trữ và cập nhật trạng thái.
    public void delete(Long assetId) {
        MediaAsset asset = assets.findById(assetId).orElseThrow(() -> new NotFoundException("Không tìm thấy tệp"));
        if (asset.getStatus() == MediaStatus.DELETED) return;
        try {
            cloudinary.uploader().destroy(asset.getPublicId(), Map.of("resource_type", asset.getResourceType(),
                    "type", asset.getDeliveryType() == MediaDeliveryType.AUTHENTICATED ? "authenticated" : "upload"));
            asset.setStatus(MediaStatus.DELETED);
            assets.save(asset);
        } catch (Exception exception) {
            throw rejected("Không thể tải tệp lên, vui lòng thử lại");
        }
    }

    // Kiểm tra phần mở rộng có được phép theo mục đích sử dụng.
    private static void validateExtension(MediaUsageType usage, String extension) {
        Set<String> allowed = switch (usage) {
            case AVATAR, FAQ_IMAGE, ANNOUNCEMENT_COVER -> IMAGES;
            case TICKET_ATTACHMENT, CHAT_ATTACHMENT -> ATTACHMENTS;
        };
        if (!allowed.contains(extension)) throw rejected("Định dạng tệp không được hỗ trợ");
    }

    // Kiểm tra kiểu nội dung thực tế của tệp.
    private static void validateMime(String extension, String mime) {
        if (mime.equals("text/html") || mime.contains("javascript") || mime.contains("executable") || mime.contains("msdownload")) throw rejected("Tệp có kiểu nội dung không an toàn");
        boolean match = switch (extension) {
            case "jpg", "jpeg" -> mime.equals("image/jpeg"); case "png" -> mime.equals("image/png");
            case "webp" -> mime.equals("image/webp"); case "gif" -> mime.equals("image/gif");
            case "pdf" -> mime.equals("application/pdf"); case "txt" -> mime.equals("text/plain");
            case "docx", "xlsx", "pptx" -> mime.startsWith("application/vnd.openxmlformats-officedocument.") || mime.equals("application/zip");
            default -> false;
        };
        if (!match) throw rejected("Kiểu nội dung thực tế không khớp với phần mở rộng tệp");
    }

    // Chuẩn hóa tên tệp tải lên.
    private static String normalizedName(String filename) {
        String name = filename == null ? "" : filename.replace('\\', '/');
        name = name.substring(name.lastIndexOf('/') + 1).replaceAll("[\\p{Cntrl}]", "").trim();
        if (name.isBlank()) throw rejected("Tên tệp không hợp lệ");
        return name.length() <= 150 ? name : name.substring(0, 150);
    }

    // Lấy phần mở rộng đã chuẩn hóa từ tên tệp.
    private static String extension(String name) {
        int dot = name.lastIndexOf('.');
        if (dot <= 0 || dot == name.length() - 1) throw rejected("Tệp phải có phần mở rộng hợp lệ");
        return name.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    // Tạo thư mục lưu trữ theo mục đích sử dụng.
    private static String folder(MediaUsageType usage) {
        return "qaute/" + usage.name().toLowerCase(Locale.ROOT);
    }

    // Chuyển giá trị số thành số nguyên khi có thể.
    private static Integer number(Object value) {
        return value instanceof Number number ? number.intValue() : null;
    }

    // Chuyển giá trị thành chuỗi an toàn.
    private static String value(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    // Tính mã băm SHA-256 của nội dung tệp.
    private static String sha256(byte[] bytes) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
    }

    // Tạo dữ liệu hiển thị từ tài nguyên tệp.
    private static MediaAssetView view(MediaAsset asset) {
        return new MediaAssetView(asset.getId(), asset.getOriginalFilename(), asset.getMimeType(),
                asset.getSizeBytes(), asset.getSecureUrl());
    }

    // Tạo ngoại lệ khi tệp bị từ chối.
    private static AppException rejected(String message) {
        return new AppException(ErrorCode.ERR_FILE_REJECTED, message);
    }
}
