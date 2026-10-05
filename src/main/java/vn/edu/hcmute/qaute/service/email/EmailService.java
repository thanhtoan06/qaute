package vn.edu.hcmute.qaute.service.email;

import java.util.Map;

public interface EmailService {

    void sendTemplated(String toEmail, String subject, String templateName, Map<String, String> model);
}
