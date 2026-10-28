package vn.edu.hcmute.qaute;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.TimeZone;

// Cấu hình triển khai ứng dụng QAUTE trên servlet container.
public class ServletInitializer extends SpringBootServletInitializer {

	// Cấu hình nguồn ứng dụng cho servlet container.
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
		return application.sources(QauteApplication.class);
	}

}
