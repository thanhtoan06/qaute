package vn.edu.hcmute.qaute.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.edu.hcmute.qaute.dto.view.SessionTokens;
import vn.edu.hcmute.qaute.security.CookieService;
import vn.edu.hcmute.qaute.security.JwtService;
import vn.edu.hcmute.qaute.security.TokenAuthenticationService;
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;

// Tiếp nhận và điều phối các yêu cầu giao diện cho Session.
@Controller
public class SessionController {

    private final AuthSessionService authSessionService;
    private final CookieService cookieService;
    private final JwtService jwtService;
    private final TokenAuthenticationService tokenAuthenticationService;

    // Khởi tạo bộ điều khiển phiên đăng nhập.
    public SessionController(AuthSessionService authSessionService, CookieService cookieService,
            JwtService jwtService, TokenAuthenticationService tokenAuthenticationService) {
        this.authSessionService = authSessionService;
        this.cookieService = cookieService;
        this.jwtService = jwtService;
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @PostMapping("/auth/refresh")
    @ResponseBody
    // Làm mới token phiên từ cookie và cập nhật kết quả.
    public ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
        Optional<SessionTokens> tokens = cookieService.getRefreshToken(request)
                .flatMap(token -> authSessionService.refresh(token,
                        request.getHeader("User-Agent"), request.getRemoteAddr()));
        if (tokens.isPresent()) {
            cookieService.writeTokens(request, response, tokens.get());
            return ResponseEntity.noContent().build();
        }
        cookieService.clear(response);
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
    }

    @PostMapping("/auth/refresh-if-needed")
    @ResponseBody
    // Làm mới access token đã hết hạn trước khi STOMP thử kết nối lại.
    public ResponseEntity<Void> refreshIfNeeded(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> accessToken = cookieService.getAccessToken(request);
        if (accessToken.isPresent() && !jwtService.isExpired(accessToken.get())) {
            if (tokenAuthenticationService.authenticateAccessToken(accessToken.get()).isPresent()) {
                return ResponseEntity.noContent().build();
            }
            cookieService.clear(response);
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }

        Optional<SessionTokens> tokens = cookieService.getRefreshToken(request)
                .flatMap(token -> authSessionService.refresh(token,
                        request.getHeader("User-Agent"), request.getRemoteAddr()));
        if (tokens.isPresent()) {
            cookieService.writeTokens(request, response, tokens.get());
            return ResponseEntity.noContent().build();
        }
        cookieService.clear(response);
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
    }
}
