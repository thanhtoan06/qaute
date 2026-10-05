package vn.edu.hcmute.qaute.service.identity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;
import vn.edu.hcmute.qaute.common.constant.OtpPurpose;
import vn.edu.hcmute.qaute.common.constant.SettingKeys;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.RateLimitException;
import vn.edu.hcmute.qaute.common.util.OtpGenerator;
import vn.edu.hcmute.qaute.common.util.TokenUtil;
import vn.edu.hcmute.qaute.config.AppProperties;
import vn.edu.hcmute.qaute.entity.identity.OtpCode;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.OtpCodeRepository;
import vn.edu.hcmute.qaute.service.email.EmailService;
import vn.edu.hcmute.qaute.service.system.SettingsService;

@Service
public class OtpService {

    private final OtpCodeRepository otpCodeRepository;
    private final SettingsService settingsService;
    private final AppProperties appProperties;
    private final EmailService emailService;

    public OtpService(OtpCodeRepository otpCodeRepository, SettingsService settingsService,
                      AppProperties appProperties, EmailService emailService) {
        this.otpCodeRepository = otpCodeRepository;
        this.settingsService = settingsService;
        this.appProperties = appProperties;
        this.emailService = emailService;
    }

    @Transactional
    public void issue(User user, OtpPurpose purpose, String templateName) {
        LocalDateTime now = LocalDateTime.now();
        int cooldownSeconds = settingsService.getInt(SettingKeys.OTP_RESEND_COOLDOWN_SECONDS);
        otpCodeRepository.findFirstByEmailAndPurposeOrderByIdDesc(user.getEmail(), purpose)
                .ifPresent(latest -> {
                    long elapsed = Duration.between(latest.getCreatedAt(), now).getSeconds();
                    if (elapsed < cooldownSeconds) {
                        throw new RateLimitException((int) (cooldownSeconds - elapsed),
                                "Vui lòng thử lại sau " + (cooldownSeconds - elapsed) + " giây");
                    }
                });

        int maxPerHour = settingsService.getInt(SettingKeys.OTP_MAX_SEND_PER_HOUR);
        long sentCount = otpCodeRepository.countByEmailAndPurposeAndCreatedAtAfter(
                user.getEmail(), purpose, now.minusHours(1));
        if (sentCount >= maxPerHour) {
            throw new RateLimitException(3600,
                    "Bạn đã vượt quá số lần gửi mã trong một giờ");
        }

        otpCodeRepository.findFirstByUserIdAndPurposeAndConsumedAtIsNullOrderByIdDesc(
                        user.getId(), purpose)
                .ifPresent(previous -> previous.setConsumedAt(now));

        String otp = OtpGenerator.sixDigits();
        int ttlMinutes = settingsService.getInt(SettingKeys.OTP_TTL_MINUTES);
        OtpCode code = OtpCode.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .purpose(purpose)
                .codeHash(TokenUtil.hmacSha256Hex(appProperties.getOtp().getHmacSecret(),
                        otp + ":" + user.getId() + ":" + purpose))
                .expiresAt(now.plusMinutes(ttlMinutes))
                .attempts(0)
                .build();
        otpCodeRepository.save(code);

        emailService.sendTemplated(user.getEmail(), "Mã xác thực QAUTE", templateName,
                Map.of("fullName", user.getFullName(), "otp", otp,
                        "ttlMinutes", String.valueOf(ttlMinutes)));
    }

    @Transactional
    public void verify(User user, OtpPurpose purpose, String code) {
        OtpCode otp = otpCodeRepository
                .findFirstByUserIdAndPurposeAndConsumedAtIsNullOrderByIdDesc(user.getId(), purpose)
                .orElseThrow(() -> new AppException(ErrorCode.ERR_OTP_INVALID));
        LocalDateTime now = LocalDateTime.now();
        if (!now.isBefore(otp.getExpiresAt())) {
            throw new AppException(ErrorCode.ERR_OTP_EXPIRED);
        }
        int maxAttempts = settingsService.getInt(SettingKeys.OTP_MAX_ATTEMPTS);
        if (otp.getAttempts() >= maxAttempts) {
            throw new AppException(ErrorCode.ERR_OTP_LOCKED);
        }

        String expected = TokenUtil.hmacSha256Hex(appProperties.getOtp().getHmacSecret(),
                code + ":" + user.getId() + ":" + purpose);
        if (!TokenUtil.constantTimeEquals(otp.getCodeHash(), expected)) {
            otp.setAttempts(otp.getAttempts() + 1);
            int remaining = Math.max(0, maxAttempts - otp.getAttempts());
            throw new AppException(ErrorCode.ERR_OTP_INVALID,
                    "Mã OTP không đúng, bạn còn " + remaining + " lần thử");
        }
        otp.setConsumedAt(now);
    }

    @Transactional(readOnly = true)
    public int secondsUntilResend(String email, OtpPurpose purpose) {
        return otpCodeRepository.findFirstByEmailAndPurposeOrderByIdDesc(email, purpose)
                .map(latest -> {
                    int cooldown = settingsService.getInt(SettingKeys.OTP_RESEND_COOLDOWN_SECONDS);
                    long elapsed = Duration.between(latest.getCreatedAt(), LocalDateTime.now()).getSeconds();
                    return (int) Math.max(0, cooldown - elapsed);
                })
                .orElse(0);
    }
}
