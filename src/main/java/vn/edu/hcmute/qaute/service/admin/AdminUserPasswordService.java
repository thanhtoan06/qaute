package vn.edu.hcmute.qaute.service.admin;

import jakarta.validation.Validator;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.admin.UserPasswordForm;
import vn.edu.hcmute.qaute.repository.identity.AccountActivationTokenRepository;
import vn.edu.hcmute.qaute.repository.identity.PasswordResetTokenRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.security.SecurityUtils;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;

@Service
@Transactional
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserPasswordService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthSessionService sessions;
    private final PasswordResetTokenRepository resetTokens;
    private final AccountActivationTokenRepository activationTokens;
    private final AuditService audit;
    private final Validator validator;

    public AdminUserPasswordService(UserRepository users, PasswordEncoder encoder,
            AuthSessionService sessions, PasswordResetTokenRepository resetTokens,
            AccountActivationTokenRepository activationTokens, AuditService audit,
            Validator validator) {
        this.users = users;
        this.encoder = encoder;
        this.sessions = sessions;
        this.resetTokens = resetTokens;
        this.activationTokens = activationTokens;
        this.audit = audit;
        this.validator = validator;
    }

    public void changePassword(Long userId, UserPasswordForm form) {
        var actor = SecurityUtils.requireUser();
        if (actor.getRoleCode() != RoleCode.ADMIN || !actor.isEnabled()) {
            throw new ForbiddenException();
        }
        validate(form);
        var user = users.findById(userId).orElseThrow(NotFoundException::new);
        if (user.getPasswordHash() != null
                && encoder.matches(form.getNewPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Mật khẩu mới phải khác mật khẩu hiện tại",
                    Map.of("newPassword", "Mật khẩu mới phải khác mật khẩu hiện tại"));
        }
        LocalDateTime now = LocalDateTime.now();
        user.setPasswordHash(encoder.encode(form.getNewPassword()));
        user.setPasswordChangedAt(now);
        user.setFailedLoginCount(0);
        user.setLockedUntil(null);

        // Các liên kết cũ không được phép ghi đè mật khẩu vừa được Admin đặt.
        resetTokens.findByUserIdAndUsedAtIsNull(userId).forEach(token -> token.setUsedAt(now));
        activationTokens.findByUserIdAndUsedAtIsNull(userId).forEach(token -> token.setUsedAt(now));
        sessions.revokeAllSessions(userId, "admin_password_changed");
        users.saveAndFlush(user);
        audit.log(AuditActions.USER_PASSWORD_CHANGED, "User", userId, null,
                "passwordChanged=true,sessionsRevoked=true");
    }

    private void validate(UserPasswordForm form) {
        if (form == null) throw new BadRequestException("Vui lòng nhập mật khẩu mới");
        Map<String, String> errors = new LinkedHashMap<>();
        validator.validate(form).forEach(violation ->
                errors.putIfAbsent(violation.getPropertyPath().toString(), violation.getMessage()));
        if (!Objects.equals(form.getNewPassword(), form.getConfirmPassword())) {
            errors.put("confirmPassword", "Mật khẩu xác nhận không khớp");
        }
        if (form.getNewPassword() != null
                && form.getNewPassword().getBytes(StandardCharsets.UTF_8).length > 72) {
            errors.put("newPassword", "Mật khẩu không được vượt quá 72 byte UTF-8");
        }
        if (!errors.isEmpty()) throw new BadRequestException("Vui lòng kiểm tra mật khẩu mới", errors);
    }
}
