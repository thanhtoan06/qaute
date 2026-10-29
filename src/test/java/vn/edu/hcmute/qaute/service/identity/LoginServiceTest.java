package vn.edu.hcmute.qaute.service.identity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.dto.request.auth.LoginForm;
import vn.edu.hcmute.qaute.dto.view.LoginOutcome;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.email.EmailService;
import vn.edu.hcmute.qaute.service.governance.AuditService;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthSessionService authSessionService;
    @Mock
    private SettingsService settingsService;
    @Mock
    private EmailService emailService;
    @Mock
    private AuditService auditService;

    private LoginService loginService;

    @BeforeEach
    void setUp() {
        loginService = new LoginService(userRepository, passwordEncoder, authSessionService,
                settingsService, emailService, auditService);
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "12345678"})
    void looksUpSixOrEightDigitIdentifiersAsMssv(String mssv) {
        when(userRepository.findByMssv(mssv)).thenReturn(Optional.empty());

        assertEquals(LoginOutcome.Kind.BAD_CREDENTIALS, login(mssv).kind());

        verify(userRepository).findByMssv(mssv);
        verify(userRepository, never()).findByEmailIgnoreCase(mssv);
        verify(userRepository, never()).findAllByFullNameIgnoreCase(mssv);
    }

    @Test
    void looksUpNonEmailIdentifierByFullName() {
        User user = User.builder().email("student@example.com").passwordHash("hash")
                .status(UserStatus.ACTIVE).build();
        when(userRepository.findByEmailIgnoreCase("nguyen van a")).thenReturn(Optional.empty());
        when(userRepository.findAllByFullNameIgnoreCase("Nguyen Van A"))
                .thenReturn(List.of(user));
        when(passwordEncoder.matches("wrong-password", "hash")).thenReturn(false);
        when(settingsService.getInt(SettingKeys.AUTH_MAX_FAILED_LOGINS)).thenReturn(10);

        assertEquals(LoginOutcome.Kind.BAD_CREDENTIALS, login("Nguyen Van A").kind());

        verify(userRepository).findAllByFullNameIgnoreCase("Nguyen Van A");
    }

    @Test
    void rejectsAmbiguousFullNameInsteadOfSelectingAnAccount() {
        List<User> matches = List.of(
                User.builder().email("first@example.com").build(),
                User.builder().email("second@example.com").build());
        when(userRepository.findByEmailIgnoreCase("nguyen van a")).thenReturn(Optional.empty());
        when(userRepository.findAllByFullNameIgnoreCase("Nguyen Van A")).thenReturn(matches);

        assertEquals(LoginOutcome.Kind.BAD_CREDENTIALS, login("Nguyen Van A").kind());

        verify(passwordEncoder).matches("wrong-password",
                "$2a$10$7EqJtq98hPqEX7fNZaFWoO6sQxYw7L9n4W6K5g0Yx6qX6L0M6z6eW");
    }

    private LoginOutcome login(String identifier) {
        LoginForm form = new LoginForm();
        form.setIdentifier(identifier);
        form.setPassword("wrong-password");
        return loginService.login(form, "127.0.0.1", "test-agent");
    }
}
