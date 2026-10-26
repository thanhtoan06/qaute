package vn.edu.hcmute.qaute.repository.identity;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmute.qaute.entity.identity.PasswordResetToken;

// Khai báo truy vấn lưu trữ dữ liệu PasswordResetToken.
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    // Tìm token đặt lại mật khẩu theo mã băm.
    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    /** Lấy các token chưa dùng của một user để vô hiệu hoá khi cấp token mới. */
    // Lấy các token đặt lại mật khẩu chưa sử dụng của người dùng.
    List<PasswordResetToken> findByUserIdAndUsedAtIsNull(Long userId);
}
