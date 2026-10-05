package vn.edu.hcmute.qaute.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.dto.view.SessionTokens;

@Component
public class CookieService {

    public static final String ACCESS_COOKIE = "QAUTE_AT";
    public static final String REFRESH_COOKIE = "QAUTE_RT";

    public void writeTokens(HttpServletResponse response, SessionTokens tokens) {
        writeTokens(response, tokens, false);
    }

    public void writeTokens(HttpServletRequest request, HttpServletResponse response,
                            SessionTokens tokens) {
        writeTokens(response, tokens, request.isSecure());
    }

    public void clear(HttpServletResponse response) {
        response.addHeader("Set-Cookie", cookie(ACCESS_COOKIE, "", "/", 0, false, "Lax").toString());
        response.addHeader("Set-Cookie", cookie(REFRESH_COOKIE, "", "/auth", 0, false, "Strict").toString());
    }

    public Optional<String> getAccessToken(HttpServletRequest request) {
        return getCookie(request, ACCESS_COOKIE);
    }

    public Optional<String> getRefreshToken(HttpServletRequest request) {
        return getCookie(request, REFRESH_COOKIE);
    }

    private void writeTokens(HttpServletResponse response, SessionTokens tokens, boolean secure) {
        response.addHeader("Set-Cookie", cookie(ACCESS_COOKIE, tokens.accessToken(), "/",
                tokens.accessMaxAgeSeconds(), secure, "Lax").toString());
        response.addHeader("Set-Cookie", cookie(REFRESH_COOKIE, tokens.refreshToken(), "/auth",
                tokens.refreshMaxAgeSeconds(), secure, "Strict").toString());
    }

    private ResponseCookie cookie(String name, String value, String path, int maxAge,
                                  boolean secure, String sameSite) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secure)
                .path(path)
                .maxAge(maxAge)
                .sameSite(sameSite)
                .build();
    }

    private Optional<String> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
