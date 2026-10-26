package vn.edu.hcmute.qaute.config;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.servlet.descriptor.JspPropertyGroupDescriptor;
import jakarta.servlet.descriptor.TaglibDescriptor;

import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.JspConfigDescriptorImpl;
import org.apache.tomcat.util.descriptor.web.JspPropertyGroup;
import org.apache.tomcat.util.descriptor.web.JspPropertyGroupDescriptorImpl;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Đặt mã hóa nguồn UTF-8 cho MỌI file JSP và JSP fragment.
 *
 * Tomcat 10.1 đọc file JSP theo ISO-8859-1 nếu chính file đó không khai báo pageEncoding. Các trang của
 * dự án chỉ include taglibs.jspf (đã có page directive), nhưng page directive trong file được include
 * không đổi cách đọc file chính, nên chữ tiếng Việt bị vỡ ("Yêu cầu" thành "YÃªu cáº§u").
 *
 * Không thêm pageEncoding vào từng JSP: Tomcat báo lỗi 500 khi khai báo trùng với taglibs.jspf.
 * Tomcat nhúng không đọc web.xml nên cấu hình jsp-property-group (tương đương thẻ page-encoding
 * trong web.xml) được đăng ký bằng mã ở đây.
 */
@Configuration
public class JspEncodingConfig {

    // Đăng ký cấu hình UTF-8 cho các trang JSP.
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> jspPageEncodingCustomizer() {
        return factory -> factory.addContextCustomizers(this::applyUtf8PageEncoding);
    }

    // Áp dụng mã hóa UTF-8 cho JSP và JSP fragment.
    private void applyUtf8PageEncoding(Context context) {
        JspPropertyGroup group = new JspPropertyGroup();
        group.addUrlPattern("*.jsp");
        group.addUrlPattern("*.jspf");
        group.setPageEncoding("UTF-8");

        Collection<JspPropertyGroupDescriptor> groups = new ArrayList<>();
        groups.add(new JspPropertyGroupDescriptorImpl(group));
        Collection<TaglibDescriptor> taglibs = new ArrayList<>();
        context.setJspConfigDescriptor(new JspConfigDescriptorImpl(groups, taglibs));
    }
}
