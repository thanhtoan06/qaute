package vn.edu.hcmute.qaute.controller.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.dto.view.MediaAssetView;
import vn.edu.hcmute.qaute.dto.view.MediaStream;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.media.MediaDownloadService;
import vn.edu.hcmute.qaute.service.media.MediaService;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
public class MediaApiController {
    private final MediaService media;
    private final MediaDownloadService downloads;

    public MediaApiController(MediaService media, MediaDownloadService downloads) {
        this.media = media;
        this.downloads = downloads;
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

    @GetMapping("/api/media/{id}/download")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable Long id) {
        var user = SecurityUtils.currentUser().orElse(null);
        MediaStream media = downloads.open(id, user == null ? null : user.getId(),
                user == null ? null : user.getRoleCode());
        StreamingResponseBody body = output -> {
            try (var input = media.stream()) {
                input.transferTo(output);
            } catch (IOException exception) {
                throw new IllegalStateException("Không thể truyền tệp", exception);
            }
        };
        ContentDisposition disposition = ContentDisposition.builder(media.inline() ? "inline" : "attachment")
                .filename(media.originalName(), StandardCharsets.UTF_8).build();
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(media.mimeType()))
                .contentLength(media.sizeBytes()).header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .header("X-Content-Type-Options", "nosniff")
                .cacheControl(CacheControl.noStore().cachePrivate()).body(body);
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
