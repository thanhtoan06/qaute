package vn.edu.hcmute.qaute.websocket;

import java.net.URI;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import vn.edu.hcmute.qaute.config.AppProperties;
import vn.edu.hcmute.qaute.security.CookieService;
import vn.edu.hcmute.qaute.security.TokenAuthenticationService;

// Hỗ trợ giao tiếp thời gian thực qua WsHandshakeInterceptor.
@Component
public class WsHandshakeInterceptor implements HandshakeInterceptor {
    private final AppProperties properties;
    private final CookieService cookies;
    private final TokenAuthenticationService tokens;

    // Khởi tạo dịch vụ xác thực và cấu hình ứng dụng.
    public WsHandshakeInterceptor(AppProperties properties, CookieService cookies,
            TokenAuthenticationService tokens) {
        this.properties = properties;
        this.cookies = cookies;
        this.tokens = tokens;
    }

    @Override
    // Kiểm tra nguồn gốc và xác thực phiên trước khi bắt tay.
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler handler, Map<String, Object> attributes) {
        String origin = request.getHeaders().getOrigin();
        if (origin != null && !allowedOrigin(origin)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }
        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        var user = cookies.getAccessToken(servletRequest.getServletRequest())
                .flatMap(tokens::authenticateAccessToken)
                .filter(details -> details.isEnabled());
        if (user.isEmpty()) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        attributes.put("userDetails", user.get());
        if (servletRequest.getServletRequest().getCookies() != null) {
            for (var cookie : servletRequest.getServletRequest().getCookies()) {
                if ("XSRF-TOKEN".equals(cookie.getName())) {
                    attributes.put("xsrf", cookie.getValue());
                    break;
                }
            }
        }
        return true;
    }

    // Kiểm tra origin có thuộc danh sách được cho phép hay không.
    private boolean allowedOrigin(String origin) {
        try {
            URI uri = URI.create(origin);
            String host = uri.getHost();
            if (host == null || !("http".equalsIgnoreCase(uri.getScheme())
                    || "https".equalsIgnoreCase(uri.getScheme()))) {
                return false;
            }
            return "localhost".equalsIgnoreCase(host)
                    || "127.0.0.1".equals(host)
                    || host.equalsIgnoreCase(URI.create(properties.getBaseUrl()).getHost());
        } catch (IllegalArgumentException | NullPointerException exception) {
            return false;
        }
    }

    @Override
    // Xử lý bước hoàn tất handshake.
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler handler, Exception exception) {
    }
}
