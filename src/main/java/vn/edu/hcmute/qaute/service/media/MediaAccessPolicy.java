package vn.edu.hcmute.qaute.service.media;

import vn.edu.hcmute.qaute.common.constant.MediaUsageType;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.entity.media.MediaAsset;

// Xử lý nghiệp vụ MediaAccessPolicy trong hệ thống.
public interface MediaAccessPolicy {
    // Trả về mục đích sử dụng mà chính sách này quản lý.
    MediaUsageType usage();

    // Kiểm tra người dùng có quyền truy cập tài nguyên hay không.
    boolean canAccess(MediaAsset asset, Long userId, RoleCode role);
}
