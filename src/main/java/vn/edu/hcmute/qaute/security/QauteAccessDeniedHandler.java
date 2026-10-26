package vn.edu.hcmute.qaute.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import vn.edu.hcmute.qaute.common.response.ApiResponse;

// Hỗ trợ xác thực và kiểm soát truy cập qua QauteAccessDeniedHandler.
public class QauteAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    // Khởi tạo bộ xử lý từ chối truy cập với bộ chuyển đổi JSON.
    public QauteAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Trả lỗi phù hợp khi người dùng không có quyền truy cập.
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException, ServletException {
        if (request.getRequestURI().startsWith("/api/")
                || "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            objectMapper.writeValue(response.getWriter(),
                    ApiResponse.fail("Bạn không có quyền thực hiện"));
            return;
        }
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
