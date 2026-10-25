package vn.edu.hcmute.qaute.controller.admin;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.Filter;
import jakarta.servlet.http.Cookie;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.AopTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.config.SecurityConfig;
import vn.edu.hcmute.qaute.dto.request.admin.UserPasswordForm;
import vn.edu.hcmute.qaute.dto.request.student.ChangePasswordForm;
import vn.edu.hcmute.qaute.dto.view.AdminProfileView;
import vn.edu.hcmute.qaute.dto.view.AdminUserDetailView;
import vn.edu.hcmute.qaute.security.CookieService;
import vn.edu.hcmute.qaute.security.JwtService;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.TokenAuthenticationService;
import vn.edu.hcmute.qaute.service.admin.AdminProfileService;
import vn.edu.hcmute.qaute.service.admin.AdminUserPasswordService;
import vn.edu.hcmute.qaute.service.admin.AdminUserQueryService;
import vn.edu.hcmute.qaute.service.identity.AuthSessionService;

/** Kiểm tra phân quyền và CSRF bằng chính chuỗi bộ lọc dùng khi chạy ứng dụng. */
@SpringJUnitConfig(AdminAccountAuthorizationTest.TestConfiguration.class)
@WebAppConfiguration
class AdminAccountAuthorizationTest {
    private static final String CSRF = "admin-account-test-csrf-token";

