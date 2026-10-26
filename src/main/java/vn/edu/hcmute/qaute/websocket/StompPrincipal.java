package vn.edu.hcmute.qaute.websocket;

import java.security.Principal;

// Hỗ trợ giao tiếp thời gian thực qua StompPrincipal.
public record StompPrincipal(Long userId) implements Principal {
    @Override
    // Trả về mã người dùng dưới dạng tên principal.
    public String getName() {
        return String.valueOf(userId);
    }
}
