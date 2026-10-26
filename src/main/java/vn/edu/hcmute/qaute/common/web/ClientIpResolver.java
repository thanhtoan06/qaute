package vn.edu.hcmute.qaute.common.web;

import jakarta.servlet.http.HttpServletRequest;

// Hỗ trợ xử lý yêu cầu web dùng chung qua ClientIpResolver.
public final class ClientIpResolver {

    // Ngăn tạo đối tượng từ lớp tiện ích.
    private ClientIpResolver() {
    }

    // Lấy địa chỉ IP từ X-Forwarded-For hoặc địa chỉ kết nối.
    public static String resolve(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
