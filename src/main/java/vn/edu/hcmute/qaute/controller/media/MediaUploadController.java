package vn.edu.hcmute.qaute.controller.media;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.media.MediaService;

@RestController
public class MediaUploadController {
    private final MediaService media;

    public MediaUploadController(MediaService media) {
        this.media = media;
    }

    @PostMapping("/api/media/upload")
    public MediaService.UploadResult upload(@RequestParam("file") MultipartFile file,
                                           @RequestParam("purpose") MediaUsageType purpose) {
        return media.upload(file, purpose, SecurityUtils.requireUserId());
    }
}
