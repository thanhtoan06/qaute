package vn.edu.hcmute.qaute.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class SiteMeshConfig {

    @Bean
    public FilterRegistrationBean<ConfigurableSiteMeshFilter> siteMeshFilter() {
        FilterRegistrationBean<ConfigurableSiteMeshFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new PublicSiteMeshFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }

    private static class PublicSiteMeshFilter extends ConfigurableSiteMeshFilter {

        @Override
        protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
            builder.addDecoratorPath("/student/*", "/WEB-INF/decorators/student.jsp")
                    .addDecoratorPath("/manager/*", "/WEB-INF/decorators/manager.jsp")
                    .addDecoratorPath("/admin/*", "/WEB-INF/decorators/admin.jsp")
                    .addDecoratorPath("/auth/*", "/WEB-INF/decorators/auth.jsp")
                    .addDecoratorPath("/*", "/WEB-INF/decorators/public.jsp")
                    .addExcludedPath("/api/*")
                    .addExcludedPath("/assets/*")
                    .addExcludedPath("/ws/*")
                    .addExcludedPath("/error*");
        }
    }
}
