package vn.edu.hcmute.qaute.entity.governance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.AnnouncementAudience;
import vn.edu.hcmute.qaute.common.constant.AnnouncementStatus;
import vn.edu.hcmute.qaute.common.entity.AuditableEntity;

// Biểu diễn dữ liệu Announcement được lưu trữ trong hệ thống.
@Entity
@Table(name = "announcements")
@Getter
@Setter
public class Announcement extends AuditableEntity {
    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 500)
    private String summary;

    @Column(name = "body_html", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String bodyHtml;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AnnouncementAudience audience;

    @Column(nullable = false)
    private boolean pinned;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AnnouncementStatus status;

    @Column(name = "publish_at")
    private LocalDateTime publishAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "cover_asset_id")
    private Long coverAssetId;

    @Column(name = "notify_on_publish", nullable = false)
    private boolean notifyOnPublish;

    @Column(nullable = false)
    private boolean notified;

    @Column(name = "auto_publish", nullable = false)
    private boolean autoPublish;

    @Column(name = "created_by")
    private Long createdBy;
}
