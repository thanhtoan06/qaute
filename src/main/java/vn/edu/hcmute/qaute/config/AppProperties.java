package vn.edu.hcmute.qaute.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// Lưu các thuộc tính cấu hình ứng dụng có thể thay đổi.
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String baseUrl;
    private Jwt jwt = new Jwt();
    private Otp otp = new Otp();
    private Demo demo = new Demo();

    @Getter
    @Setter
    public static class Jwt {
        private String secret;
    }

    @Getter
    @Setter
    public static class Otp {
        private String hmacSecret;
    }

    @Getter
    @Setter
    public static class Demo {
        private boolean seed;
    }
}
