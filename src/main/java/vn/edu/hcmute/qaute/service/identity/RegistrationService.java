package vn.edu.hcmute.qaute.service.identity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.OtpPurpose;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.DuplicateException;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.dto.request.auth.RegisterForm;
import vn.edu.hcmute.qaute.entity.identity.Role;
import vn.edu.hcmute.qaute.entity.identity.StudentProfile;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.RoleRepository;
import vn.edu.hcmute.qaute.repository.identity.StudentProfileRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final SettingsService settingsService;

    public RegistrationService(UserRepository userRepository, RoleRepository roleRepository,
                               StudentProfileRepository studentProfileRepository,
                               PasswordEncoder passwordEncoder, OtpService otpService,
                               SettingsService settingsService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.settingsService = settingsService;
    }

    @Transactional
    public void verifyRegistration(String email, String otp) {
        User user = userRepository.findByEmailIgnoreCase(email.trim().toLowerCase())
                .filter(item -> item.getStatus() == UserStatus.PENDING_VERIFICATION)
                .orElseThrow(() -> new AppException(vn.edu.hcmute.qaute.common.constant.ErrorCode.ERR_OTP_INVALID));
        otpService.verify(user, OtpPurpose.REGISTER_VERIFY, otp);
        user.setStatus(UserStatus.ACTIVE);
        user.setEmailVerifiedAt(LocalDateTime.now());
    }

    @Transactional
    public void resendOtp(String email) {
        userRepository.findByEmailIgnoreCase(email.trim().toLowerCase())
                .filter(item -> item.getStatus() == UserStatus.PENDING_VERIFICATION)
                .ifPresent(user -> otpService.issue(user, OtpPurpose.REGISTER_VERIFY, "otp-register"));
    }

    public int otpTtlMinutes() {
        return settingsService.getInt(SettingKeys.OTP_TTL_MINUTES);
    }

    @Transactional
    public String register(RegisterForm form) {
        if (!form.isAcceptTerms()) {
            throw new BadRequestException("Vui lòng đồng ý với điều khoản sử dụng",
                    Map.of("acceptTerms", "Bạn cần đồng ý với điều khoản sử dụng"));
        }
        if (!Objects.equals(form.getPassword(), form.getConfirmPassword())) {
            throw new BadRequestException("Mật khẩu nhập lại không khớp",
                    Map.of("confirmPassword", "Mật khẩu nhập lại không khớp"));
        }

        String email = form.getEmail().trim().toLowerCase();
        String mssv = normalizeMssv(form.getMssv());
        User emailUser = userRepository.findByEmailIgnoreCase(email).orElse(null);
        User mssvUser = mssv == null ? null : userRepository.findByMssv(mssv).orElse(null);

        if (emailUser != null && emailUser.getStatus() != UserStatus.PENDING_VERIFICATION) {
            throw new DuplicateException("email", "Email đã được sử dụng");
        }
        if (mssvUser != null && (emailUser == null
                || !mssvUser.getId().equals(emailUser.getId()))) {
            throw new DuplicateException("mssv", "MSSV đã được sử dụng");
        }

        User user = emailUser;
        if (user == null) {
            Role studentRole = roleRepository.findByCode(RoleCode.STUDENT)
                    .orElseThrow(() -> new IllegalStateException("Chưa cấu hình vai trò sinh viên"));
            user = User.builder()
                    .email(email)
                    .mssv(mssv)
                    .fullName(form.getFullName().trim())
                    .passwordHash(passwordEncoder.encode(form.getPassword()))
                    .passwordChangedAt(LocalDateTime.now())
                    .role(studentRole)
                    .status(UserStatus.PENDING_VERIFICATION)
                    .build();
            userRepository.save(user);
            studentProfileRepository.save(StudentProfile.builder().userId(user.getId()).build());
        } else {
            user.setEmail(email);
            user.setMssv(mssv);
            user.setFullName(form.getFullName().trim());
            user.setPasswordHash(passwordEncoder.encode(form.getPassword()));
            user.setPasswordChangedAt(LocalDateTime.now());
        }

        otpService.issue(user, OtpPurpose.REGISTER_VERIFY, "otp-register");
        return email;
    }

    private String normalizeMssv(String mssv) {
        return mssv == null || mssv.isBlank() ? null : mssv.trim();
    }
}
