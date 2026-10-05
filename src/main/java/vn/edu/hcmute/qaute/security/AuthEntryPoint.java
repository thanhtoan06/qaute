package vn.edu.hcmute.qaute.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import vn.edu.hcmute.qaute.common.response.ApiResponse;

public class AuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public AuthEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException, ServletException {
        if (isApi(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            objectMapper.writeValue(response.getWriter(), ApiResponse.fail("Bạn cần đăng nhập"));
            return;
        }
        String path = request.getRequestURI();
        if (path == null || !path.startsWith("/")) {
            path = "/";
        }
        String query = request.getQueryString();
        String returnUrl = query == null || query.isBlank() ? path : path + "?" + query;
        response.sendRedirect("/auth/login?returnUrl="
                + URLEncoder.encode(returnUrl, StandardCharsets.UTF_8));
    }

    private boolean isApi(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/")
                || "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }
}
