package vn.edu.hcmute.qaute.service.identity;

import java.util.Locale;
import java.util.regex.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.dto.request.auth.LoginForm;
import vn.edu.hcmute.qaute.dto.view.LoginOutcome;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.email.EmailService;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@Service
public class LoginService {

    private static final Pattern MSSV = Pattern.compile("^\\d{8}$");
    private static final String DUMMY_HASH =
            "$2a$10$7EqJtq98hPqEX7fNZaFWoO6sQxYw7L9n4W6K5g0Yx6qX6L0M6z6eW";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthSessionService authSessionService;
    private final SettingsService settingsService;
    private final EmailService emailService;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                        AuthSessionService authSessionService, SettingsService settingsService,
                        EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authSessionService = authSessionService;
        this.settingsService = settingsService;
        this.emailService = emailService;
    }

    @Transactional
    public LoginOutcome login(LoginForm form, String ip, String userAgent) {
        String identifier = form.getIdentifier().trim();
        String normalized = identifier.toLowerCase(Locale.ROOT);
        User user = MSSV.matcher(identifier).matches()
                ? userRepository.findByMssv(identifier).orElse(null)
                : userRepository.findByEmailIgnoreCase(normalized).orElse(null);
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

    private LoginOutcome outcome(LoginOutcome.Kind kind,
                                 vn.edu.hcmute.qaute.dto.view.SessionTokens tokens,
                                 String email, String role, long lockedMinutes) {
        return new LoginOutcome(kind, tokens, email, role, lockedMinutes);
    }

    private String shorten(String value) {
        if (value == null) {
            return null;
        }
        return value.length() <= 255 ? value : value.substring(0, 255);
    }
}
