package vn.edu.hcmute.qaute.service.email;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import vn.edu.hcmute.qaute.config.AppProperties;

// Xử lý nghiệp vụ EmailTemplateRenderer trong hệ thống.
@Component
public class EmailTemplateRenderer {

    private static final Pattern PLACEHOLDER = Pattern.compile("\\{\\{([a-zA-Z0-9_.-]+)}}");

    private final AppProperties appProperties;

    // Khởi tạo bộ kết xuất mẫu với cấu hình ứng dụng.
    public EmailTemplateRenderer(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    // Đọc mẫu email và thay thế các biến bằng dữ liệu đã mã hóa HTML.
    public String render(String templateName, Map<String, String> model) {
        ClassPathResource resource = new ClassPathResource("mail-templates/" + templateName + ".html");
        if (!resource.exists()) {
            throw new IllegalStateException("Không tìm thấy mẫu email: " + templateName);
        }

        final String template;
        try (InputStream inputStream = resource.getInputStream()) {
            template = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Không thể đọc mẫu email: " + templateName, exception);
        }

        Map<String, String> values = new HashMap<>();
        if (model != null) {
            values.putAll(model);
        }
        values.put("appName", "QAUTE");
        values.put("baseUrl", appProperties.getBaseUrl());
        Matcher matcher = PLACEHOLDER.matcher(template);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String value = values.getOrDefault(matcher.group(1), "");
            matcher.appendReplacement(result, Matcher.quoteReplacement(escapeHtml(value)));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    // Mã hóa ký tự đặc biệt để chèn an toàn vào HTML.
    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
