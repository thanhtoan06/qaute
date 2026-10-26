package vn.edu.hcmute.qaute.service.email;

import java.util.Map;

// Xử lý nghiệp vụ Email trong hệ thống.
public interface EmailService {

    // Ghi email theo mẫu vào hàng đợi gửi.
    void sendTemplated(String toEmail, String subject, String templateName, Map<String, String> model);
}
