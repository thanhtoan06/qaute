package vn.edu.hcmute.qaute.service.identity;

import java.util.List;
import java.util.Optional;
import vn.edu.hcmute.qaute.dto.view.AuthSessionView;
import vn.edu.hcmute.qaute.dto.view.LoginHistoryView;
import vn.edu.hcmute.qaute.dto.view.SessionTokens;

public interface AuthSessionService {

    SessionTokens createSession(Long userId, boolean remember, String deviceInfo, String ip);

    Optional<SessionTokens> refresh(String refreshToken, String deviceInfo, String ip);

    boolean isSessionActive(String jti);

    void revokeByJti(String jti, String reason);

    void revokeAllSessions(Long userId, String reason);

    void revokeAllExcept(Long userId, String keepJti, String reason);

    void revokeSession(Long sessionId, Long actorUserId);

    List<AuthSessionView> listSessions(Long userId);

    List<LoginHistoryView> listLoginHistory(Long userId, int limit);

    void recordLogin(Long userId, String identifier, boolean success, String ip, String userAgent);
}
