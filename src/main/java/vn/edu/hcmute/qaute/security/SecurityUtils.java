package vn.edu.hcmute.qaute.security;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.AppException;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<QauteUserDetails> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof QauteUserDetails user)) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public static QauteUserDetails requireUser() {
        return currentUser().orElseThrow(() -> new AppException(ErrorCode.ERR_UNAUTHORIZED));
    }

    public static Long requireUserId() {
        return requireUser().getId();
    }

    public static boolean hasRole(RoleCode role) {
        return currentUser().map(user -> user.getRoleCode() == role).orElse(false);
    }
}
