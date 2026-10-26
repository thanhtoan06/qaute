package vn.edu.hcmute.qaute.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

// Hỗ trợ xác thực và kiểm soát truy cập qua CsrfCookieFilter.
public class CsrfCookieFilter extends OncePerRequestFilter {

    // Tải token CSRF để cookie CSRF được phát sinh khi cần.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        if (token != null) {
            token.getToken();
        }
        filterChain.doFilter(request, response);
    }
}
