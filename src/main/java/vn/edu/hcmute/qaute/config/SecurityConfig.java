package vn.edu.hcmute.qaute.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import vn.edu.hcmute.qaute.security.AuthEntryPoint;
import vn.edu.hcmute.qaute.security.CookieService;
import vn.edu.hcmute.qaute.security.CsrfCookieFilter;
import vn.edu.hcmute.qaute.security.JwtAuthenticationFilter;
import vn.edu.hcmute.qaute.security.JwtService;
import vn.edu.hcmute.qaute.security.QauteAccessDeniedHandler;
import vn.edu.hcmute.qaute.security.TokenAuthenticationService;
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CookieService cookieService,
            TokenAuthenticationService tokenAuthenticationService,
            JwtService jwtService,
            AuthSessionService authSessionService,
            com.fasterxml.jackson.databind.ObjectMapper objectMapper) throws Exception {
        CookieCsrfTokenRepository csrfRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        CsrfTokenRequestAttributeHandler csrfHandler = new CsrfTokenRequestAttributeHandler();

        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfRepository)
                        .csrfTokenRequestHandler(csrfHandler)
                        .ignoringRequestMatchers("/ws/**"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(login -> login.disable())
                .httpBasic(basic -> basic.disable())
                .logout(logout -> logout.disable())
                .addFilterAfter(new CsrfCookieFilter(), CsrfFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(cookieService, tokenAuthenticationService,
                        jwtService, authSessionService),
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/assets/**", "/faq/**", "/topics/**", "/search",
                                "/pages/**", "/auth/**", "/error", "/api/faqs/**", "/api/public/**")
                        .permitAll()
                        .requestMatchers("/student/**").hasRole("STUDENT")
                        .requestMatchers("/manager/**").hasRole("MANAGER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/**", "/ws/**").authenticated()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new AuthEntryPoint(objectMapper))
                        .accessDeniedHandler(new QauteAccessDeniedHandler(objectMapper)))
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                        .contentTypeOptions(content -> {
                        })
                        .referrerPolicy(referrer -> referrer
                                .policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "default-src 'self'; script-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net; "
                                        + "style-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net https://fonts.googleapis.com; "
                                        + "font-src 'self' https://cdn.jsdelivr.net https://fonts.gstatic.com data:; "
                                        + "img-src 'self' data: https://res.cloudinary.com; connect-src 'self' ws: wss:; "
                                        + "frame-ancestors 'self'")));
        return http.build();
    }
}
