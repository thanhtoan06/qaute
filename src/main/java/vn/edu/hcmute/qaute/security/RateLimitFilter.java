package vn.edu.hcmute.qaute.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.edu.hcmute.qaute.common.response.ApiResponse;
import vn.edu.hcmute.qaute.common.web.ClientIpResolver;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final ObjectMapper objectMapper;

    public RateLimitFilter(RateLimitService rateLimitService, ObjectMapper objectMapper) {
        this.rateLimitService = rateLimitService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Limit limit = limitFor(request);
        if (limit == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String key = limit.name + ":" + ClientIpResolver.resolve(request);
        if (rateLimitService.tryConsume(key, limit.capacity, limit.period)) {
            filterChain.doFilter(request, response);
            return;
        }
        long retryAfter = rateLimitService.secondsToWait(key);
        response.setHeader("Retry-After", String.valueOf(retryAfter));
        if (isApi(request)) {
            response.setStatus(429);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(),
                    ApiResponse.fail("Bạn thao tác quá nhanh, vui lòng thử lại sau"));
        } else {
            response.sendError(429);
        }
    }

    private Limit limitFor(HttpServletRequest request) {
        String method = request.getMethod();
        String path = request.getRequestURI();
        if ("POST".equals(method) && "/auth/login".equals(path)) {
            return new Limit("login", 10, Duration.ofMinutes(1));
        }
        if ("POST".equals(method) && ("/auth/register".equals(path)
                || "/auth/forgot-password".equals(path)
                || "/auth/resend-otp".equals(path)
                || "/auth/reset-password/verify".equals(path))) {
            return new Limit("auth-action", 5, Duration.ofHours(1));
        }
        if ("GET".equals(method) && ("/search".equals(path)
                || "/api/faqs/suggestions".equals(path))) {
            return new Limit("search", 60, Duration.ofMinutes(1));
        }
        if ("POST".equals(method) && "/api/media/upload".equals(path)) {
            return null;
        }
        if (path.startsWith("/api/")) {
            return new Limit("api", 120, Duration.ofMinutes(1));
        }
        return null;
    }

    private boolean isApi(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/")
                || "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    private record Limit(String name, int capacity, Duration period) {
    }
}
