package vn.edu.hcmute.qaute.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Cấu hình thành phần PasswordEncoderConfig cho ứng dụng.
@Configuration
public class PasswordEncoderConfig {

    // Tạo bộ mã hóa mật khẩu BCrypt.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
