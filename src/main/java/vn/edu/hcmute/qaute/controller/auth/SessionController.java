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
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;

@Controller
public class SessionController {

    private final AuthSessionService authSessionService;
    private final CookieService cookieService;

    public SessionController(AuthSessionService authSessionService, CookieService cookieService) {
        this.authSessionService = authSessionService;
        this.cookieService = cookieService;
    }

    @PostMapping("/auth/refresh")
    @ResponseBody
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
}
