package vn.edu.hcmute.qaute.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmute.qaute.common.constant.AnnouncementAudience;

// Đóng gói dữ liệu đầu vào cho thao tác Announcement.
@Getter
@Setter
public class AnnouncementForm {
    private Long id;
    @NotBlank @Size(min = 5, max = 200)
    private String title;
    @NotBlank @Size(min = 10, max = 500)
    private String summary;
    @NotBlank
    private String bodyHtml;
    @NotNull
    private AnnouncementAudience audience;
    private boolean pinned;
    private LocalDateTime publishAt;
    private LocalDateTime expiresAt;
    private Long coverAssetId;
    private boolean notifyOnPublish;
    private boolean autoPublish;
}
