package vn.edu.hcmute.qaute.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.dto.view.SessionTokens;

// Hỗ trợ xác thực và kiểm soát truy cập qua CookieService.
@Component
public class CookieService {

    public static final String ACCESS_COOKIE = "QAUTE_AT";
    public static final String REFRESH_COOKIE = "QAUTE_RT";

    // Ghi cookie phiên với cấu hình bảo mật mặc định.
    public void writeTokens(HttpServletResponse response, SessionTokens tokens) {
        writeTokens(response, tokens, false);
    }

    // Ghi cookie phiên theo trạng thái HTTPS của yêu cầu.
    public void writeTokens(HttpServletRequest request, HttpServletResponse response,
                            SessionTokens tokens) {
        writeTokens(response, tokens, request.isSecure());
    }

    // Xóa các cookie phiên hiện có trên những đường dẫn đã sử dụng.
    public void clear(HttpServletResponse response) {
        response.addHeader("Set-Cookie", cookie(ACCESS_COOKIE, "", "/", 0, false, "Lax").toString());
        response.addHeader("Set-Cookie", cookie(REFRESH_COOKIE, "", "/auth", 0, false, "Strict").toString());
        response.addHeader("Set-Cookie", cookie(REFRESH_COOKIE, "", "/", 0, false, "Lax").toString());
    }

    // Đọc access token từ cookie của yêu cầu.
    public Optional<String> getAccessToken(HttpServletRequest request) {
        return getCookie(request, ACCESS_COOKIE);
    }

    // Đọc refresh token từ cookie của yêu cầu.
    public Optional<String> getRefreshToken(HttpServletRequest request) {
        return getCookie(request, REFRESH_COOKIE);
    }

    // Ghi access token và refresh token vào phản hồi.
    private void writeTokens(HttpServletResponse response, SessionTokens tokens, boolean secure) {
        response.addHeader("Set-Cookie", cookie(ACCESS_COOKIE, tokens.accessToken(), "/",
                tokens.accessMaxAgeSeconds(), secure, "Lax").toString());
        response.addHeader("Set-Cookie", cookie(REFRESH_COOKIE, tokens.refreshToken(), "/auth",
                tokens.refreshMaxAgeSeconds(), secure, "Strict").toString());
    }

    // Tạo cookie phiên theo các thuộc tính được cung cấp.
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

    // Tìm giá trị cookie có tên tương ứng trong yêu cầu.
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
