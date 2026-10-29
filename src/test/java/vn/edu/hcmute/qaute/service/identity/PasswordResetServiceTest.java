package vn.edu.hcmute.qaute.service.identity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.edu.hcmute.qaute.common.constant.OtpPurpose;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.PasswordResetTokenRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.email.EmailService;
import vn.edu.hcmute.qaute.service.governance.AuditService;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Mock
    private OtpService otpService;
    @Mock
    private AuthSessionService authSessionService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailService emailService;
    @Mock
    private AuditService auditService;

    private PasswordResetService passwordResetService;

    @BeforeEach
    void setUp() {
        passwordResetService = new PasswordResetService(userRepository, passwordResetTokenRepository,
                otpService, authSessionService, passwordEncoder, emailService, auditService);
    }

    @Test
    void doesNotIssueOtpWhenAccountIsNotActive() {
        User user = User.builder().email("student@example.com")
                .status(UserStatus.PENDING_ACTIVATION).build();
        when(userRepository.findByEmailIgnoreCase("student@example.com"))
                .thenReturn(Optional.of(user));

        assertFalse(passwordResetService.start("student@example.com"));

        verify(otpService, never()).issue(user, OtpPurpose.PASSWORD_RESET, "otp-reset");
    }

    @Test
    void issuesOtpWhenAccountIsActive() {
        User user = User.builder().email("student@example.com")
                .status(UserStatus.ACTIVE).build();
        when(userRepository.findByEmailIgnoreCase("student@example.com"))
                .thenReturn(Optional.of(user));

        assertTrue(passwordResetService.start("student@example.com"));

        verify(otpService).issue(user, OtpPurpose.PASSWORD_RESET, "otp-reset");
    }
}
