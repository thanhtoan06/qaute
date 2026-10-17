package vn.edu.hcmute.qaute.service.identity;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.exception.BadRequestException;
import vn.edu.hcmute.qaute.common.exception.NotFoundException;
import vn.edu.hcmute.qaute.config.AppProperties;
import vn.edu.hcmute.qaute.entity.identity.AccountActivationToken;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.AccountActivationTokenRepository;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.email.EmailService;

@Service
public class InvitationServiceImpl implements InvitationService {
    private static final SecureRandom RANDOM = new SecureRandom();
    private final UserRepository users;
    private final AccountActivationTokenRepository tokens;
    private final EmailService emails;
    private final AppProperties properties;

    public InvitationServiceImpl(UserRepository users, AccountActivationTokenRepository tokens,
                                 EmailService emails, AppProperties properties) {
        this.users = users;
        this.tokens = tokens;
        this.emails = emails;
        this.properties = properties;
    }

    @Override
    @Transactional
    public void sendInvitation(Long userId) {
        User user = users.findById(userId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        if (user.getStatus() != UserStatus.PENDING_ACTIVATION && user.getStatus() != UserStatus.ACTIVE) {
            throw new BadRequestException("Trạng thái tài khoản không cho phép gửi lời mời");
        }
        LocalDateTime now = LocalDateTime.now();
        for (AccountActivationToken token : tokens.findByUserIdAndUsedAtIsNull(userId)) {
            token.setUsedAt(now);
        }
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        String rawToken = HexFormat.of().formatHex(bytes);
        tokens.save(AccountActivationToken.builder()
                .userId(userId).tokenHash(hash(rawToken)).expiresAt(now.plusHours(48)).build());
        String baseUrl = properties.getBaseUrl();
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException("Thiếu cấu hình app.base-url để gửi lời mời");
        }
        String link = baseUrl.replaceAll("/+$", "") + "/auth/activate?token="
                + URLEncoder.encode(rawToken, StandardCharsets.UTF_8);
        emails.sendTemplated(user.getEmail(), "Kích hoạt tài khoản QAUTE", "invitation",
                Map.of("fullName", user.getFullName(), "linkUrl", link, "hours", "48"));
    }

    public static String hash(String value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new IllegalStateException("Thiếu SHA-256", e);
        }
    }
}
