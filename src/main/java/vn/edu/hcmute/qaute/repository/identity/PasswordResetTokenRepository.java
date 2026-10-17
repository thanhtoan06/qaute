package vn.edu.hcmute.qaute.repository.identity;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.identity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    /** Lấy các token chưa dùng của một user để vô hiệu hoá khi cấp token mới. */
    List<PasswordResetToken> findByUserIdAndUsedAtIsNull(Long userId);
}
