package vn.edu.hcmute.qaute.service.chat;

import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;
import vn.edu.hcmute.qaute.service.media.MediaAccessPolicy;

// Xử lý nghiệp vụ ChatMediaAccessPolicy trong hệ thống.
@Component
public class ChatMediaAccessPolicy implements MediaAccessPolicy {
    private final ChatAccessChecker access;

    // Khởi tạo chính sách truy cập tệp đính kèm chat.
    public ChatMediaAccessPolicy(ChatAccessChecker access) {
        this.access = access;
    }

    @Override
    // Trả về loại mục đích sử dụng của tệp chat.
    public MediaUsageType usage() {
        return MediaUsageType.CHAT_ATTACHMENT;
    }

    @Override
    // Kiểm tra người dùng có quyền truy cập tệp đính kèm của phiên chat hay không.
    public boolean canAccess(MediaAsset asset, Long userId, RoleCode role) {
        return asset != null && asset.getUsageType() == MediaUsageType.CHAT_ATTACHMENT
                && asset.getUsageRefId() != null && userId != null
                && access.isParticipant(asset.getUsageRefId(), userId);
    }
}
