package vn.edu.hcmute.qaute.service.identity;

import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.util.TokenUtil;
import vn.edu.hcmute.qaute.common.validation.StrongPasswordValidator;
import vn.edu.hcmute.qaute.entity.identity.AccountActivationToken;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.AccountActivationTokenRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.governance.AuditService;

@Service
public class AccountActivationService {

    private final AccountActivationTokenRepository accountActivationTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthSessionService authSessionService;
    private final AuditService auditService;

    public AccountActivationService(AccountActivationTokenRepository accountActivationTokenRepository,
                                    UserRepository userRepository,
                                    PasswordEncoder passwordEncoder,
                                    AuthSessionService authSessionService,
                                    AuditService auditService) {
        this.accountActivationTokenRepository = accountActivationTokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authSessionService = authSessionService;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public boolean isTokenValid(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            return false;
        }
        String tokenHash = TokenUtil.sha256Hex(rawToken);
        return accountActivationTokenRepository.findByTokenHash(tokenHash)
                .filter(token -> token.getUsedAt() == null && LocalDateTime.now().isBefore(token.getExpiresAt()))
                .flatMap(token -> userRepository.findById(token.getUserId()))
                .filter(user -> user.getStatus() == UserStatus.PENDING_ACTIVATION || user.getStatus() == UserStatus.ACTIVE)
                .isPresent();
    }

    @Transactional
    public void activate(String rawToken, String newPassword) {
        if (!new StrongPasswordValidator().isValid(newPassword, null)) {
            throw new AppException(ErrorCode.ERR_VALIDATION,
                    "Mật khẩu 8–64 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt");
        }
        if (rawToken == null || rawToken.isBlank()) {
            throw new AppException(ErrorCode.ERR_VALIDATION, "Liên kết kích hoạt không hợp lệ hoặc đã hết hạn");
        }
        String tokenHash = TokenUtil.sha256Hex(rawToken);
        AccountActivationToken token = accountActivationTokenRepository.findByTokenHashForUpdate(tokenHash)
                .orElseThrow(() -> new AppException(ErrorCode.ERR_VALIDATION,
                        "Liên kết kích hoạt không hợp lệ hoặc đã hết hạn"));

        if (token.getUsedAt() != null || !LocalDateTime.now().isBefore(token.getExpiresAt())) {
            throw new AppException(ErrorCode.ERR_VALIDATION,
                    "Liên kết kích hoạt không hợp lệ hoặc đã hết hạn");
        }

        User user = userRepository.findById(token.getUserId())
                .filter(u -> u.getStatus() == UserStatus.PENDING_ACTIVATION || u.getStatus() == UserStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.ERR_VALIDATION,
                        "Liên kết kích hoạt không hợp lệ hoặc đã hết hạn"));

        UserStatus previousStatus = user.getStatus();
        LocalDateTime now = LocalDateTime.now();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setStatus(UserStatus.ACTIVE);
        if (user.getEmailVerifiedAt() == null) {
            user.setEmailVerifiedAt(now);
        }
        user.setPasswordChangedAt(now);
        user.setFailedLoginCount(0);
        user.setLockedUntil(null);

        token.setUsedAt(now);

        authSessionService.revokeAllSessions(user.getId(), "activation");
        auditService.log(AuditActions.AUTH_ACTIVATED, "User", user.getId(),
                "status=" + previousStatus, "status=ACTIVE");
    }
}
