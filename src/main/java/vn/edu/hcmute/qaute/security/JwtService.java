package vn.edu.hcmute.qaute.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.config.AppProperties;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@Service
public class JwtService {

    private final AppProperties appProperties;
    private final SettingsService settingsService;
    private SecretKey signingKey;

    public JwtService(AppProperties appProperties, SettingsService settingsService) {
        this.appProperties = appProperties;
        this.settingsService = settingsService;
    }

    @PostConstruct
    void validateSecret() {
        String secret = appProperties.getJwt().getSecret();
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("Khóa JWT phải có tối thiểu 32 byte");
        }
        signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(Long userId, RoleCode role, String jti) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(
                settingsService.getInt(SettingKeys.AUTH_ACCESS_TTL_MINUTES) * 60L);
        var builder = Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role.name())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt));
        if (jti != null) {
            builder.id(jti);
        }
        return builder.signWith(signingKey, Jwts.SIG.HS256).compact();
    }

    public JwtClaimsView parse(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        try {
            Long userId = Long.valueOf(claims.getSubject());
            RoleCode role = RoleCode.valueOf(claims.get("role", String.class));
            Date expiration = claims.getExpiration();
            if (expiration == null) {
                throw new JwtException("Token thiếu thời hạn");
            }
            return new JwtClaimsView(userId, role, claims.getId(), expiration.toInstant());
        } catch (IllegalArgumentException exception) {
            throw new JwtException("Claim token không hợp lệ", exception);
        }
    }

    public boolean isExpired(String token) {
        try {
            return parse(token).expiresAt().isBefore(Instant.now());
        } catch (JwtException | IllegalArgumentException exception) {
            return true;
        }
    }
}
