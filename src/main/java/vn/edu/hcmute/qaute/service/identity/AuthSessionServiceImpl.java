package vn.edu.hcmute.qaute.service.identity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.util.TokenUtil;
import vn.edu.hcmute.qaute.dto.view.AuthSessionView;
import vn.edu.hcmute.qaute.dto.view.LoginHistoryView;
import vn.edu.hcmute.qaute.dto.view.SessionTokens;
import vn.edu.hcmute.qaute.entity.identity.AuthSession;
import vn.edu.hcmute.qaute.entity.identity.LoginHistory;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.AuthSessionRepository;
import vn.edu.hcmute.qaute.repository.identity.LoginHistoryRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.security.JwtService;
import vn.edu.hcmute.qaute.security.event.SessionRevokedEvent;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@Service
@Transactional
public class AuthSessionServiceImpl implements AuthSessionService {

    private static final int REFRESH_TOKEN_BYTES = 48;

    private final AuthSessionRepository authSessionRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SettingsService settingsService;
    private final ApplicationEventPublisher eventPublisher;

    public AuthSessionServiceImpl(AuthSessionRepository authSessionRepository,
                                  LoginHistoryRepository loginHistoryRepository,
                                  UserRepository userRepository,
                                  JwtService jwtService,
                                  SettingsService settingsService,
                                  ApplicationEventPublisher eventPublisher) {
        this.authSessionRepository = authSessionRepository;
        this.loginHistoryRepository = loginHistoryRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.settingsService = settingsService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public SessionTokens createSession(Long userId, boolean remember, String deviceInfo, String ip) {
        User user = findActiveUser(userId);
        String jti = TokenUtil.randomToken(32);
        String refreshToken = TokenUtil.randomToken(REFRESH_TOKEN_BYTES);
        int accessMaxAge = settingsService.getInt(SettingKeys.AUTH_ACCESS_TTL_MINUTES) * 60;
        int refreshMaxAge = refreshMaxAgeSeconds(remember);
        AuthSession session = authSessionRepository.save(AuthSession.builder()
                .userId(userId)
                .jti(jti)
                .refreshTokenHash(TokenUtil.sha256Hex(refreshToken))
                .deviceInfo(deviceInfo)
                .ip(ip)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshMaxAge))
                .remember(remember)
                .build());
        String accessToken = jwtService.createAccessToken(user.getId(), user.getRole().getCode(), session.getJti());
        return new SessionTokens(accessToken, refreshToken, accessMaxAge, refreshMaxAge);
    }

    @Override
    public Optional<SessionTokens> refresh(String refreshToken, String deviceInfo, String ip) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return Optional.empty();
        }
        Optional<AuthSession> found = authSessionRepository.findByRefreshTokenHash(
                TokenUtil.sha256Hex(refreshToken));
        if (found.isEmpty()) {
            return Optional.empty();
        }
        AuthSession oldSession = found.get();
        if (oldSession.getRevokedAt() != null) {
            revokeAllSessions(oldSession.getUserId(), "refresh_reuse");
            return Optional.empty();
        }
        if (!oldSession.getExpiresAt().isAfter(LocalDateTime.now())) {
            return Optional.empty();
        }
        User user = findActiveUser(oldSession.getUserId());
        oldSession.setRevokedAt(LocalDateTime.now());
        oldSession.setRevokedReason("refresh_rotated");
        oldSession.setLastUsedAt(LocalDateTime.now());
        authSessionRepository.save(oldSession);
        SessionTokens replacement = createSession(user.getId(), oldSession.isRemember(), deviceInfo, ip);
        AuthSession newSession = authSessionRepository.findByJti(
                jwtService.parse(replacement.accessToken()).jti()).orElseThrow();
        oldSession.setReplacedById(newSession.getId());
        return Optional.of(replacement);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSessionActive(String jti) {
        return jti != null && authSessionRepository.findByJti(jti)
                .map(session -> session.getRevokedAt() == null
                        && session.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    @Override
    public void revokeByJti(String jti, String reason) {
        authSessionRepository.findByJti(jti).ifPresent(session -> {
            if (revoke(session, reason)) {
                eventPublisher.publishEvent(new SessionRevokedEvent(session.getUserId(), session.getJti()));
            }
        });
    }

    @Override
    public void revokeAllSessions(Long userId, String reason) {
        authSessionRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .forEach(session -> revoke(session, reason));
        eventPublisher.publishEvent(new SessionRevokedEvent(userId, null));
    }

    @Override
    public void revokeAllExcept(Long userId, String keepJti, String reason) {
        authSessionRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .filter(session -> keepJti == null || !keepJti.equals(session.getJti()))
                .forEach(session -> {
                    if (revoke(session, reason)) {
                        eventPublisher.publishEvent(new SessionRevokedEvent(userId, session.getJti()));
                    }
                });
    }

    @Override
    public void revokeSession(Long sessionId, Long actorUserId) {
        AuthSession session = authSessionRepository.findById(sessionId).orElseThrow(NotFoundException::new);
        User actor = userRepository.findById(actorUserId).orElseThrow(NotFoundException::new);
        boolean owner = session.getUserId().equals(actorUserId);
        boolean admin = actor.getRole() != null && actor.getRole().getCode() == RoleCode.ADMIN;
        if (!owner && !admin) {
            throw new AccessDeniedException("Bạn không có quyền thu hồi phiên này");
        }
        revoke(session, "manual");
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthSessionView> listSessions(Long userId) {
        return authSessionRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(session -> new AuthSessionView(
                        session.getId(), session.getDeviceInfo(), session.getIp(),
                        session.getCreatedAt(), session.getLastUsedAt(),
                        session.getRevokedAt() != null, false))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoginHistoryView> listLoginHistory(Long userId, int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 100));
        return loginHistoryRepository.findByUserIdOrderByCreatedAtDesc(
                        userId, PageRequest.of(0, safeLimit)).stream()
                .map(history -> new LoginHistoryView(
                        history.getCreatedAt(), history.isSuccess(),
                        history.getIp(), history.getUserAgent()))
                .toList();
    }

    @Override
    public void recordLogin(Long userId, String identifier, boolean success,
                            String ip, String userAgent) {
        loginHistoryRepository.save(LoginHistory.builder()
                .userId(userId)
                .identifierMasked(maskIdentifier(identifier))
                .success(success)
                .ip(ip)
                .userAgent(userAgent)
                .build());
    }

    private User findActiveUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        if (user.getStatus() != UserStatus.ACTIVE || user.getRole() == null) {
            throw new IllegalStateException("Tài khoản không hoạt động");
        }
        return user;
    }

    private int refreshMaxAgeSeconds(boolean remember) {
        int days = settingsService.getInt(remember
                ? SettingKeys.AUTH_REFRESH_REMEMBER_TTL_DAYS
                : SettingKeys.AUTH_REFRESH_TTL_DAYS);
        long seconds = days * 86400L;
        return Math.toIntExact(seconds);
    }

    private boolean revoke(AuthSession session, String reason) {
        if (session.getRevokedAt() == null) {
            session.setRevokedAt(LocalDateTime.now());
            session.setRevokedReason(reason);
            authSessionRepository.save(session);
            return true;
        }
        return false;
    }

    private String maskIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return "***";
        }
        return identifier.substring(0, Math.min(3, identifier.length())) + "***";
    }
}
