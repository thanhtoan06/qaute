package vn.edu.hcmute.qaute.security;

import java.time.Instant;
import vn.edu.hcmute.qaute.common.constant.RoleCode;

public record JwtClaimsView(Long userId, RoleCode role, String jti, Instant expiresAt) {
}
