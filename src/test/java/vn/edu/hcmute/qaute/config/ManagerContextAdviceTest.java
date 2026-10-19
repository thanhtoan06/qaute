package vn.edu.hcmute.qaute.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.edu.hcmute.qaute.common.constant.AvailabilityStatus;
import vn.edu.hcmute.qaute.common.constant.RoleCode;
import vn.edu.hcmute.qaute.security.QauteUserDetails;
import vn.edu.hcmute.qaute.service.manager.ManagerProfileService;

@ExtendWith(MockitoExtension.class)
class ManagerContextAdviceTest {
    @Mock ManagerProfileService profiles;
    @InjectMocks ManagerContextAdvice advice;

    @AfterEach
    void clearSecurity() { SecurityContextHolder.clearContext(); }

    @Test
    void repeatedRequestsWithinMinuteTouchPresenceOnce() {
        QauteUserDetails user = new QauteUserDetails(7L, "manager@example.com", "Manager",
                RoleCode.MANAGER, "test", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
        when(profiles.currentStatus(7L)).thenReturn(AvailabilityStatus.ONLINE);

        assertEquals(AvailabilityStatus.ONLINE, advice.managerStatus());
        assertEquals(AvailabilityStatus.ONLINE, advice.managerStatus());

        verify(profiles, times(1)).touchLastSeen(7L);
    }
}
