package vn.edu.hcmute.qaute.service.identity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.OtpPurpose;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.util.TokenUtil;
import vn.edu.hcmute.qaute.entity.identity.PasswordResetToken;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.PasswordResetTokenRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.email.EmailService;
import vn.edu.hcmute.qaute.service.governance.AuditService;

@Service
public class PasswordResetService {

    private static final Pattern MSSV_PATTERN = Pattern.compile("^\\d{8}$");
    private static final int RESET_TOKEN_BYTES = 32;
    private static final int TOKEN_TTL_MINUTES = 10;
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final OtpService otpService;
    private final AuthSessionService authSessionService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuditService auditService;

    public PasswordResetService(UserRepository userRepository,
                                PasswordResetTokenRepository passwordResetTokenRepository,
                                OtpService otpService,
                                AuthSessionService authSessionService,
                                PasswordEncoder passwordEncoder,
                                EmailService emailService,
                                AuditService auditService) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.otpService = otpService;
        this.authSessionService = authSessionService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.auditService = auditService;
    }

    @Transactional
    public void start(String identifier) {
        findActiveUser(identifier).ifPresent(user ->
                otpService.issue(user, OtpPurpose.PASSWORD_RESET, "otp-reset"));
    }

    @Transactional
    public String verifyOtpAndIssueToken(String identifier, String otp) {
        User user = findActiveUser(identifier)
                .orElseThrow(() -> new AppException(ErrorCode.ERR_OTP_INVALID));

        otpService.verify(user, OtpPurpose.PASSWORD_RESET, otp);

        invalidateExistingTokens(user.getId());

        String rawToken = TokenUtil.randomToken(RESET_TOKEN_BYTES);
        String tokenHash = TokenUtil.sha256Hex(rawToken);
        passwordResetTokenRepository.save(PasswordResetToken.builder()
                .userId(user.getId())
                .tokenHash(tokenHash)
                .expiresAt(LocalDateTime.now().plusMinutes(TOKEN_TTL_MINUTES))
                .build());

        return rawToken;
    }

    @Transactional
    public void resetPassword(String rawToken, String newPassword) {
        String tokenHash = TokenUtil.sha256Hex(rawToken);
        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(() -> new AppException(ErrorCode.ERR_VALIDATION,
                        "Phiên đặt lại mật khẩu không hợp lệ hoặc đã hết hạn"));

        if (resetToken.getUsedAt() != null || !LocalDateTime.now().isBefore(resetToken.getExpiresAt())) {
            throw new AppException(ErrorCode.ERR_VALIDATION,
                    "Phiên đặt lại mật khẩu không hợp lệ hoặc đã hết hạn");
        }

        User user = userRepository.findById(resetToken.getUserId())
                .filter(u -> u.getStatus() == UserStatus.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.ERR_VALIDATION,
                        "Phiên đặt lại mật khẩu không hợp lệ hoặc đã hết hạn"));

        if (user.getPasswordHash() != null && passwordEncoder.matches(newPassword, user.getPasswordHash())) {
            throw new AppException(ErrorCode.ERR_VALIDATION,
                    "Mật khẩu mới phải khác mật khẩu hiện tại");
        }

        LocalDateTime now = LocalDateTime.now();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setPasswordChangedAt(now);
        user.setFailedLoginCount(0);
        user.setLockedUntil(null);

        resetToken.setUsedAt(now);

        authSessionService.revokeAllSessions(user.getId(), "password_reset");
        auditService.log(AuditActions.AUTH_PASSWORD_RESET, "User", user.getId(),
                "passwordResetRequested", "passwordResetCompleted");

        emailService.sendTemplated(user.getEmail(),
                "Mật khẩu QAUTE đã được đặt lại",
                "password-changed",
                Map.of(
                        "fullName", user.getFullName(),
                        "time", now.format(TIME_FORMATTER)));
    }

    private Optional<User> findActiveUser(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return Optional.empty();
        }
        String trimmed = identifier.trim();
        Optional<User> found = MSSV_PATTERN.matcher(trimmed).matches()
                ? userRepository.findByMssv(trimmed)
                : userRepository.findByEmailIgnoreCase(trimmed.toLowerCase(Locale.ROOT));
        return found.filter(u -> u.getStatus() == UserStatus.ACTIVE);
    }

    private void invalidateExistingTokens(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        passwordResetTokenRepository.findByUserIdAndUsedAtIsNull(userId)
                .forEach(t -> t.setUsedAt(now));
    }
}
