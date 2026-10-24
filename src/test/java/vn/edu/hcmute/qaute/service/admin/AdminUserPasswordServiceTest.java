package vn.edu.hcmute.qaute.service.admin;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import vn.edu.hcmute.qaute.common.constant.AuditActions;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.ForbiddenException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.dto.request.admin.UserPasswordForm;
import vn.edu.hcmute.qaute.entity.identity.AccountActivationToken;
import vn.edu.hcmute.qaute.entity.identity.PasswordResetToken;
import vn.edu.hcmute.qaute.entity.identity.Role;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.AccountActivationTokenRepository;
import vn.edu.hcmute.qaute.repository.identity.PasswordResetTokenRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;

@ExtendWith(MockitoExtension.class)
class AdminUserPasswordServiceTest {
    private static final ValidatorFactory VALIDATORS = Validation.buildDefaultValidatorFactory();
    @Mock UserRepository users;
    @Mock AuthSessionService sessions;
    @Mock PasswordResetTokenRepository resetTokens;
    @Mock AccountActivationTokenRepository activationTokens;
    @Mock AuditService audit;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
    private AdminUserPasswordService service;

    @BeforeEach
    void setUp() {
        service = new AdminUserPasswordService(users, encoder, sessions, resetTokens,
                activationTokens, audit, VALIDATORS.getValidator());
        authenticate(RoleCode.ADMIN);
    }

    @AfterEach
    void clearSecurity() { SecurityContextHolder.clearContext(); }

    @AfterAll
    static void closeValidator() { VALIDATORS.close(); }

    @ParameterizedTest
    @EnumSource(RoleCode.class)
    void adminCanSetPasswordForEveryRoleAndRevokeAllOldCredentials(RoleCode role) {
        User target = target(role);
        target.setPasswordHash(encoder.encode("Previous@12345"));
        target.setFailedLoginCount(3);
        when(users.findById(4L)).thenReturn(Optional.of(target));
        PasswordResetToken reset = new PasswordResetToken();
        AccountActivationToken activation = new AccountActivationToken();
        when(resetTokens.findByUserIdAndUsedAtIsNull(4L)).thenReturn(List.of(reset));
        when(activationTokens.findByUserIdAndUsedAtIsNull(4L)).thenReturn(List.of(activation));

        service.changePassword(4L, form("Changed@12345"));

        assertTrue(encoder.matches("Changed@12345", target.getPasswordHash()));
        assertFalse(encoder.matches("Previous@12345", target.getPasswordHash()));
        assertNotEquals("Changed@12345", target.getPasswordHash());
        assertNotNull(target.getPasswordChangedAt());
        assertNotNull(reset.getUsedAt());
        assertNotNull(activation.getUsedAt());
        assertEquals(0, target.getFailedLoginCount());
        assertEquals(role, target.getRole().getCode());
        assertEquals(UserStatus.ACTIVE, target.getStatus());
        verify(sessions).revokeAllSessions(4L, "admin_password_changed");
        verify(users).saveAndFlush(target);
        verify(audit).log(AuditActions.USER_PASSWORD_CHANGED, "User", 4L, null,
                "passwordChanged=true,sessionsRevoked=true");
    }

    @Test
    void settingInitialPasswordDoesNotActivatePendingAccount() {
        User target = target(RoleCode.STUDENT);
        target.setStatus(UserStatus.PENDING_ACTIVATION);
        when(users.findById(4L)).thenReturn(Optional.of(target));
        service.changePassword(4L, form("Changed@12345"));
        assertEquals(UserStatus.PENDING_ACTIVATION, target.getStatus());
        assertTrue(encoder.matches("Changed@12345", target.getPasswordHash()));
    }

    @Test
    void weakPasswordIsRejectedBeforeReadingOrChangingUser() {
        BadRequestException error = assertThrows(BadRequestException.class,
                () -> service.changePassword(4L, form("weak")));
        assertTrue(error.getFieldErrors().containsKey("newPassword"));
        verifyNoInteractions(users, sessions, resetTokens, activationTokens, audit);
    }

    @Test
    void mismatchingConfirmationIsRejected() {
        UserPasswordForm form = form("Changed@12345");
        form.setConfirmPassword("Other@12345");
        BadRequestException error = assertThrows(BadRequestException.class,
                () -> service.changePassword(4L, form));
        assertTrue(error.getFieldErrors().containsKey("confirmPassword"));
        verifyNoInteractions(users, sessions, resetTokens, activationTokens, audit);
    }

    @Test
    void multibytePasswordBeyondBcryptLimitIsRejected() {
        UserPasswordForm form = form("Ạ".repeat(25) + "a1@");
        BadRequestException error = assertThrows(BadRequestException.class,
                () -> service.changePassword(4L, form));
        assertTrue(error.getFieldErrors().containsKey("newPassword"));
        verifyNoInteractions(users, sessions, resetTokens, activationTokens, audit);
    }

    @Test
    void reusingCurrentPasswordIsRejectedWithoutRevokingSessions() {
        User target = target(RoleCode.MANAGER);
        target.setPasswordHash(encoder.encode("Changed@12345"));
        when(users.findById(4L)).thenReturn(Optional.of(target));
        assertThrows(BadRequestException.class,
                () -> service.changePassword(4L, form("Changed@12345")));
        verify(users, never()).saveAndFlush(any());
        verifyNoInteractions(sessions, resetTokens, activationTokens, audit);
    }

    @ParameterizedTest
    @EnumSource(value = RoleCode.class, names = {"STUDENT", "MANAGER"})
    void nonAdminCannotCallServiceDirectly(RoleCode role) {
        authenticate(role);
        assertThrows(ForbiddenException.class,
                () -> service.changePassword(4L, form("Changed@12345")));
        verifyNoInteractions(users, sessions, resetTokens, activationTokens, audit);
    }

    @Test
    void missingUserDoesNotRevokeAnySession() {
        assertThrows(NotFoundException.class,
                () -> service.changePassword(4L, form("Changed@12345")));
        verifyNoInteractions(sessions, resetTokens, activationTokens, audit);
    }

    private static User target(RoleCode role) {
        User user = User.builder().fullName("Người dùng kiểm thử").email("user@example.com")
                .role(Role.builder().code(role).build()).status(UserStatus.ACTIVE).build();
        user.setId(4L);
        return user;
    }

    private static UserPasswordForm form(String password) {
        UserPasswordForm form = new UserPasswordForm();
        form.setNewPassword(password);
        form.setConfirmPassword(password);
        return form;
    }

    private static void authenticate(RoleCode role) {
        QauteUserDetails actor = new QauteUserDetails(1L, "admin@example.com", "Admin", role, "jti", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(actor, null, actor.getAuthorities()));
    }
}
