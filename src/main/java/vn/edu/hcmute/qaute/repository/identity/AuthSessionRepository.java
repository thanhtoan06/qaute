package vn.edu.hcmute.qaute.repository.identity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.identity.AuthSession;

// Khai báo truy vấn lưu trữ dữ liệu AuthSession.
public interface AuthSessionRepository extends JpaRepository<AuthSession, Long> {

    // Tìm phiên theo mã JTI.
    Optional<AuthSession> findByJti(String jti);

    // Tìm phiên theo mã băm refresh token.
    Optional<AuthSession> findByRefreshTokenHash(String refreshTokenHash);

    // Lấy các phiên còn hiệu lực của người dùng.
    List<AuthSession> findByUserIdAndRevokedAtIsNullAndExpiresAtAfter(Long userId, LocalDateTime now);

    // Lấy các phiên của người dùng theo thứ tự mới nhất.
    List<AuthSession> findByUserIdOrderByCreatedAtDesc(Long userId);
}
