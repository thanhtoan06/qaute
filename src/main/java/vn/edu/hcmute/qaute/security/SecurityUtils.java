package vn.edu.hcmute.qaute.security;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.AppException;

// Hỗ trợ xác thực và kiểm soát truy cập qua SecurityUtils.
public final class SecurityUtils {

    // Ngăn khởi tạo lớp tiện ích.
    private SecurityUtils() {
    }

    // Lấy principal hiện tại nếu đã xác thực hợp lệ.
    public static Optional<QauteUserDetails> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof QauteUserDetails user)) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    // Lấy principal hiện tại hoặc báo lỗi chưa xác thực.
    public static QauteUserDetails requireUser() {
        return currentUser().orElseThrow(() -> new AppException(ErrorCode.ERR_UNAUTHORIZED));
    }

    // Lấy mã người dùng đã xác thực.
    public static Long requireUserId() {
        return requireUser().getId();
    }

    // Kiểm tra người dùng hiện tại có vai trò được chỉ định.
    public static boolean hasRole(RoleCode role) {
        return currentUser().map(user -> user.getRoleCode() == role).orElse(false);
    }
}
