package vn.edu.hcmute.qaute.config;

import com.cloudinary.Cloudinary;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Cấu hình thành phần CloudinaryConfig cho ứng dụng.
@Configuration
public class CloudinaryConfig {

    // Kiểm tra URL Cloudinary và trả về trạng thái cấu hình.
    @Bean
    public CloudinaryProperties cloudinaryProperties(@Value("${cloudinary.url:}") String url) {
        return new CloudinaryProperties(url != null && !url.isBlank());
    }

    // Tạo Cloudinary theo URL nếu dịch vụ đã được cấu hình.
    @Bean
    public Cloudinary cloudinary(@Value("${cloudinary.url:}") String url, CloudinaryProperties properties) {
        return properties.isConfigured() ? new Cloudinary(url) : new Cloudinary(Map.of());
    }

    public record CloudinaryProperties(boolean configured) {
        // Trả về trạng thái cấu hình Cloudinary.
        public boolean isConfigured() {
            return configured;
        }
    }
}
