package vn.edu.hcmute.qaute.entity.media;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.MediaDeliveryType;
import vn.edu.hcmute.qaute.common.constant.MediaStatus;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

@Entity
@Table(name = "media_assets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaAsset extends AuditableEntity {

    @Column(name = "public_id", nullable = false, length = 255, unique = true)
    private String publicId;

    @Column(name = "resource_type", nullable = false, length = 20)
    private String resourceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type", nullable = false, length = 20)
    private MediaDeliveryType deliveryType;

    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;

    @Column(name = "size_bytes", nullable = false)
    private Long sizeBytes;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "original_filename", nullable = false, length = 255)
    private String originalFilename;

    @Column(name = "checksum_sha256", length = 64)
    private String checksumSha256;

    @Column(name = "folder", nullable = false, length = 150)
    private String folder;

    @Column(name = "owner_id")
    private Long ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "usage_type", nullable = false, length = 30)
    private MediaUsageType usageType;

    @Column(name = "usage_ref_id")
    private Long usageRefId;

    /**
     * Chỉ lưu URL bảo mật này cho ảnh được phép phân phối công khai.
     * Việc kiểm tra loại tài nguyên và quyền truy cập sẽ thực hiện ở service upload.
     */
    @Column(name = "secure_url", length = 500)
    private String secureUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private MediaStatus status;
}
