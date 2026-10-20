package vn.edu.hcmute.qaute.websocket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.socket.WebSocketHandler;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.config.AppProperties;
import vn.edu.hcmute.qaute.security.CookieService;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.security.TokenAuthenticationService;

@ExtendWith(MockitoExtension.class)
class WsHandshakeInterceptorTest {
    @Mock AppProperties properties;
    @Mock CookieService cookies;
    @Mock TokenAuthenticationService tokens;
    @Mock WebSocketHandler handler;
    @InjectMocks WsHandshakeInterceptor interceptor;

    @Test
    void rejectsForeignOriginBeforeAuthentication() {
        when(properties.getBaseUrl()).thenReturn("https://qaute.example.edu");
        MockHttpServletRequest request = request("https://evil.example.edu");
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        ServletServerHttpResponse response = new ServletServerHttpResponse(servletResponse);
        assertFalse(interceptor.beforeHandshake(new ServletServerHttpRequest(request), response,
                handler, new HashMap<>()));
        assertEquals(HttpStatus.FORBIDDEN.value(), servletResponse.getStatus());
    }

    @Test
    void validOriginAndAccessCookiePopulateSessionAttributes() {
        when(properties.getBaseUrl()).thenReturn("https://qaute.example.edu");
        MockHttpServletRequest request = request("https://qaute.example.edu");
        request.setCookies(new Cookie("QAUTE_AT", "jwt"), new Cookie("XSRF-TOKEN", "csrf"));
        QauteUserDetails user = new QauteUserDetails(7L, "m@example.edu", "Manager",
                RoleCode.MANAGER, "jti-1", true);
        when(cookies.getAccessToken(request)).thenReturn(Optional.of("jwt"));
        when(tokens.authenticateAccessToken("jwt")).thenReturn(Optional.of(user));
        Map<String, Object> attributes = new HashMap<>();
        assertTrue(interceptor.beforeHandshake(new ServletServerHttpRequest(request),
                new ServletServerHttpResponse(new MockHttpServletResponse()), handler, attributes));
        assertSame(user, attributes.get("userDetails"));
        assertEquals("csrf", attributes.get("xsrf"));
    }

    private MockHttpServletRequest request(String origin) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/ws");
        request.addHeader("Origin", origin);
        return request;
    }
}
