package vn.edu.hcmute.qaute.service.media;

import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;

public interface MediaAccessPolicy {
    MediaUsageType usage();

    boolean canAccess(MediaAsset asset, Long userId, RoleCode role);
}
