package vn.edu.hcmute.qaute.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;

// Hỗ trợ xác thực và kiểm soát truy cập qua JwtAuthenticationFilter.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CookieService cookieService;
    private final TokenAuthenticationService tokenAuthenticationService;
    private final JwtService jwtService;
    private final AuthSessionService authSessionService;

    // Khởi tạo bộ lọc xác thực JWT cùng các dịch vụ liên quan.
    public JwtAuthenticationFilter(CookieService cookieService,
                                   TokenAuthenticationService tokenAuthenticationService,
                                   JwtService jwtService,
                                   AuthSessionService authSessionService) {
        this.cookieService = cookieService;
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.jwtService = jwtService;
        this.authSessionService = authSessionService;
    }

    // Bỏ qua xác thực JWT cho tài nguyên tĩnh.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/assets/") || "/auth/refresh-if-needed".equals(path);
    }

    // Xác thực access token hoặc làm mới phiên trước khi tiếp tục chuỗi lọc.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> accessToken = cookieService.getAccessToken(request);
            Optional<QauteUserDetails> user = accessToken.flatMap(
                    tokenAuthenticationService::authenticateAccessToken);
            if (user.isEmpty()) {
                boolean shouldRefresh = accessToken.isEmpty()
                        || accessToken.map(jwtService::isExpired).orElse(false);
                if (shouldRefresh) {
                    user = refresh(request, response);
                }
            }
            user.ifPresent(this::setAuthentication);
        } catch (RuntimeException exception) {
            cookieService.clear(response);
        }
        filterChain.doFilter(request, response);
    }

    // Làm mới phiên từ refresh token và xác thực access token mới.
    private Optional<QauteUserDetails> refresh(HttpServletRequest request, HttpServletResponse response) {
        return cookieService.getRefreshToken(request)
                .flatMap(token -> authSessionService.refresh(token, request.getHeader("User-Agent"),
                        request.getRemoteAddr()))
                .flatMap(tokens -> {
                    cookieService.writeTokens(request, response, tokens);
                    return tokenAuthenticationService.authenticateAccessToken(tokens.accessToken());
                });
    }

    // Đặt thông tin người dùng đã xác thực vào ngữ cảnh bảo mật.
    private void setAuthentication(QauteUserDetails user) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
