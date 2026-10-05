package vn.edu.hcmute.qaute.service.identity;

import java.util.Locale;
import java.util.regex.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.dto.request.auth.LoginForm;
import vn.edu.hcmute.qaute.dto.view.LoginOutcome;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;

@Service
public class LoginService {

    private static final Pattern MSSV = Pattern.compile("^\\d{8}$");
    private static final String DUMMY_HASH =
            "$2a$10$7EqJtq98hPqEX7fNZaFWoO6sQxYw7L9n4W6K5g0Yx6qX6L0M6z6eW";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthSessionService authSessionService;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                        AuthSessionService authSessionService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authSessionService = authSessionService;
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
            return new LoginOutcome(LoginOutcome.Kind.BAD_CREDENTIALS, null, null, null);
        }
        if (!passwordEncoder.matches(form.getPassword(), user.getPasswordHash())) {
            authSessionService.recordLogin(user.getId(), identifier, false, ip, shorten(userAgent));
            return new LoginOutcome(LoginOutcome.Kind.BAD_CREDENTIALS, null, user.getEmail(), null);
        }
        if (user.getStatus() == UserStatus.PENDING_VERIFICATION) {
            authSessionService.recordLogin(user.getId(), identifier, false, ip, shorten(userAgent));
            return new LoginOutcome(LoginOutcome.Kind.NEED_VERIFY, null, user.getEmail(), null);
        }
        if (user.getStatus() == UserStatus.PENDING_ACTIVATION
                || user.getStatus() == UserStatus.DEACTIVATED) {
            authSessionService.recordLogin(user.getId(), identifier, false, ip, shorten(userAgent));
            return new LoginOutcome(LoginOutcome.Kind.BAD_CREDENTIALS, null, user.getEmail(), null);
        }
        if (user.getStatus() == UserStatus.LOCKED) {
            authSessionService.recordLogin(user.getId(), identifier, false, ip, shorten(userAgent));
            return new LoginOutcome(LoginOutcome.Kind.LOCKED_ADMIN, null, user.getEmail(), null);
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            authSessionService.recordLogin(user.getId(), identifier, false, ip, shorten(userAgent));
            return new LoginOutcome(LoginOutcome.Kind.BAD_CREDENTIALS, null, user.getEmail(), null);
        }

        var tokens = authSessionService.createSession(user.getId(), form.isRemember(),
                shorten(userAgent), ip);
        user.setLastLoginAt(java.time.LocalDateTime.now());
        authSessionService.recordLogin(user.getId(), identifier, true, ip, shorten(userAgent));
        return new LoginOutcome(LoginOutcome.Kind.OK, tokens, user.getEmail(),
                user.getRole().getCode().name());
    }

    private String shorten(String value) {
        if (value == null) {
            return null;
        }
        return value.length() <= 255 ? value : value.substring(0, 255);
    }
}
