package vn.edu.hcmute.qaute.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.common.constant.UserStatus;
import vn.edu.hcmute.qaute.common.web.ClientIpResolver;
import vn.edu.hcmute.qaute.entity.identity.User;
import vn.edu.hcmute.qaute.repository.identity.UserRepository;
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;

// Hỗ trợ xác thực và kiểm soát truy cập qua GoogleSsoSuccessHandler.
@Component
@Profile("google")
public class GoogleSsoSuccessHandler implements AuthenticationSuccessHandler {

    private static final Pattern MSSV = Pattern.compile("^\\d{8}$");
    private static final String STUDENT_DOMAIN = "@student.hcmute.edu.vn";

    private final UserRepository userRepository;
    private final AuthSessionService authSessionService;
    private final CookieService cookieService;

    // Khởi tạo bộ xử lý đăng nhập Google cùng các dịch vụ phiên và tài khoản.
    public GoogleSsoSuccessHandler(UserRepository userRepository,
                                   AuthSessionService authSessionService,
                                   CookieService cookieService) {
        this.userRepository = userRepository;
        this.authSessionService = authSessionService;
        this.cookieService = cookieService;
    }

    // Xác minh tài khoản Google rồi tạo phiên đăng nhập cho sinh viên hợp lệ.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (!(authentication instanceof OAuth2AuthenticationToken oauth)
                || !(oauth.getPrincipal() instanceof OAuth2User principal)) {
            reject(response, "Google không trả về thông tin tài khoản hợp lệ");
            return;
        }

        String email = principal.getAttribute("email");
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        if (!normalizedEmail.endsWith(STUDENT_DOMAIN)) {
            reject(response, "Chỉ chấp nhận email sinh viên @student.hcmute.edu.vn");
            return;
        }

        String mssv = normalizedEmail.substring(0,
                normalizedEmail.length() - STUDENT_DOMAIN.length());
        if (!MSSV.matcher(mssv).matches()) {
            reject(response, "Email Google không chứa MSSV hợp lệ");
            return;
        }

        User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseGet(() -> userRepository.findByMssv(mssv).orElse(null));
        if (user == null || user.getRole().getCode() != RoleCode.STUDENT
                || user.getStatus() != UserStatus.ACTIVE) {
            reject(response, "Tài khoản chưa được quản trị viên cấp trong QAUTE");
            return;
        }

        user.setEmail(normalizedEmail);
        user.setMssv(mssv);
        user.setEmailVerifiedAt(java.time.LocalDateTime.now());
        userRepository.save(user);
        var tokens = authSessionService.createSession(user.getId(), false,
                shorten(request.getHeader("User-Agent")), ClientIpResolver.resolve(request));
        cookieService.writeTokens(request, response, tokens);
        response.sendRedirect("/student/dashboard");
    }

    // Từ chối đăng nhập và chuyển người dùng về trang đăng nhập kèm lý do.
    private void reject(HttpServletResponse response, String message) throws IOException {
        response.sendRedirect("/auth/login?oauthError="
                + java.net.URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8));
    }

    // Giới hạn chuỗi đầu vào tối đa 255 ký tự.
    private String shorten(String value) {
        if (value == null) {
            return "";
        }
        return value.length() <= 255 ? value : value.substring(0, 255);
    }
}
