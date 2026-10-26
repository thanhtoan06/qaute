package vn.edu.hcmute.qaute.security;

import java.time.Instant;
import vn.edu.hcmute.qaute.common.constant.RoleCode;

// Hỗ trợ xác thực và kiểm soát truy cập qua JwtClaimsView.
public record JwtClaimsView(Long userId, RoleCode role, String jti, Instant expiresAt) {
}
