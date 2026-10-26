package vn.edu.hcmute.qaute.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

// Cấu hình thành phần SchedulingConfig cho ứng dụng.
@Configuration
@EnableScheduling
public class SchedulingConfig {

    // Tạo bộ lập lịch tác vụ với cấu hình của ứng dụng.
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(4);
        scheduler.setThreadNamePrefix("qaute-job-");
        return scheduler;
    }
}
