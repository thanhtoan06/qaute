package vn.edu.hcmute.qaute.config;

import com.cloudinary.Cloudinary;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public CloudinaryProperties cloudinaryProperties(@Value("${cloudinary.url:}") String url) {
        return new CloudinaryProperties(url != null && !url.isBlank());
    }

    @Bean
    public Cloudinary cloudinary(@Value("${cloudinary.url:}") String url, CloudinaryProperties properties) {
        return properties.isConfigured() ? new Cloudinary(url) : new Cloudinary(Map.of());
    }

    public record CloudinaryProperties(boolean configured) {
        public boolean isConfigured() { return configured; }
    }
}