    @Autowired WebApplicationContext context;
    @Autowired AdminUserQueryService userQueries;
    @Autowired AdminUserPasswordService passwords;
    @Autowired AdminProfileService profiles;
    @Autowired CookieService cookies;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        passwords = AopTestUtils.getUltimateTargetObject(passwords);
        profiles = AopTestUtils.getUltimateTargetObject(profiles);
        userQueries = AopTestUtils.getUltimateTargetObject(userQueries);
        reset(userQueries, passwords, profiles, cookies);
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(context.getBean("springSecurityFilterChain", Filter.class))
                .build();
    }

    @AfterEach
    void clearSecurity() {
        SecurityContextHolder.clearContext();
    }

    @ParameterizedTest
    @EnumSource(value = RoleCode.class, names = {"STUDENT", "MANAGER"})
    void nonAdminCannotOpenAccountManagementPages(RoleCode role) throws Exception {
        mvc.perform(get("/admin/users/4/password").session(session(role)))
                .andExpect(status().isForbidden());
        mvc.perform(get("/admin/profile").session(session(role)))
                .andExpect(status().isForbidden());
        verifyNoInteractions(userQueries, passwords, profiles);
    }

    @ParameterizedTest
    @EnumSource(value = RoleCode.class, names = {"STUDENT", "MANAGER"})
    void nonAdminCannotSubmitAccountChangesEvenWithValidCsrf(RoleCode role) throws Exception {
        for (String path : new String[]{"/admin/users/4/password", "/admin/profile",
                "/admin/profile/password", "/admin/profile/avatar"}) {
            mvc.perform(csrf(post(path)).session(session(role)))
                    .andExpect(status().isForbidden());
        }
        verifyNoInteractions(userQueries, passwords, profiles);
    }

    @Test
    void anonymousRequestsMustLogInBeforeUsingAccountPages() throws Exception {
        mvc.perform(get("/admin/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login?returnUrl=%2Fadmin%2Fprofile"));
        mvc.perform(get("/admin/users/4/password"))
                .andExpect(status().is3xxRedirection());
        mvc.perform(csrf(post("/admin/users/4/password")))
                .andExpect(status().is3xxRedirection());
        verifyNoInteractions(userQueries, passwords, profiles);
    }

    @Test
    void adminCanOpenTheirProfileAndUserPasswordForm() throws Exception {
        when(profiles.get(1L)).thenReturn(
                new AdminProfileView("Quản trị viên", "admin@example.com", "0901234567", null));
        when(userQueries.getDetail(4L)).thenReturn(mock(AdminUserDetailView.class));

        mvc.perform(get("/admin/profile").session(session(RoleCode.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/profile"))
                .andExpect(model().attributeExists("form", "passwordForm", "profile"));
        mvc.perform(get("/admin/users/4/password").session(session(RoleCode.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user-password"))
                .andExpect(model().attributeExists("form", "user"));
        verifyNoInteractions(passwords);
    }

    @Test
    void adminCannotChangePasswordsWithoutCsrf() throws Exception {
        for (String path : new String[]{"/admin/users/4/password", "/admin/profile/password"}) {
            mvc.perform(post(path).session(session(RoleCode.ADMIN))
                            .param("currentPassword", "Current@123")
                            .param("newPassword", "Changed@123")
                            .param("confirmPassword", "Changed@123"))
                    .andExpect(status().isForbidden());
        }
        verifyNoInteractions(userQueries, passwords, profiles);
    }

    @Test
    void adminPasswordChangeWithValidCsrfReachesService() throws Exception {
        mvc.perform(csrf(post("/admin/users/4/password")).session(session(RoleCode.ADMIN))
                        .param("newPassword", "Changed@123")
                        .param("confirmPassword", "Changed@123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/4"));
        verify(passwords).changePassword(eq(4L), any(UserPasswordForm.class));
    }

    @Test
    void resettingOwnPasswordClearsCookiesAndInvalidatesCurrentSession() throws Exception {
        MockHttpSession currentSession = session(RoleCode.ADMIN);

        MvcResult response = mvc.perform(csrf(post("/admin/users/1/password"))
                        .session(currentSession)
                        .param("newPassword", "Changed@123")
                        .param("confirmPassword", "Changed@123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login?passwordChanged=true"))
                .andReturn();

        verify(passwords).changePassword(eq(1L), any(UserPasswordForm.class));
        verify(cookies).clear(any(jakarta.servlet.http.HttpServletResponse.class));
        assertTrue(currentSession.isInvalid());
        assertThrows(IllegalStateException.class, () -> currentSession.getAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY));
    }

    @Test
    void weakUserPasswordIsRejectedAndRemovedFromViewModel() throws Exception {
        when(userQueries.getDetail(4L)).thenReturn(mock(AdminUserDetailView.class));
        MvcResult response = mvc.perform(csrf(post("/admin/users/4/password"))
                        .session(session(RoleCode.ADMIN))
                        .param("newPassword", "weak")
                        .param("confirmPassword", "weak"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user-password"))
                .andReturn();

        Map<String, Object> model = response.getModelAndView().getModel();
        assertTrue(((Map<?, ?>) model.get("errors")).containsKey("newPassword"));
        UserPasswordForm form = (UserPasswordForm) model.get("form");
        assertNull(form.getNewPassword());
        assertNull(form.getConfirmPassword());
        assertNoRejectedPassword(model, "form");
        verifyNoInteractions(passwords);
    }

    @Test
    void weakOwnPasswordDoesNotReachServiceOrStayInViewModel() throws Exception {
        when(profiles.get(1L)).thenReturn(
                new AdminProfileView("Quản trị viên", "admin@example.com", null, null));
        MvcResult response = mvc.perform(csrf(post("/admin/profile/password"))
                        .session(session(RoleCode.ADMIN))
                        .param("currentPassword", "Current@123")
                        .param("newPassword", "weak")
                        .param("confirmPassword", "weak"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/profile"))
                .andReturn();

        Map<String, Object> model = response.getModelAndView().getModel();
        assertTrue(((Map<?, ?>) model.get("passwordErrors")).containsKey("newPassword"));
        ChangePasswordForm form = (ChangePasswordForm) model.get("passwordForm");
        assertNull(form.getCurrentPassword());
        assertNull(form.getNewPassword());
        assertNull(form.getConfirmPassword());
        assertNoRejectedPassword(model, "passwordForm");
        verify(profiles, org.mockito.Mockito.never()).changePassword(any(), any());
    }

    private static void assertNoRejectedPassword(Map<String, Object> model, String formName) {
        Object binding = model.get(BindingResult.MODEL_KEY_PREFIX + formName);
        if (binding instanceof BindingResult result) {
            assertFalse(result.getFieldErrors().stream().anyMatch(error -> error.getRejectedValue() != null),
                    "Không giữ mật khẩu đã nhập trong rejectedValue của biểu mẫu");
        }
    }

    private static MockHttpServletRequestBuilder csrf(MockHttpServletRequestBuilder request) {
        return request.cookie(new Cookie("XSRF-TOKEN", CSRF)).header("X-XSRF-TOKEN", CSRF);
    }

    private static MockHttpSession session(RoleCode role) {
        QauteUserDetails user = new QauteUserDetails(1L, "admin@example.com", "Người kiểm thử",
                role, "test-session", true);
        var security = SecurityContextHolder.createEmptyContext();
        security.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, security);
        return session;
    }

    @Configuration
    @EnableWebMvc
    @EnableWebSecurity
    @Import({SecurityConfig.class, AdminUserPasswordController.class, AdminProfileController.class})
    static class TestConfiguration {
        @Bean AdminUserQueryService userQueries() { return mock(AdminUserQueryService.class); }
        @Bean AdminUserPasswordService passwords() { return mock(AdminUserPasswordService.class); }
        @Bean AdminProfileService profiles() { return mock(AdminProfileService.class); }
        @Bean CookieService cookies() { return mock(CookieService.class); }
        @Bean TokenAuthenticationService tokenAuthentication() { return mock(TokenAuthenticationService.class); }
        @Bean JwtService jwtService() { return mock(JwtService.class); }
        @Bean AuthSessionService authSessions() { return mock(AuthSessionService.class); }
        @Bean ObjectMapper objectMapper() { return new ObjectMapper(); }
        @Bean EntityManagerFactory entityManagerFactory() {
            EntityManagerFactory factory = mock(EntityManagerFactory.class);
            when(factory.createEntityManager()).thenReturn(mock(EntityManager.class));
            when(factory.getProperties()).thenReturn(Map.of());
            return factory;
        }
        @Bean InternalResourceViewResolver viewResolver() {
            return new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");
        }
    }
}
