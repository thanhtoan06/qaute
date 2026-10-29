package vn.edu.hcmute.qaute.service.identity;

import java.util.Locale;
import java.util.regex.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.dto.request.auth.LoginForm;
import vn.edu.hcmute.qaute.dto.view.LoginOutcome;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.email.EmailService;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.system.SettingsService;

// Xử lý nghiệp vụ Login trong hệ thống.
@Service
public class LoginService {

    private static final Pattern MSSV = Pattern.compile("^\\d{6}(?:\\d{2})?$");
    private static final String DUMMY_HASH =
            "$2a$10$7EqJtq98hPqEX7fNZaFWoO6sQxYw7L9n4W6K5g0Yx6qX6L0M6z6eW";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthSessionService authSessionService;
    private final SettingsService settingsService;
    private final EmailService emailService;
    private final AuditService auditService;

    // Khởi tạo dịch vụ đăng nhập.
    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                        AuthSessionService authSessionService, SettingsService settingsService,
                        EmailService emailService, AuditService auditService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authSessionService = authSessionService;
        this.settingsService = settingsService;
        this.emailService = emailService;
        this.auditService = auditService;
    }

    // Xác thực thông tin đăng nhập và xử lý trạng thái tài khoản.
    @Transactional
    public LoginOutcome login(LoginForm form, String ip, String userAgent) {
        String identifier = form.getIdentifier().trim();
        String normalized = identifier.toLowerCase(Locale.ROOT);
        User user = findUser(identifier, normalized);
        if (user == null) {
            passwordEncoder.matches(form.getPassword(), DUMMY_HASH);
            authSessionService.recordLogin(null, identifier, false, ip, shorten(userAgent));
            return outcome(LoginOutcome.Kind.BAD_CREDENTIALS, null, null, null, 0);
        }
        LocalDateTime now = LocalDateTime.now();
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(now)) {
            long minutes = Math.max(1, java.time.Duration.between(now, user.getLockedUntil()).toMinutes());
            authSessionService.recordLogin(user.getId(), identifier, false, ip, shorten(userAgent));
            return outcome(LoginOutcome.Kind.LOCKED_TEMP, null, user.getEmail(), null, minutes);
        }
        if (!passwordEncoder.matches(form.getPassword(), user.getPasswordHash())) {
            if (user.getStatus() == UserStatus.ACTIVE) {
                user.setFailedLoginCount(user.getFailedLoginCount() + 1);
                int maxFailed = settingsService.getInt(SettingKeys.AUTH_MAX_FAILED_LOGINS);
                if (user.getFailedLoginCount() >= maxFailed) {
                    int lockMinutes = settingsService.getInt(SettingKeys.AUTH_LOCK_MINUTES);
                    user.setFailedLoginCount(0);
                    user.setLockedUntil(now.plusMinutes(lockMinutes));
                    emailService.sendTemplated(user.getEmail(), "Tài khoản QAUTE bị khóa tạm thời",
                            "account-locked", java.util.Map.of(
                                    "fullName", user.getFullName(),
                                    "minutes", String.valueOf(lockMinutes)));
                    authSessionService.recordLogin(user.getId(), identifier, false, ip, shorten(userAgent));
                    auditService.log(AuditActions.AUTH_LOCKED, "User", user.getId(), "status=ACTIVE",
                            "lockedUntil=" + user.getLockedUntil());
                    return outcome(LoginOutcome.Kind.LOCKED_TEMP, null, user.getEmail(), null,
                            lockMinutes);
                }
            }
            authSessionService.recordLogin(user.getId(), identifier, false, ip, shorten(userAgent));
            return outcome(LoginOutcome.Kind.BAD_CREDENTIALS, null, user.getEmail(), null, 0);
        }
        if (user.getStatus() == UserStatus.PENDING_VERIFICATION) {
            authSessionService.recordLogin(user.getId(), identifier, false, ip, shorten(userAgent));
            return outcome(LoginOutcome.Kind.NEED_VERIFY, null, user.getEmail(), null, 0);
        }
        if (user.getStatus() == UserStatus.PENDING_ACTIVATION
                || user.getStatus() == UserStatus.DEACTIVATED) {
            authSessionService.recordLogin(user.getId(), identifier, false, ip, shorten(userAgent));
            return outcome(LoginOutcome.Kind.BAD_CREDENTIALS, null, user.getEmail(), null, 0);
        }
        if (user.getStatus() == UserStatus.LOCKED) {
            authSessionService.recordLogin(user.getId(), identifier, false, ip, shorten(userAgent));
            return outcome(LoginOutcome.Kind.LOCKED_ADMIN, null, user.getEmail(), null, 0);
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            authSessionService.recordLogin(user.getId(), identifier, false, ip, shorten(userAgent));
            return outcome(LoginOutcome.Kind.BAD_CREDENTIALS, null, user.getEmail(), null, 0);
        }

        var tokens = authSessionService.createSession(user.getId(), form.isRemember(),
                shorten(userAgent), ip);
        user.setFailedLoginCount(0);
        user.setLockedUntil(null);
        user.setLastLoginAt(now);
        authSessionService.recordLogin(user.getId(), identifier, true, ip, shorten(userAgent));
        return outcome(LoginOutcome.Kind.OK, tokens, user.getEmail(),
                user.getRole().getCode().name(), 0);
    }

    private User findUser(String identifier, String normalized) {
        if (MSSV.matcher(identifier).matches()) {
            return userRepository.findByMssv(identifier).orElse(null);
        }
        User emailUser = userRepository.findByEmailIgnoreCase(normalized).orElse(null);
        if (emailUser != null) {
            return emailUser;
        }
        var nameMatches = userRepository.findAllByFullNameIgnoreCase(identifier);
        return nameMatches.size() == 1 ? nameMatches.get(0) : null;
    }

    // Tạo kết quả đăng nhập theo trạng thái xác thực.
    private LoginOutcome outcome(LoginOutcome.Kind kind,
                                 vn.edu.hcmute.qaute.dto.view.SessionTokens tokens,
                                 String email, String role, long lockedMinutes) {
        return new LoginOutcome(kind, tokens, email, role, lockedMinutes);
    }

    // Giới hạn độ dài thông tin thiết bị được lưu.
    private String shorten(String value) {
        if (value == null) {
            return null;
        }
        return value.length() <= 255 ? value : value.substring(0, 255);
    }
}
