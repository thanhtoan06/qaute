package vn.edu.hcmute.qaute;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// Cấu hình triển khai ứng dụng QAUTE trên servlet container.
public class ServletInitializer extends SpringBootServletInitializer {

	// Cấu hình nguồn ứng dụng cho servlet container.
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(QauteApplication.class);
	}

}
