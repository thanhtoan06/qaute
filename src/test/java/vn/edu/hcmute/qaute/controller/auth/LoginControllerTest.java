package vn.edu.hcmute.qaute.controller.auth;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.security.CookieService;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.service.identity.LoginService;

class LoginControllerTest {
    private LoginService logins;
    private CookieService cookies;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        logins = mock(LoginService.class);
        cookies = mock(CookieService.class);
        mvc = MockMvcBuilders.standaloneSetup(new LoginController(logins, cookies))
                .setViewResolvers(new InternalResourceViewResolver("/WEB-INF/views/", ".jsp"))
                .build();
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void anonymousUserCanOpenLoginForm() throws Exception {
        mvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"))
                .andExpect(model().attributeExists("form"));
        verifyNoInteractions(logins, cookies);
    }

    @ParameterizedTest
    @EnumSource(RoleCode.class)
    void authenticatedUserOpeningLoginIsRedirectedThroughDashboardController(RoleCode role)
            throws Exception {
        authenticate(role);
        mvc.perform(get("/auth/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(dashboard(role)));
        verifyNoInteractions(logins, cookies);
    }

    @ParameterizedTest
    @EnumSource(RoleCode.class)
    void authenticatedUserSubmittingOldLoginFormDoesNotCreateAnotherSession(RoleCode role)
            throws Exception {
        authenticate(role);
        mvc.perform(post("/auth/login").param("returnUrl", "https://example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(dashboard(role)));
        verifyNoInteractions(logins, cookies);
    }

    private void authenticate(RoleCode role) {
        QauteUserDetails user = new QauteUserDetails(1L, "demo@qaute.edu.vn", "Demo",
                role, "test-session", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    private String dashboard(RoleCode role) {
        return switch (role) {
            case STUDENT -> "/student/dashboard";
            case MANAGER -> "/manager/dashboard";
            case ADMIN -> "/admin/dashboard";
        };
    }
}
