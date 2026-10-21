package vn.edu.hcmute.qaute.service.identity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.email.EmailService;
import vn.edu.hcmute.qaute.service.governance.AuditService;

@Service
@Transactional
public class PasswordServiceImpl implements PasswordService {

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthSessionService authSessionService;
    private final EmailService emailService;
    private final AuditService auditService;

    public PasswordServiceImpl(UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               AuthSessionService authSessionService,
                               EmailService emailService,
                               AuditService auditService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authSessionService = authSessionService;
        this.emailService = emailService;
        this.auditService = auditService;
    }

    @Override
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);

        if (user.getPasswordHash() == null || !passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new BadRequestException("Mật khẩu hiện tại không đúng",
                    Map.of("currentPassword", "Mật khẩu hiện tại không đúng"));
        }

        if (!isStrongPassword(newPassword)) {
            throw new BadRequestException("Mật khẩu mới không đủ mạnh",
                    Map.of("newPassword", "Mật khẩu 8–64 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt"));
        }

        if (passwordEncoder.matches(newPassword, user.getPasswordHash())) {
            throw new BadRequestException("Mật khẩu mới phải khác mật khẩu hiện tại",
                    Map.of("newPassword", "Mật khẩu mới phải khác mật khẩu hiện tại"));
        }

        LocalDateTime now = LocalDateTime.now();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setPasswordChangedAt(now);

        String currentJti = SecurityUtils.currentUser()
                .map(QauteUserDetails::getSessionJti)
                .orElse(null);
        authSessionService.revokeAllExcept(userId, currentJti, "password_changed");
        auditService.log(AuditActions.AUTH_PASSWORD_CHANGED, "User", userId,
                "passwordChanged", "passwordChanged");

        emailService.sendTemplated(user.getEmail(),
                "Mật khẩu QAUTE đã được thay đổi",
                "password-changed",
                Map.of(
                        "fullName", user.getFullName(),
                        "time", now.format(TIME_FORMATTER)
                ));
    }

    private boolean isStrongPassword(String value) {
        if (value == null || value.length() < 8 || value.length() > 64) {
            return false;
        }
        return value.chars().anyMatch(Character::isUpperCase)
                && value.chars().anyMatch(Character::isLowerCase)
                && value.chars().anyMatch(Character::isDigit)
                && value.chars().anyMatch(character -> !Character.isLetterOrDigit(character));
    }
}

