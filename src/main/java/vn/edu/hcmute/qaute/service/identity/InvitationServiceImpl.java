package vn.edu.hcmute.qaute.service.identity;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.ErrorCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.exception.AppException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.common.util.TokenUtil;
import vn.edu.hcmute.qaute.config.AppProperties;
import vn.edu.hcmute.qaute.entity.identity.AccountActivationToken;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.AccountActivationTokenRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.email.EmailService;

@Service
@Transactional
public class InvitationServiceImpl implements InvitationService {

    private static final int ACTIVATION_TOKEN_BYTES = 32;
    private static final int EXPIRE_HOURS = 48;

    private final UserRepository userRepository;
    private final AccountActivationTokenRepository accountActivationTokenRepository;
    private final EmailService emailService;
    private final AppProperties appProperties;

    public InvitationServiceImpl(UserRepository userRepository,
                                 AccountActivationTokenRepository accountActivationTokenRepository,
                                 EmailService emailService,
                                 AppProperties appProperties) {
        this.userRepository = userRepository;
        this.accountActivationTokenRepository = accountActivationTokenRepository;
        this.emailService = emailService;
        this.appProperties = appProperties;
    }

    @Override
    public void sendInvitation(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);

        if (user.getStatus() != UserStatus.PENDING_ACTIVATION && user.getStatus() != UserStatus.ACTIVE) {
            throw new AppException(ErrorCode.ERR_INVALID_TRANSITION,
                    "Chỉ có thể gửi lời mời cho tài khoản chờ kích hoạt hoặc đang hoạt động");
        }

        LocalDateTime now = LocalDateTime.now();
        accountActivationTokenRepository.findByUserIdAndUsedAtIsNull(userId)
                .forEach(token -> token.setUsedAt(now));

        String rawToken = TokenUtil.randomToken(ACTIVATION_TOKEN_BYTES);
        String tokenHash = TokenUtil.sha256Hex(rawToken);
        accountActivationTokenRepository.save(AccountActivationToken.builder()
                .userId(userId)
                .tokenHash(tokenHash)
                .expiresAt(now.plusHours(EXPIRE_HOURS))
                .build());

        String linkUrl = appProperties.getBaseUrl() + "/auth/activate?token=" + rawToken;
        emailService.sendTemplated(user.getEmail(),
                "Lời mời kích hoạt tài khoản QAUTE",
                "invitation",
                Map.of(
                        "fullName", user.getFullName(),
                        "linkUrl", linkUrl,
                        "hours", String.valueOf(EXPIRE_HOURS)
                ));
    }
}

