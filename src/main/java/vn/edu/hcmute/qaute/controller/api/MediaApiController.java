package vn.edu.hcmute.qaute.controller.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.dto.view.MediaAssetView;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.media.MediaService;

@RestController
public class MediaApiController {
    private final MediaService media;

    public MediaApiController(MediaService media) {
        this.media = media;
    }

    @PostMapping("/api/media/upload")
    public ApiResponse<MediaAssetView> upload(@RequestParam("file") MultipartFile file,
                                               @RequestParam("purpose") MediaUsageType purpose) {
        authorize(purpose);
        Long ownerId = SecurityUtils.requireUserId();
        MediaAssetView asset = switch (purpose) {
            case AVATAR, FAQ_IMAGE, ANNOUNCEMENT_COVER -> media.uploadPublic(file, purpose, ownerId);
            case TICKET_ATTACHMENT, CHAT_ATTACHMENT -> media.uploadPrivate(file, purpose, ownerId);
        };
        return ApiResponse.ok(asset);
    }

    private static void authorize(MediaUsageType purpose) {
        if (purpose == null) throw new ForbiddenException();
        boolean managerOrAdmin = SecurityUtils.hasRole(RoleCode.MANAGER) || SecurityUtils.hasRole(RoleCode.ADMIN);
        boolean allowed = switch (purpose) {
            case AVATAR, TICKET_ATTACHMENT, CHAT_ATTACHMENT -> SecurityUtils.currentUser().isPresent();
            case FAQ_IMAGE -> managerOrAdmin;
            case ANNOUNCEMENT_COVER -> SecurityUtils.hasRole(RoleCode.ADMIN);
        };
        if (!allowed) throw new ForbiddenException("Bạn không có quyền tải loại tệp này");
    }
}
