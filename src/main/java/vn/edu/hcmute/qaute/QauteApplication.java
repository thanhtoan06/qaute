package vn.edu.hcmute.qaute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

// Điểm khởi chạy ứng dụng QAUTE bằng Spring Boot.
@SpringBootApplication
public class QauteApplication {

	static {
		// Align every server-side local date/time operation with the application's Vietnam timezone.
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
	}

	// Khởi chạy ứng dụng Spring Boot.
	public static void main(String[] args) {
		SpringApplication.run(QauteApplication.class, args);
	}

}
