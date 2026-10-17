package vn.edu.hcmute.qaute.service.identity;

import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.validation.StrongPasswordValidator;
import vn.edu.hcmute.qaute.entity.identity.AccountActivationToken;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.AccountActivationTokenRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;

@Service
public class AccountActivationService {
    private final AccountActivationTokenRepository tokens;
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final AuthSessionService sessions;

    public AccountActivationService(AccountActivationTokenRepository tokens, UserRepository users,
                                    PasswordEncoder passwordEncoder, AuthSessionService sessions) {
        this.tokens = tokens;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.sessions = sessions;
    }

    @Transactional(readOnly = true)
    public boolean isTokenValid(String rawToken) {
        AccountActivationToken token = findValid(rawToken, false);
        return token != null && users.findById(token.getUserId())
                .map(user -> user.getStatus() == UserStatus.PENDING_ACTIVATION
                        || user.getStatus() == UserStatus.ACTIVE)
                .orElse(false);
    }

    @Transactional
    public void activate(String rawToken, String newPassword) {
        if (!new StrongPasswordValidator().isValid(newPassword, null)) {
            throw new BadRequestException("Mật khẩu 8–64 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt");
        }
        AccountActivationToken token = findValid(rawToken, true);
        if (token == null) {
            throw new BadRequestException("Liên kết kích hoạt không hợp lệ hoặc đã hết hạn");
        }
        User user = users.findById(token.getUserId())
                .orElseThrow(() -> new BadRequestException("Tài khoản không còn tồn tại"));
        if (user.getStatus() != UserStatus.PENDING_ACTIVATION && user.getStatus() != UserStatus.ACTIVE) {
            throw new BadRequestException("Tài khoản không thể kích hoạt");
        }
        LocalDateTime now = LocalDateTime.now();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setPasswordChangedAt(now);
        user.setStatus(UserStatus.ACTIVE);
        if (user.getEmailVerifiedAt() == null) {
            user.setEmailVerifiedAt(now);
        }
        user.setFailedLoginCount(0);
        user.setLockedUntil(null);
        token.setUsedAt(now);
        sessions.revokeAllSessions(user.getId(), "account_activated");
    }

    private AccountActivationToken findValid(String rawToken, boolean lock) {
        if (rawToken == null || !rawToken.matches("[0-9a-fA-F]{64}")) {
            return null;
        }
        String hash = InvitationServiceImpl.hash(rawToken.toLowerCase());
        return (lock ? tokens.findByTokenHashForUpdate(hash) : tokens.findByTokenHash(hash))
                .filter(token -> token.getUsedAt() == null && token.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElse(null);
    }
}
