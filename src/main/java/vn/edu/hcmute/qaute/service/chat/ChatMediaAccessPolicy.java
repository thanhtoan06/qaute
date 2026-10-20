package vn.edu.hcmute.qaute.service.chat;

import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.service.media.MediaAccessPolicy;

@Component
public class ChatMediaAccessPolicy implements MediaAccessPolicy {
    private final ChatAccessChecker access;

    public ChatMediaAccessPolicy(ChatAccessChecker access) {
        this.access = access;
    }

    @Override
    public MediaUsageType usage() {
        return MediaUsageType.CHAT_ATTACHMENT;
    }

    @Override
    public boolean canAccess(MediaAsset asset, Long userId, RoleCode role) {
        return asset != null && asset.getUsageType() == MediaUsageType.CHAT_ATTACHMENT
                && asset.getUsageRefId() != null && userId != null
                && access.isParticipant(asset.getUsageRefId(), userId);
    }
}
