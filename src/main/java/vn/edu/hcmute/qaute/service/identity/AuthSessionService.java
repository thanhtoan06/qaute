package vn.edu.hcmute.qaute.service.identity;

import java.util.List;
import java.util.Optional;
import vn.edu.hcmute.qaute.dto.view.AuthSessionView;
import vn.edu.hcmute.qaute.dto.view.LoginHistoryView;
import vn.edu.hcmute.qaute.dto.view.SessionTokens;

// Xử lý nghiệp vụ AuthSession trong hệ thống.
public interface AuthSessionService {

    // Tạo phiên đăng nhập mới cho người dùng.
    SessionTokens createSession(Long userId, boolean remember, String deviceInfo, String ip);

    // Làm mới phiên bằng refresh token.
    Optional<SessionTokens> refresh(String refreshToken, String deviceInfo, String ip);

    // Kiểm tra phiên theo mã JTI còn hoạt động hay không.
    boolean isSessionActive(String jti);

    // Thu hồi phiên theo mã JTI.
    void revokeByJti(String jti, String reason);

    // Thu hồi toàn bộ phiên của người dùng.
    void revokeAllSessions(Long userId, String reason);

    // Thu hồi mọi phiên trừ phiên được giữ lại.
    void revokeAllExcept(Long userId, String keepJti, String reason);

    // Thu hồi phiên theo mã phiên và người thực hiện.
    void revokeSession(Long sessionId, Long actorUserId);

    // Lấy danh sách phiên của người dùng.
    List<AuthSessionView> listSessions(Long userId);

    // Lấy danh sách phiên và đánh dấu phiên hiện tại.
    List<AuthSessionView> listSessions(Long userId, String currentJti);

    // Lấy lịch sử đăng nhập gần đây.
    List<LoginHistoryView> listLoginHistory(Long userId, int limit);

    // Ghi nhận kết quả đăng nhập.
    void recordLogin(Long userId, String identifier, boolean success, String ip, String userAgent);
}
